package com.example.eventlottery.Admin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * Class that's purpose is to open a view that show the information of an event and the ability
 * to delete the event and delete the QR code related to the event
 */
public class AdminEventDetailsActivity extends AppCompatActivity {
    public FirebaseFirestore db;
    private boolean posterExists = false;
    private TextView profile_letter;
    private ImageView pfp;
    private UserModel organizer;

    /**
     * Empty Constructor, that grab an instance of the Firebase
     * to add to the db variable to be able to interact with the Firebase
     */
    public AdminEventDetailsActivity() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * This onCreate creates the activity that show a view of the selected event details
     * also it generates buttons with the purpose of deleting the selected event
     * and delete the QR code of the selected event
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EventModel event = (EventModel) getIntent().getExtras().getSerializable("item");
        ((TextView)findViewById(R.id.admin_event_title)).setText(event.getEventTitle());
        ((TextView)findViewById(R.id.admin_event_organizer_event_description)).setText(event.getEventDescription());
        CharSequence timeFormat  = DateFormat.format("MMMM d, yyyy ", event.getJoinDeadline().getTime());
        ((TextView)findViewById(R.id.admin_event_date)).setText(timeFormat);
        String id = event.getFacilityID();
        Task<DocumentSnapshot> task = db.collection("users").document(id).get();
        task.addOnCompleteListener((Task<DocumentSnapshot> posttask) -> {
            organizer = posttask.getResult().toObject(UserModel.class);
            ((TextView)findViewById(R.id.organizer_name)).setText(String.format("%s %s", organizer.getfName(), organizer.getlName()));
        });
        task = db.collection("facilities").document(id).get();
        task.addOnCompleteListener((Task<DocumentSnapshot> posttask) -> {
            FacilityModel facility = posttask.getResult().toObject(FacilityModel.class);
            if (facility == null) return;
            ((TextView)findViewById(R.id.facility_name)).setText(facility.getName());
        });

        findViewById(R.id.admin_event_details_delete_event_btn).setOnClickListener((View view) -> {
            db.collection("events").document(event.getEventID()).delete();
            db.collection("posters").document(event.getEventID()).delete();
            finish();
        });
        findViewById(R.id.qr_code_delete_button).setOnClickListener(v -> {
            event.randomizeHashQR();
            db.collection("events").document(event.getEventID()).set(event);
        });
        Button delete_poster = findViewById(R.id.delete_poster);

        ImageView poster = findViewById(R.id.event_poster);
        pfp = findViewById(R.id.organizer_picture);
        db.collection("posters").document(event.getEventID()).get().addOnCompleteListener( t -> {
            DocumentSnapshot document = t.getResult();
            if (document.exists()) {
                posterExists = true;
                Blob blob = document.getBlob("Blob");
                byte[] bytes = blob.toBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                poster.setImageBitmap(bitmap);
            } else {
                poster.setImageResource(R.drawable.defaultposter);
                delete_poster.setVisibility(View.GONE);
            }
        });
        db.collection("photos").document(id).get().addOnCompleteListener( t -> {
            DocumentSnapshot document = t.getResult();
            if (document.exists()){
                Blob blob = document.getBlob("Blob");
                byte[] bytes = blob.toBytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                pfp.setImageBitmap(bitmap);
            } else {
                profile_letter = findViewById(R.id.profile_letter);
                defaultOrAutogenerated(organizer);

//                pfp.setImageResource(R.drawable.defaultprofilepicture);
            }
        });
        delete_poster.setOnClickListener((view) -> {
            db.collection("posters").document(event.getEventID()).delete();
            poster.setImageBitmap(null);
        });
        FloatingActionButton back = findViewById(R.id.back);
        back.setOnClickListener((view) -> {
            onBackPressed();
        });
    }

    /**
     * check if the user need a default picture or a autogenerated one, if the user need a
     * autogenerated one the user call the method that crete one
     * @param organizer  The current organizer of the event
     */
    public void defaultOrAutogenerated(UserModel organizer){
        String initial = "";
        if (!organizer.getfName().isEmpty()){
            initial = String.valueOf(organizer.getfName().charAt(0)).toUpperCase();
        } else if(!organizer.getlName().isEmpty()){
            initial = String.valueOf(organizer.getlName().charAt(0)).toUpperCase();
        }
        if(!initial.isEmpty()){
            autoGeneratedProfilePicture(initial);
        } else{
            pfp.setImageResource(R.drawable.defaultprofilepicture);
        }
    }

    /**
     * this is the class the generate a profile picture if the user doesn't have one
     * @param finalInitial  The initial of the name of the organizer to create autogenerated
     *                      profile
     */
    public void autoGeneratedProfilePicture(String finalInitial){
        profile_letter.setVisibility(View.VISIBLE);
        profile_letter.setText(finalInitial);
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // Get the TextView's text and draw it onto the canvas
        Paint paint = profile_letter.getPaint();

        paint.setColor(ContextCompat.getColor(this, R.color.black));

        // Citation: https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
        paint.setTextAlign(Paint.Align.CENTER);
        int x_pos = (canvas.getWidth() / 2);
        int y_pos = (int) (((float) canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        // Add any colours for the profile background
        int[] colors = {
                R.color.gold,
        };
        int randColor = colors[new Random().nextInt(colors.length)];

        canvas.drawColor(ContextCompat.getColor(this, randColor));

        canvas.drawText(profile_letter.getText().toString(), x_pos, y_pos, paint);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        pfp.setImageBitmap(bitmap);
        profile_letter.setVisibility(View.GONE);
    }
}