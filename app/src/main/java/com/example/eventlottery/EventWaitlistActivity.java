package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class EventWaitlistActivity extends AppCompatActivity {

    private Button notify;
    private ListView waitlist;
    private EventModel event;
    private UserListviewAdapter adapter;
    private Button remove;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<UsersList> userWaitList;

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

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = event.getEventID() + "_waitlist";
                SendNotification sendNotification = new SendNotification(getApplicationContext(), topic);
                sendNotification.popup();
            }
        });

        userWaitList = new ArrayList<>();
        userWaitList = event.getWaitingList();
        adapter = new UserListviewAdapter(this, R.layout.user_listview_content, userWaitList, "waitlist", event, db);

        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (doc != null && doc.exists()) {
                            userWaitList.clear();
                            for (UsersList u : doc.toObject(EventModel.class).getWaitingList()) {
                                userWaitList.add(u);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        waitlist.setAdapter(adapter);

        // Add back button functionality
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventWaitlistActivity.this, EventOrganizerActivity.class);
            startActivity(intent);
            finish();
        });
        waitlist.setAdapter(adapter);
        db.collection("events").
                document(event.getEventID()).
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (doc != null && doc.exists()) {
                    userWaitList.clear();
                    for(UsersList u : doc.toObject(EventModel.class).getWaitingList()) {
                        userWaitList.add(u);
                        adapter.notifyDataSetChanged();
                    }
//                    userWaitList = (ArrayList<UsersList>) doc.get("waitingList");
                }
            }
        });
    }
}
