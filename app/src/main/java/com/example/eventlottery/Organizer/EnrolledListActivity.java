package com.example.eventlottery.Organizer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Notifications.SendNotificationDialog;
import com.example.eventlottery.R;
import com.example.eventlottery.Models.RemoteUserRef;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * Enrolled List Activity
 * This class purpose is to show the entrants that are enroll to the event
 * it display the enroll entrants in a listview where the organizer can send a notification
 * so they know they are enrolled
 */
public class EnrolledListActivity extends AppCompatActivity {

    private ArrayList<RemoteUserRef> userEnrollList;
    private ListView enrollList;
    private ArrayAdapter<RemoteUserRef> enrollAdapter;
    private EventModel event;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button notif_enrolled;
    private TextView enrollConfirmed; // TextView for displaying the number of enrolled entrants
    private FloatingActionButton backFAB;

    /**
     * Overriding on create
     * it creates the view where the organizer can see the listview and interact with the activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_enroll_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userEnrollList = new ArrayList<>();

        Bundle e = getIntent().getExtras();
        if (e != null) {
            event = (EventModel) e.getSerializable("eventModel");
        }

        userEnrollList = event.getEnrolledList();
        backFAB = findViewById(R.id.back);
        enrollList = findViewById(R.id.enroll_list);
        enrollAdapter = new EnrolledListArrayAdapter(this, 0, userEnrollList, "enrolled", event, db);
        enrollList.setAdapter(enrollAdapter);

        notif_enrolled = findViewById(R.id.enroll_notif_button);
        enrollConfirmed = findViewById(R.id.enroll_confirmed); // Initialize the TextView

        notif_enrolled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendNotificationDialog(event, "Enrolled", false, db).show(getSupportFragmentManager(), "Send Notification");
            }
        });

        // Updates all the event's lists and the number of enrolled entrants
        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("EnrolledListActivity", e.toString());
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
                            enrollConfirmed.setText(String.valueOf(event.getEnrolledList().size())); // Update count
                            // Update Invited List
                            event.getInvitedList().clear();
                            event.getInvitedList().addAll(FireStoreEvent.getInvitedList());
                            // Notify adapter of changes
                            enrollAdapter.notifyDataSetChanged();
                        }
                    }
                });
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
