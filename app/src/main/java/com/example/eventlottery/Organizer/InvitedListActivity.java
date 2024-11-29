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
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Notifications.SendNotificationDialog;
import com.example.eventlottery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * This class is the InvitedListActivity
 */
public class InvitedListActivity extends AppCompatActivity {

    private ArrayList<RemoteUserRef> userInvitedList;
    private ListView invitedList;
    private ArrayAdapter<RemoteUserRef> invitedAdapter;
    private EventModel event;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button notify_invited;
    private TextView invitedConfirmed;
    private FloatingActionButton backFab;

    /**
     * On create Override
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_invited_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userInvitedList = new ArrayList<>();

        Bundle e = getIntent().getExtras();
        if (e != null) {
            event = (EventModel) e.getSerializable("eventModel");
        }

        userInvitedList = event.getInvitedList();

        invitedList = findViewById(R.id.invited_list);
        invitedAdapter = new InvitedListArrayAdapter(this, userInvitedList, event, db);
        invitedList.setAdapter(invitedAdapter);
        backFab = findViewById(R.id.back);
        notify_invited = findViewById(R.id.invited_notif_button);
        invitedConfirmed = findViewById(R.id.invited_confirmed);

        invitedConfirmed.setText(String.valueOf(event.getInvitedList().size()));

        notify_invited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendNotificationDialog(event, "Invited", false, db).show(getSupportFragmentManager(), "Send Notification");
            }
        });

        // Update
        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("InvitedListActivity", e.toString());
                        }
                        if (doc != null && doc.exists()) {
                            // Get the EventModel object
                            EventModel FireStoreEvent = doc.toObject(EventModel.class);

                            // Update Invited List
                            event.getInvitedList().clear();
                            event.getInvitedList().addAll(FireStoreEvent.getInvitedList());
                            invitedConfirmed.setText(String.valueOf(event.getInvitedList().size()));

                            // Notify adapter of changes
                            invitedAdapter.notifyDataSetChanged();
                        }
                    }
                });
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
