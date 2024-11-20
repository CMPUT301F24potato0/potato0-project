package com.example.eventlottery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Event Waitlist Activity
 */
public class EventWaitlistActivity extends AppCompatActivity {

    private Button notify;
    private ListView waitlist;
    private EventModel event;
    private WaitlistEventAdapter adapter;
    private Button drawSample;
    private EditText drawSampleEditText;
    private int remaining_spots;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // UI elements to display updated values
    private TextView geolocationRequiredTextView;
    private TextView eventCapacityTextView;
    private TextView waitlistLimitTextView;
    private TextView waitlistedEntrantsTextView;
    private TextView freeSlotsTextView;

    /**
     * On create Override
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_waitlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the event data
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            event = (EventModel) extra.getSerializable("eventModel");
        }

        // Initialize UI elements
        notify = findViewById(R.id.notify_btn_id);
        waitlist = findViewById(R.id.waitList_listview);
        drawSample = findViewById(R.id.draw_sample_button);
        drawSampleEditText = findViewById(R.id.draw_sample_edittext);

        // Initialize TextViews for dynamic data
        geolocationRequiredTextView = findViewById(R.id.event_waitlist_geolocation_required);
        eventCapacityTextView = findViewById(R.id.event_waitlist_event_capacity);
        waitlistLimitTextView = findViewById(R.id.event_waitlist_limit);
        waitlistedEntrantsTextView = findViewById(R.id.waitlisted_entrants);
        freeSlotsTextView = findViewById(R.id.free_slots);

        // Set initial values
        updateUI();

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendNotificationDialog(event, "Waitlist", false, db).show(getSupportFragmentManager(), "Send Notification");
            }
        });

        adapter = new WaitlistEventAdapter(this, 100, event, db);
        waitlist.setAdapter(adapter);

        // calculate remaining spots for event and update edittext
        remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
        drawSampleEditText.setText(Integer.toString(remaining_spots));

        drawSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update remaining spots in case the organizer returns to this page and clicks draw sample button again
                remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
                // Check for empty input
                if (drawSampleEditText.getText().toString().equals("")) {
                    Toast.makeText(EventWaitlistActivity.this, "Please enter a number.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer sample_amount = Integer.parseInt(drawSampleEditText.getText().toString());
                    // Check if the waiting list has enough people to sample
                    if (remaining_spots < sample_amount) {
                        Toast.makeText(EventWaitlistActivity.this, "Your event only has " + remaining_spots + " remaining spots left!", Toast.LENGTH_SHORT).show();
                    }
                    // Check if organizer is trying to sample more than the remaining amount of spots available for the event
                    else if (event.getWaitingList().size() < sample_amount) {
                        Toast.makeText(EventWaitlistActivity.this, "Waiting list only has " + event.getWaitingList().size() + " entrants left!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(EventWaitlistActivity.this, ChosenListActivity.class);
                        intent.putExtra("sample_amount", sample_amount);
                        intent.putExtra("remaining_spots", remaining_spots);
                        intent.putExtra("eventModel", event);
                        startActivity(intent);
                    }
                }
            }
        });

        // Updates all the event's lists
        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("EventWaitlistActivity", e.toString());
                        }
                        if (doc != null && doc.exists()) {
                            // Get the EventModel object
                            EventModel FireStoreEvent = doc.toObject(EventModel.class);

                            // Update Waiting List
                            event.getWaitingList().clear();
                            event.getWaitingList().addAll(FireStoreEvent.getWaitingList());
                            // Update Cancelled List
                            event.getCancelledList().clear();
                            event.getCancelledList().addAll(FireStoreEvent.getCancelledList());
                            // Update Chosen List
                            event.getChosenList().clear();
                            event.getChosenList().addAll(FireStoreEvent.getChosenList());
                            // Update Enrolled List
                            event.getEnrolledList().clear();
                            event.getEnrolledList().addAll(FireStoreEvent.getEnrolledList());
                            // Update Invited List
                            event.getInvitedList().clear();
                            event.getInvitedList().addAll(FireStoreEvent.getInvitedList());

                            // Notify adapter of changes
                            adapter.notifyDataSetChanged();

                            // Update UI elements with new values
                            updateUI();
                        }
                    }
                });
    }

    /**
     * Updates the dynamic UI elements based on the latest event data
     */
    private void updateUI() {
        geolocationRequiredTextView.setText(event.getGeolocationRequired() ? "Yes" : "No");
        eventCapacityTextView.setText(String.valueOf(event.getCapacity()));
        waitlistLimitTextView.setText(event.getWaitingListLimit() == -1 ? "No Limit" : String.valueOf(event.getWaitingListLimit()));
        waitlistedEntrantsTextView.setText(String.valueOf(event.getWaitingList().size()));

        // Calculate and display free slots based on event capacity and enrolled entrants
        int freeSlots = event.getCapacity() - event.getEnrolledList().size();
        freeSlotsTextView.setText("Free Slots: " + freeSlots);

        // Ensure freeSlotsTextView is visible
        freeSlotsTextView.setVisibility(View.VISIBLE);
    }
}
