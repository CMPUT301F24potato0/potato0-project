package com.example.eventlottery;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ChosenListActivity extends AppCompatActivity {

    private ArrayList<UsersList> chosenEntrants;
    private EventModel event;
    private TextView chosenEntrantsCount;
    private ListView chosenEntrantsListView;
    private ChosenEntrantsAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chosen_entrants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get data from intent
        event = (EventModel) getIntent().getSerializableExtra("eventModel");
        chosenEntrants = (ArrayList<UsersList>) getIntent().getSerializableExtra("chosenEntrants");

        // Initialize views
        chosenEntrantsCount = findViewById(R.id.chosen_entrants_number_chosen);
        chosenEntrantsListView = findViewById(R.id.chosen_entrants_listview);

        // Update the count text
        updateChosenEntrantsCount();

        // Set up adapter for ListView
        adapter = new ChosenEntrantsAdapter(this, chosenEntrants, event, db);
        chosenEntrantsListView.setAdapter(adapter);
    }

    /**
     * Updates the count text for chosen entrants.
     */
    private void updateChosenEntrantsCount() {
        String countText = chosenEntrants.size() + "/" + event.getCapacity();
        chosenEntrantsCount.setText(countText);
    }
}
