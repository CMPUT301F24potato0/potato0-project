package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private Button remove;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<UsersList> userWaitList;
    private Button drawSample;
    private EditText drawSampleEditText;

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

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            event = (EventModel) extra.getSerializable("eventModel");
        }

        notify = findViewById(R.id.notify_btn_id);
        waitlist = findViewById(R.id.waitList_listview);
        drawSample = findViewById(R.id.draw_sample_button);
        drawSampleEditText = findViewById(R.id.draw_sample_edittext);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<UsersList> userIDs = event.getWaitingList();
                String eventID = event.getEventID();
                for (int i = 0; i < userIDs.size(); i++) {
                    String topic = eventID + "_" + userIDs.get(i);
                    SendNotification sendNotification = new SendNotification(getApplicationContext(), topic);
                    sendNotification.popup();
                }
            }
        });

        userWaitList = new ArrayList<>(event.getWaitingList());
        adapter = new WaitlistEventAdapter(this, 100, userWaitList, event, db);
        waitlist.setAdapter(adapter);

        // When user unjoins the event, it is now being shown in this
        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("EventWaitlistActivity", e.toString());
                        }
                        if (doc != null && doc.exists()) {
                            userWaitList.clear();
                            ArrayList<UsersList> userWaitListTemp = (ArrayList<UsersList>) doc.toObject(EventModel.class).getWaitingList();
                            for (UsersList u : userWaitListTemp) {
                                userWaitList.add(u);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        // calculate remaining spots for event and update edittext
        int remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
        drawSampleEditText.setText(Integer.toString(remaining_spots));

        drawSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer sample_amount = Integer.parseInt(drawSampleEditText.getText().toString());
                // Check if the waiting list has enough people to sample
                if (event.getWaitingList().size() < sample_amount) {
                    Toast.makeText(EventWaitlistActivity.this, "Waiting list only has " + event.getWaitingList().size() + " entrants left!", Toast.LENGTH_SHORT).show();
                }
                // Check if organizer is trying to sample more than the remaining amount of spots available for the event
                if (remaining_spots < sample_amount) {
                    Toast.makeText(EventWaitlistActivity.this, "Cannot sample more than " + Integer.toString(remaining_spots) + " remaining spots.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(EventWaitlistActivity.this, ChosenListActivity.class);
                    intent.putExtra("sample_amount", sample_amount);
                    intent.putExtra("remaining_spots", remaining_spots);
                    intent.putExtra("eventModel", event);
                    startActivity(intent);
                }
            }
        });
    }
}
