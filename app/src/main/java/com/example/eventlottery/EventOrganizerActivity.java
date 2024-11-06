package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
    private Button invited;
    private Button cancelled;
    private Button waitlist;
    private Button enrolled;
    private LinearLayout eventView;

    private String eventID;
    private EventModel event;
    private CurrentUser curUser;  // Ensure this is initialized correctly
    private ConstraintLayout progessBar;

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
//        back = findViewById(R.id.back_button);
        eventTitle = findViewById(R.id.event_organizer_event_title);
        eventDate = findViewById(R.id.event_organizer_event_date);
        eventDescription = findViewById(R.id.event_organizer_event_description);
        eventPoster = findViewById(R.id.event_organizer_event_poster);
//        organizerName = findViewById(R.id.event_organizer_organizer_and_facility_names);
        QRCode = findViewById(R.id.event_organizer_QR_code_view_button);
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
            curUser = (CurrentUser) extra.getSerializable("currentUser");
        }

        eventDescription.setText(event.getEventDescription());
        eventTitle.setText(event.getEventTitle());
        eventDate.setText(event.getJoinDeadline().toString());
//        organizerName.setText(event.getOrganizer());
        progessBar.setVisibility(View.GONE);
        eventView.setVisibility(View.VISIBLE);

        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new qr_code_dialog(eventID).show(getSupportFragmentManager(), "qr_code_dialog");
            }
        });

        waitlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventOrganizerActivity.this, EventWaitlistActivity.class);
                i.putExtra("eventModel", event);
                startActivity(i);
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

        editEvent.setOnClickListener(v -> {
            CreateEventDialogueFragment editDialog = new CreateEventDialogueFragment(
                    event.getEventID(),
                    event.getEventTitle(),
                    event.getCapacity(),
                    event.getWaitingListLimit(),
                    event.getJoinDeadline(),
                    event.getEventStrLocation(),
                    event.getGeolocationRequired(),
                    event.getEventDescription(),
                    curUser,
                    db
            );
            editDialog.show(getSupportFragmentManager(), "edit_event");
        });

        // Setup snapshot listeners for counters
        setupSnapshotListeners();
    }

    private void setupSnapshotListeners() {
        db.collection("events").document(eventID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.w("EventOrganizerActivity", "Listen failed.", e);
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Update Waitlist Count
                        ArrayList<UsersList> waitlistArray = (ArrayList<UsersList>) documentSnapshot.get("waitingList");
                        waitlist.setText("Waitlist: " + (waitlistArray != null ? waitlistArray.size() : 0));

                        // Update Invited Count
                        ArrayList<UsersList> invitedArray = (ArrayList<UsersList>) documentSnapshot.get("invitedList");
                        invited.setText("Invited: " + (invitedArray != null ? invitedArray.size() : 0));

                        // Update Cancelled Count
                        ArrayList<UsersList> cancelledArray = (ArrayList<UsersList>) documentSnapshot.get("cancelledList");
                        cancelled.setText("Cancelled: " + (cancelledArray != null ? cancelledArray.size() : 0));

                        // Update Enrolled Count
                        ArrayList<UsersList> enrolledArray = (ArrayList<UsersList>) documentSnapshot.get("enrolledList");
                        enrolled.setText("Enrolled: " + (enrolledArray != null ? enrolledArray.size() : 0));
                    } else {
                        Log.d("EventOrganizerActivity", "Current data: null");
                    }
                });
    }
}
