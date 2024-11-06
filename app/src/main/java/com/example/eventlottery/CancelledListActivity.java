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

public class CancelledListActivity extends AppCompatActivity {

    private ArrayList<UsersList> userCancelList;
    private ListView cancelledList;
    private ArrayAdapter<UsersList> cancelAdapter;
    private EventModel event;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        cancelAdapter = new UserListviewAdapter(this, 0, userCancelList, "cancelled", event, db);
        cancelledList.setAdapter(cancelAdapter);

        // Add back button functionality
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CancelledListActivity.this, EventOrganizerActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
