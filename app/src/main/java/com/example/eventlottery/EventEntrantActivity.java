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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.Date;
import java.util.List;

public class EventEntrantActivity extends AppCompatActivity {
    FloatingActionButton back;
    FirebaseFirestore db;

    private TextView organizerName;
    private DocumentReference facilityRef;
    private String organizer;
    // Event stuff and user stuff
    private UsersList userList;
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EventEntrantActivity.this, MainActivity.class);
        startActivity(i);
    }

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
            userList = (UsersList) extras.getSerializable("userList");
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

        // getting event information from Firestore
        final DocumentReference eventRef = db.collection("events").document(event.getEventID());


        eventTitle.setText(event.getEventTitle());
        eventLocation.setText(event.getEventStrLocation());
        Date javaDate = event.getJoinDeadline();
        eventDate.setText(javaDate.toString());
        organizer = event.getOrganizer();
        organizerName.setText(organizer);
        eventDescription.setText(event.getEventDescription());
        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        if (event.checkUserInList(userList, event.getWaitingList())) {
            unjoinBtn.setVisibility(View.VISIBLE);
            joinBtn.setVisibility(View.GONE);
            unjoinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.unqueueWaitingList(userList);
                    db.collection("events").document(event.getEventID()).set(event);
                    String topic = eventRef + "_waitlist";
                    UnsubscribeFromTopic unsubscribeFromTopic = new UnsubscribeFromTopic(topic,getApplicationContext());
                    unsubscribeFromTopic.unsubscribe();


                }
            });
        } else {
            unjoinBtn.setVisibility(View.GONE);
            joinBtn.setVisibility(View.VISIBLE);
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.getGeolocationRequired()) {
                        new geo_requirement_dialog(userList, event, db).show(getSupportFragmentManager(), "geo_requirement_dialog");
                        // eventRef = eventID
                        String topic = eventRef + "_waitlist";
                        SubscribeToTopic subscribeToTopic = new SubscribeToTopic(topic,getApplicationContext());
                        subscribeToTopic.subscribe();

                    }
                    else {
                        try {
                            event.queueWaitingList(userList);
                            String topic = eventRef + "_waitlist";
                            SubscribeToTopic subscribeToTopic = new SubscribeToTopic(topic,getApplicationContext());
                            subscribeToTopic.subscribe();

                        } catch (Exception e) {
                            Toast.makeText(EventEntrantActivity.this, "Waitlist is full", Toast.LENGTH_SHORT).show();
                        }
                        db.collection("events").document(event.getEventID()).set(event);
                    }
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventEntrantActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}