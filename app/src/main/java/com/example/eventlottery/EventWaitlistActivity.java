package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        userWaitList = new ArrayList<>(event.getWaitingList());
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


//        // Add back button functionality
//        Button backButton = findViewById(R.id.back_button);
//        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(EventWaitlistActivity.this, EventOrganizerActivity.class);
//            startActivity(intent);
//            finish();
//        });


        drawSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Draw random sample based on event capacity
                ArrayList<UsersList> chosenEntrants = drawRandomSample(userWaitList, event.getCapacity());

                // Pass chosen entrants to ChosenListActivity
                Intent intent = new Intent(EventWaitlistActivity.this, ChosenListActivity.class);
                intent.putExtra("chosenEntrants", chosenEntrants);
                intent.putExtra("eventModel", event);
                startActivity(intent);
            }
        });
    }

    /**
     * Draws a random sample from the waitlist based on the event's capacity.
     * If the waitlist is smaller than the capacity, returns the entire waitlist.
     *
     * @param waitlist The list of users on the waitlist.
     * @param capacity The maximum number of entrants to select.
     * @return A randomly selected subset of the waitlist.
     */
    private ArrayList<UsersList> drawRandomSample(ArrayList<UsersList> waitlist, int capacity) {
        if (waitlist.size() <= capacity) {
            return new ArrayList<>(waitlist); // Return full list if within capacity
        }
        Collections.shuffle(waitlist, new Random()); // Shuffle for randomness
        return new ArrayList<>(waitlist.subList(0, capacity)); // Return subset
    }
}
