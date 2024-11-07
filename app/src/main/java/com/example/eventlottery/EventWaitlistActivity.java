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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
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
    private Button remove;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<UsersList> userWaitList;
    private ArrayList<UsersList> cancelList;

    private Button drawSample;
    private EditText drawSampleEditText;
    private int remaining_spots;
    private String title;
    private String body;

    private SendNotification sendNotification;

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

                //Need to get arrayList of all UserID's

//                ArrayList<UsersList> userIDs = event.getWaitingList();
//                String eventID = event.getEventID();


                Log.e("Array1","");
                Log.e("Array2","");
                Log.e("Array3","");
                Log.e("Array4","");


                // List of user ID's
                ArrayList<UsersList> usersLists = event.getWaitingList();

                // Event ID
                String eventId = event.getEventID();



                Intent intent1 = new Intent(EventWaitlistActivity.this,SendNotificationActivity.class);
                intent1.putExtra("event", event);
                intent1.putExtra("Bool",0);
                startActivity(intent1);


            }
        });

        userWaitList = event.getWaitingList();
        cancelList = event.getCancelledList();
        adapter = new WaitlistEventAdapter(this, 100, userWaitList, cancelList, event, db);
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
                            ArrayList<UsersList> temp = (ArrayList<UsersList>) userWaitList.clone();
                            userWaitList.clear();
                            ArrayList<UsersList> userWaitListTemp = (ArrayList<UsersList>) doc.toObject(EventModel.class).getWaitingList();
                            for (UsersList u : userWaitListTemp) {
                                userWaitList.add(u);
                                temp.remove(u);
                            }

//                            for (UsersList u : temp) {
//                                event.getCancelledList().add(u);
//                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        // calculate remaining spots for event and update edittext
        remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
        drawSampleEditText.setText(Integer.toString(remaining_spots));

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
                    // Check for 0 sample_amount
//                    if (sample_amount == 0) {
//                        Toast.makeText(EventWaitlistActivity.this, "Cannot sample 0 entrants.", Toast.LENGTH_SHORT).show();
//                    }
                    // Check if the waiting list has enough people to sample
                    if (remaining_spots < sample_amount) {
                        Toast.makeText(EventWaitlistActivity.this, "Your event only has " + remaining_spots + " remaining spots left!", Toast.LENGTH_SHORT).show();
                    }
                    else if (event.getWaitingList().size() < sample_amount) {
                        Toast.makeText(EventWaitlistActivity.this, "Waiting list only has " + event.getWaitingList().size() + " entrants left!", Toast.LENGTH_SHORT).show();
                    }
                    // Check if organizer is trying to sample more than the remaining amount of spots available for the event
                    else {
                        Intent intent = new Intent(EventWaitlistActivity.this, ChosenListActivity.class);
                        intent.putExtra("sample_amount", sample_amount);
                        intent.putExtra("remaining_spots", remaining_spots);
                        intent.putExtra("eventModel", event);
                        startActivityIntent.launch(intent);
                    }
                }
            }
        });
    }


    /**
     * Returns the EventModel object in an intent after modifications have been done to it
     */
    @Override
    public void finish() {
        // Adapted from https://stackoverflow.com/questions/22549294/getting-intent-result-from-ondestroy
        Intent returnIntent = new Intent();
        returnIntent.putExtra("eventModel", event);
        setResult(Activity.RESULT_OK, returnIntent);
        super.finish();
    }
}
