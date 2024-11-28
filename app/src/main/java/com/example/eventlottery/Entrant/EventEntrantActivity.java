package com.example.eventlottery.Entrant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventlottery.MainActivity;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Notifications.SubscribeToTopic;
import com.example.eventlottery.Notifications.UnsubscribeFromTopic;
import com.example.eventlottery.R;
import com.example.eventlottery.Models.RemoteUserRef;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Event Entrant Activity
 * All images and poster stuff is left for part 4
 */
public class EventEntrantActivity extends AppCompatActivity {
    FloatingActionButton back;
    FirebaseFirestore db;

    private TextView organizerName;
    private DocumentReference facilityRef;
    private String organizer;
    // Event stuff and user stuff
    private RemoteUserRef userList;
    private EventModel event;
    // XML stuff
    private TextView eventDescription;
    private ImageView eventPoster;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventDate;
    private Button joinBtn;
    private Button unjoinBtn;
    private ImageView organizerProfilePicture;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private UserModel currentUser;
    private TextView profile_letter;

    /**
     * Overriding on back pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EventEntrantActivity.this, MainActivity.class);
        startActivity(i);
    }

    /**
     * Making a toast depending on the user's choice whether to allow the app to send notifications
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Notifications on", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Notifications off", Toast.LENGTH_SHORT).show();
                }
            });

    /**
     * Checks if the user has allowed the app to send notifications
     * If not, it asks the user to have notifications turned on
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {

            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    /**
     * On create of the View
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_entrant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            event = (EventModel) extras.getSerializable("eventModel");
            userList = (RemoteUserRef) extras.getSerializable("userList");
        }

        db = FirebaseFirestore.getInstance();
        eventPoster = findViewById(R.id.event_entrant_page_event_poster1);
        eventTitle = findViewById(R.id.event_entrant_page_event_title1);
        eventLocation = findViewById(R.id.event_entrant_page_event_location1);
        eventDate = findViewById(R.id.event_entrant_page_event_date1);
        joinBtn = findViewById(R.id.event_entrant_page_join_button1);
        unjoinBtn = findViewById(R.id.event_entrant_page_unjoin_button1);
        organizerProfilePicture = findViewById(R.id.event_entrant_page_profile_picture1);
        organizerName = findViewById(R.id.event_entrant_page_organizer_name1);
        eventDescription = findViewById(R.id.event_entrant_page_event_details1);

        progressBar = findViewById(R.id.progressBar1);
        linearLayout = findViewById(R.id.linearLayout1);
        back = findViewById(R.id.floatingActionButton);

        update();
        checkUserInEvent();
        Task<DocumentSnapshot> task1 = db.collection("events").document(event.getEventID()).get();

        task1.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    event = documentSnapshot.toObject(EventModel.class);
                    update();
                    checkUserInEvent();
                }
            }
        });

        Tasks.whenAllComplete(task1);

        // getting event information from Firestore
        final DocumentReference eventRef = db.collection("events").document(event.getEventID());
        eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("EventEntrantActivity", error.toString());
                }
                if (value != null) {
                    event = value.toObject(EventModel.class);
                    update();
                    checkUserInEvent();
                }
            }
        });

        // ****************************************************************************************
        // ****************************************************************************************
        Task<DocumentSnapshot> task = db.collection("users").document(userList.getiD()).get();
        task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(UserModel.class);
            }
        });
        // ****************************************************************************************
        // ****************************************************************************************

        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        unjoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Unjoining
                try {
                    event.unqueueWaitingList(userList);
                    event.deregisterUserID(userList);
                    db.collection("events").document(event.getEventID()).set(event);
                }
                catch (Exception e) {
                    Toast.makeText(EventEntrantActivity.this, "This user is not in the waiting list!", Toast.LENGTH_SHORT).show();
                }
                // ****************************************************************************************
                UnsubscribeFromTopic unsubscribeFromTopic = new UnsubscribeFromTopic(event.getEventID() + "_" + userList.getiD(),getApplicationContext());
                unsubscribeFromTopic.unsubscribe();
                currentUser.removeTopics(event.getEventID() + "_" + userList.getiD());
                db.collection("users").document(userList.getiD()).set(currentUser);
                // ****************************************************************************************
                unjoinBtn.setVisibility(View.GONE);
                joinBtn.setVisibility(View.VISIBLE);
            }
        });
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askNotificationPermission();
                if (event.getGeolocationRequired()) {
                    // Joining
                    new geo_requirement_dialog(userList, event, db, joinBtn, unjoinBtn).show(getSupportFragmentManager(), "geo_requirement_dialog");
                    // Getting event specific topic
                }
                else {
                    try {
                        event.queueWaitingList(userList);
                        event.registerUserID(userList);
                        db.collection("events").document(event.getEventID()).set(event);
                        joinBtn.setVisibility(View.GONE);
                        unjoinBtn.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText(EventEntrantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    // ****************************************************************************************
                    // Subscribing to topic when joining event to receive notification
                    SubscribeToTopic subscribeToTopic = new SubscribeToTopic(event.getEventID() + "_" + userList.getiD(),getApplicationContext());
                    subscribeToTopic.subscribe();
                    currentUser.addTopics(event.getEventID() + "_" + userList.getiD());
                    // ****************************************************************************************
                    db.collection("users").document(userList.getiD()).set(currentUser);
                    // ****************************************************************************************
                    joinBtn.setVisibility(View.GONE);
                    unjoinBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // ********************************************************************
        decode();
        // ********************************************************************
    }

    public void decode(){
        DocumentReference docref = db.collection("posters").document(event.getEventID());
        docref.get().addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Document exists
                    Blob blob = document.getBlob("Blob");
                    byte[] bytes = blob.toBytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    eventPoster.setImageBitmap(bitmap);
                }
                else{
//                    eventPoster.setImageDrawable(getResources().getDrawable(R.drawable.defaultposter));
                    eventPoster.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.defaultposter));


                }
            }

        });
        DocumentReference organizerPicRef = db.collection("photos").document(event.getFacilityID()); // FacilityId == UserID
        organizerPicRef.get().addOnCompleteListener( task1 -> {
            if(task1.isSuccessful()) {
                DocumentSnapshot document1 = task1.getResult();
                if (document1.exists()) {
                    // user has a profile picture (either uploaded or auto-generated)
                    Blob blob1 = document1.getBlob("Blob");
                    byte[] bytes1 = blob1.toBytes();
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
                    organizerProfilePicture.setImageBitmap(bitmap1);
                }
                else {
                    char initial;
                    Log.e("Initial",""+event.getOrganizer().isEmpty());
                    Log.e("Orgnanizer", event.getOrganizer());
                    if (!event.getOrganizer().isEmpty()) {
                        int index = event.getOrganizer().indexOf(event.getOrganizer().trim());
                        initial = event.getOrganizer().charAt(index);
                        String finalInital = String.valueOf(initial).toUpperCase();
                        profile_letter = findViewById(R.id.profileLetter);
                        autoGeneratedProfilePicture(finalInital);
                    } else {
//                        organizerProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.defaultprofilepicture));
                        organizerProfilePicture.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.defaultprofilepicture));


                    }

                }
            }

        });
    }

    /**
     * Updating the view
     */
    private void update(){
        eventTitle.setText(event.getEventTitle());
        eventLocation.setText(event.getEventStrLocation());
        Date javaDate = event.getJoinDeadline();
        eventDate.setText(javaDate.toString());
        organizer = event.getOrganizer();
        organizerName.setText(organizer);
        eventDescription.setText(event.getEventDescription());
    }

    /**
     * Checking if the user is in the event
     */
    private void checkUserInEvent(){
        if (event.checkUserInList(userList, event.getWaitingList())) {
            unjoinBtn.setVisibility(View.VISIBLE);
            joinBtn.setVisibility(View.GONE);
        } else {
            unjoinBtn.setVisibility(View.GONE);
            joinBtn.setVisibility(View.VISIBLE);
        }
    }
public void autoGeneratedProfilePicture(String finalInitial){
    profile_letter.setVisibility(View.VISIBLE);
    profile_letter.setText(finalInitial);
    Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    // Get the TextView's text and draw it onto the canvas
    Paint paint = profile_letter.getPaint();

    Log.e("Context","Context is not null");
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
    organizerProfilePicture.setImageBitmap(bitmap);

//        byte[] bytes = stream.toByteArray();
    profile_letter.setVisibility(View.GONE);

//        Blob blob = Blob.fromBytes(bytes);
//
//        HashMap<String, Object> hashMap = new HashMap<String, Object>();
//        hashMap.put("Blob",blob);
//        hashMap.put("personal",false);
//        hashMap.put("Initial",finalInitial);
//
//        db.collection("photos").document(curUser.getiD()).set(hashMap);
//        decode();
}

    public EventModel getEvent() {
        return event;
    }
}