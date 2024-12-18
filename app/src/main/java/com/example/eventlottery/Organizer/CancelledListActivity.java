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
 * Cancelled List Activity
 * This activity displays a list of users who have been cancelled by the organizer of the event.
 */
public class CancelledListActivity extends AppCompatActivity {

    private ArrayList<RemoteUserRef> userCancelList;
    private ListView cancelledList;
    private ArrayAdapter<RemoteUserRef> cancelAdapter;
    private EventModel event;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button notification_send;
    private TextView cancelledConfirmed; // TextView for displaying the number of cancelled entrants
    private FloatingActionButton backFAB;


    /**
     * Called when the activity is first created.
     * It creates the activity view so the user can see it and interact with it
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_cancelled_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userCancelList = new ArrayList<>();

        Bundle e = getIntent().getExtras();
        if (e != null) {
            event = (EventModel) e.getSerializable("eventModel");
        }

        userCancelList = event.getCancelledList();

        cancelledList = findViewById(R.id.cancelled_list);
        cancelAdapter = new CancelledListArrayAdapter(this, 0, userCancelList, "cancelled", event, db);
        cancelledList.setAdapter(cancelAdapter);
        cancelAdapter.notifyDataSetChanged();

        cancelledConfirmed = findViewById(R.id.cancelled_confirmed); // Initialize the TextView

        backFAB = findViewById(R.id.back);
        notification_send = findViewById(R.id.cancelled_notif_button);

        notification_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendNotificationDialog(event, "Cancelled", false, db).show(getSupportFragmentManager(), "Send Notification");
            }
        });

        // Updates all the event's lists and the number of cancelled
        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("CancelledListActivity", e.toString());
                        }
                        if (doc != null && doc.exists()) {
                            // Get the EventModel object
                            EventModel FireStoreEvent = doc.toObject(EventModel.class);

                            // Update Cancelled List
                            event.getCancelledList().clear();
                            event.getCancelledList().addAll(FireStoreEvent.getCancelledList());
                            // Update the cancelled count
                            cancelledConfirmed.setText(String.valueOf(event.getCancelledList().size()));

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
                            cancelAdapter.notifyDataSetChanged();
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
