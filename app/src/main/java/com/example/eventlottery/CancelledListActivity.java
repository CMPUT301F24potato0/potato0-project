package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Cancelled List Activity
 * This activity displays a list of users who have been cancelled by the organizer of the event.
 */
public class CancelledListActivity extends AppCompatActivity {

    private ArrayList<UsersList> userCancelList;
    private ListView cancelledList;
    private ArrayAdapter<UsersList> cancelAdapter;
    private EventModel event;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Called when the activity is first created.
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

        userCancelList = new ArrayList<UsersList>();

        Bundle e = getIntent().getExtras();
        if (e != null) {
            event = (EventModel) e.getSerializable("eventModel");
        }

        userCancelList = event.getCancelledList();

        cancelledList = findViewById(R.id.cancelled_list);
        cancelAdapter = new CancelledListArrayAdapter(this, 0, userCancelList, "cancelled", event, db);
        cancelledList.setAdapter(cancelAdapter);
    }
}
