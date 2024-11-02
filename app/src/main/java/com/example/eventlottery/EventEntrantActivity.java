package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firestore.v1.WriteResult;

import java.util.Date;
import java.util.List;

public class EventEntrantActivity extends AppCompatActivity {
    FloatingActionButton back;
    FirebaseFirestore db;

    private TextView organizerName;
    private DocumentReference facilityRef;
    private String organizer;
    // Event stuff and user stuff
    private String eventID;
    private String userID;
    // XML stuff
    private TextView eventDescription;
    private ImageView eventPoster;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventDate;
    private Button joinUnjoinButton;
    private ImageView organizerProfilePicture;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
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
            eventID = extras.getString("event_id");
            userID = extras.getString("user_id");
        }

        db = FirebaseFirestore.getInstance();

        eventPoster = findViewById(R.id.event_entrant_page_event_poster1);
        eventTitle = findViewById(R.id.event_entrant_page_event_title1);
        eventLocation = findViewById(R.id.event_entrant_page_event_location1);
        eventDate = findViewById(R.id.event_entrant_page_event_date1);
        joinUnjoinButton = findViewById(R.id.event_entrant_page_join_unjoin_button1);
        organizerProfilePicture = findViewById(R.id.event_entrant_page_profile_picture1);
        organizerName = findViewById(R.id.event_entrant_page_organizer_name1);
        eventDescription = findViewById(R.id.event_entrant_page_event_details1);

        progressBar = findViewById(R.id.progressBar1);
        linearLayout = findViewById(R.id.linearLayout1);

        back = findViewById(R.id.floatingActionButton);




        // getting event information from Firestore
        final DocumentReference eventRef = db.collection("events").document(eventID);

        // Joining waiting list onClickListener
        View.OnClickListener join_list = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User exists
                // add user to the wait list
                eventRef.update("waiting_list", FieldValue.arrayUnion(userID));

            }
        };

        // Unjoin waiting list onClickListener
        View.OnClickListener unjoin_list = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User exists
                // remove user from the wait list
                eventRef.update("waiting_list", FieldValue.arrayRemove(userID));
            }
        };

        eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Firebase Error", "Listen failed.", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d("Firebase Data", "Current data: " + snapshot.getData());
                    eventTitle.setText(snapshot.getString("title"));
                    eventLocation.setText(snapshot.getString("location_string"));
                    Date javaDate = snapshot.getTimestamp("join_deadline").toDate();
                    eventDate.setText(javaDate.toString());
                    organizer = snapshot.getString("organizer");
                    organizerName.setText(organizer);
                    eventDescription.setText(snapshot.getString("description"));
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    if (((List<String>) snapshot.get("waiting_list")).contains(userID)) {
                        joinUnjoinButton.setText("Unjoin");
                        joinUnjoinButton.setOnClickListener(unjoin_list);
                    } else {
                        joinUnjoinButton.setText("Join");
                        joinUnjoinButton.setOnClickListener(join_list);
                    }
                } else {
                    Log.d("Firebase data", "Current data: null");
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventEntrantActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        // TODO: Implement join button
        // TODO: add functionality to unjoin button
        // TODO: 1) check if user already joined
        // TODO: 2) if joined, change text in button to "Unjoin"
        // TODO: 3) add functionality to unjoin the event
    }
}