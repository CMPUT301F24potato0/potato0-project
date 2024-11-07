package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

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
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

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

                ArrayList<UsersList> userIDs = event.getWaitingList();
                String eventID = event.getEventID();
                for(int i = 0; i < userIDs.size(); i++){
                    String topic = eventID + "_" + userIDs.get(i);
                    SendNotification sendNotification = new SendNotification(getApplicationContext(), topic);
                    sendNotification.popup();

                }
            }
        });

        userWaitList = new ArrayList<>();
        userWaitList = event.getWaitingList();
        adapter = new WaitlistEventAdapter(this, 100, userWaitList,event,db);
        waitlist.setAdapter(adapter);
        // When user un joins the event its is now being showed in this 
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

        drawSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventWaitlistActivity.this, ChosenListActivity.class);
                intent.putExtra("eventModel", event);
                startActivity(intent);
            }
        });
    }
}
