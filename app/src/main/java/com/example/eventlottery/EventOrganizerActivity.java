package com.example.eventlottery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventOrganizerActivity extends AppCompatActivity {
    /*
        Getting the views from the layout file
     */

    private FirebaseFirestore db;

    private Button editEvent;
    private FloatingActionButton back;
    private TextView eventTitle;
    private TextView eventDate;
    private TextView eventDescription;
    private ImageView eventPoster;
    private TextView organizerName;
    private Button QRCode;
    private TextView geolocationRequired;
    private TextView eventCapacity;
    private TextView waitlistLimit;
    private Button invited;
    private Button cancelled;
    private Button waitlist;
    private Button enrolled;
    private LinearLayout eventView;

    private String eventID;
    private EventModel event;
    private ConstraintLayout progessBar;
    EventOrganizerActivity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_organizer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
            Initializing the views
         */
        editEvent = findViewById(R.id.event_organizer_edit_event_button);
//        back = findViewById(R.id.event_organizer_fab_button);
        eventTitle = findViewById(R.id.event_organizer_event_title);
        eventDate = findViewById(R.id.event_organizer_event_date);
        eventDescription = findViewById(R.id.event_organizer_event_description);
        eventPoster = findViewById(R.id.event_organizer_event_poster);
//        organizerName = findViewById(R.id.event_organizer_organizer_and_facility_names);
        QRCode = findViewById(R.id.event_organizer_QR_code_view_button);
        geolocationRequired = findViewById(R.id.event_organizer_geolocation_required);
        eventCapacity = findViewById(R.id.event_organizer_event_capacity);
        waitlistLimit = findViewById(R.id.event_organizer_waiting_list_limit);
        invited = findViewById(R.id.event_organizer_invited_button);
        cancelled = findViewById(R.id.event_organizer_cancelled_button);
        waitlist = findViewById(R.id.event_organizer_waitlist_button);
        enrolled = findViewById(R.id.event_organizer_enrolled_button);
        eventView = findViewById(R.id.event_view);
        progessBar = findViewById(R.id.progressBar2);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            eventID = extra.getString("event_id");
            event = (EventModel) extra.getSerializable("eventModel");
        }
        updateViews();
//        eventDescription.setText(event.getEventDescription());
//        eventTitle.setText(event.getEventTitle());
//        eventDate.setText(event.getJoinDeadline().toString());
////        eventPoster.setImageResource(R.drawable.ic_facility_background);
//        organizerName.setText(event.getOrganizer());
        progessBar.setVisibility(View.GONE);
        eventView.setVisibility(View.VISIBLE);
//        db.collection("events").document(eventID)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                event = document.toObject(EventModel.class);
//                                eventTitle.setText(event.getEventTitle());
//                                eventDescription.setText(event.getEventDescription());
////                                eventPoster.setImageResource(R.drawable.ic_facility_background);
////                                organizerName.setText(document.getString("organizer"));
//                                progessBar.setVisibility(View.GONE);
//                                eventView.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        else {
//                            Log.d("Firebase error", "Cached get failed: ", task.getException());
//                        }
//                    }
//                });


        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new qr_code_dialog(eventID).show(getSupportFragmentManager(), "qr_code_dialog");
            }
        });

        // Adapted from https://stackoverflow.com/questions/71082372/startactivityforresult-is-deprecated-im-trying-to-update-my-code
        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            event = (EventModel) result.getData().getSerializableExtra("eventModel");
                        }
                    }
                }
        );

        waitlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventOrganizerActivity.this, EventWaitlistActivity.class);
                i.putExtra("eventModel", event);
                startActivityIntent.launch(i);
//                event_waitlist eventWaitlist = new event_waitlist();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.main,eventWaitlist);
//                transaction.commit();
            }
        });

        cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventOrganizerActivity.this, CancelledListActivity.class);
                i.putExtra("eventModel", event);
                startActivity(i);

            }
        });

        invited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventOrganizerActivity.this, InvitedListActivity.class);
                i.putExtra("eventModel", event);
                startActivity(i);
            }
        });

        enrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventOrganizerActivity.this, EnrolledListActivity.class);
                i.putExtra("eventModel", event);
                startActivity(i);
            }
        });

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateEventDialogueFragment(event, db, currentActivity).show(getSupportFragmentManager(), "EditEventDialogueFragment");
            }
        });
    }

    public void updateViews() {
        Log.d("TESTING", "Views updated");
        eventTitle.setText(event.getEventTitle());
        eventCapacity.setText(event.getCapacity().toString());
        waitlistLimit.setText(event.getWaitingListLimit().toString());
        if (event.getGeolocationRequired().equals(Boolean.TRUE)) {
            geolocationRequired.setText("Yes");
        }
        else {
            geolocationRequired.setText("No");
        }
        eventDate.setText(event.getJoinDeadline().toString());
        eventDescription.setText(event.getEventDescription());
//        organizerName.setText(event.getOrganizer());
    }
}