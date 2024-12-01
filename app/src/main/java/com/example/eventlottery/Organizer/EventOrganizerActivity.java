package com.example.eventlottery.Organizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventlottery.Entrant.qr_code_dialog;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;

/**
 * Event Organizer Activity
 * The purpose of this class is to give the ability to the organizer to manage it event
 * this class contain the information of the event, the Entrant that join the event and the differences
 * list for each stage the entrant is in the event selection
 * All the poster and images are for part 4
 */
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
    private FacilityModel facility;
    private LinearLayout progessBar;
    EventOrganizerActivity currentActivity = this;
    private String hashQR;
    private FloatingActionButton backFAB;

    /**
     * On create override
     * This method create the view so the organizer can interact and manage they event
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
        backFAB = findViewById(R.id.back);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            eventID = extra.getString("event_id");
            hashQR = extra.getString("hashQR");
            event = (EventModel) extra.getSerializable("eventModel");
            facility = (FacilityModel) extra.getSerializable("facilityModel");
        }
        updateViews();
        setupSnapshotListeners(); // Setting up snapshot listeners
        progessBar.setVisibility(View.GONE);
        eventView.setVisibility(View.VISIBLE);
        QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new qr_code_dialog(hashQR).show(getSupportFragmentManager(), "qr_code_dialog");
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

        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentDate = new Date();
                if (currentDate.before(event.getJoinDeadline())) {
                    new CreateEventDialogueFragment(event, facility, db, currentActivity).show(getSupportFragmentManager(), "EditEventDialogueFragment");
                }
                else {
                    Toast.makeText(currentActivity, "Cannot edit event after the join deadline", Toast.LENGTH_SHORT).show();
                }
            }
        });
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        decode();


    }

    /**
     * Updates the views
     * If the event was modify in someway, this method, change the information so the view has the
     * update information of the event in actual real time
     */
    public void updateViews() {
        Log.d("TESTING", "Views updated");
        eventTitle.setText(event.getEventTitle());
        eventCapacity.setText(event.getCapacity().toString());

        // Update waitlist limit display
        if (event.getWaitingListLimit() == -1) {
            waitlistLimit.setText("No Limit");
        } else {
            waitlistLimit.setText(event.getWaitingListLimit().toString());
        }

        if (event.getGeolocationRequired().equals(Boolean.TRUE)) {
            geolocationRequired.setText("Yes");
        } else {
            geolocationRequired.setText("No");
        }
        CharSequence timeFormat = DateFormat.format("MMMM d, yyyy ", event.getJoinDeadline().getTime());
        eventDate.setText(timeFormat);
        eventDescription.setText(event.getEventDescription());

        // Update button text with counters
        invited.setText("Invited: " + event.getInvitedList().size());
        cancelled.setText("Cancelled: " + event.getCancelledList().size());
        waitlist.setText("Waitlist: " + event.getWaitingList().size());
        enrolled.setText("Enrolled: " + event.getEnrolledList().size());

        decode();
    }


    /**
     * Sets up Firestore snapshot listeners to observe real-time updates
     */
    private void setupSnapshotListeners() {
        DocumentReference docRef = db.collection("events").document(eventID);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("EventOrganizerActivity", "Snapshot listener failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    event = snapshot.toObject(EventModel.class);

                    if (event != null) {
                        // Update the button text dynamically
                        invited.setText("Invited: " + event.getInvitedList().size());
                        cancelled.setText("Cancelled: " + event.getCancelledList().size());
                        waitlist.setText("Waitlist: " + event.getWaitingList().size());
                        enrolled.setText("Enrolled: " + event.getEnrolledList().size());
                    }
                }
            }
        });
    }

    /**
     * the code the image to make it available as the poster of the event
     */
    public void decode() {
        DocumentReference docref = db.collection("posters").document(eventID);
        docref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Blob blob = document.getBlob("Blob");
                    byte[] bytes = blob.toBytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    eventPoster.setImageBitmap(bitmap);

                } else {
                    eventPoster.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.defaultposter));
                }
            }
        });
    }
}
