package com.example.eventlottery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ChosenListActivity extends AppCompatActivity {

    private Integer sample_amount;
    private Integer remaining_spots;
    private EventModel event;
    private TextView chosenEntrantsCount;
    private Button send_invites_button;
    private Button resample_button;
    private EditText searchbar;
    private ListView chosenEntrantsListView;
    private ChosenEntrantsAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference eventRef;

    private ArrayList<UsersList> chosen_entrants;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chosen_entrants);

        // Initialize views
        chosenEntrantsCount = findViewById(R.id.chosen_entrants_number_chosen);
        send_invites_button = findViewById(R.id.chosen_entrants_send_invites_button);
        resample_button = findViewById(R.id.chosen_entrants_resample_button);
        searchbar = findViewById(R.id.chosen_entrants_search_edittext);  // TODO: implement in part 4
        chosenEntrantsListView = findViewById(R.id.chosen_entrants_listview);

        eventRef = db.collection("events").document(event.getEventID());

        // get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = (EventModel) extras.getSerializable("eventModel");
            sample_amount =  extras.getInt("sample_amount");
            remaining_spots = extras.getInt("remaining_spots");
        }

        // Empty the chosen list (this happens only once after sample button is clicked)
        event.getChosenList().clear();
        // Make a copy of waitlist
        ArrayList<UsersList> waitlist_copy = (ArrayList<UsersList>) event.getWaitingList().clone();

        // Initial draw random sample based on provided sample_amount
        sampleEntrants(waitlist_copy, sample_amount);


        // Set up adapter for ListView
        adapter = new ChosenEntrantsAdapter(this, event, db);
        chosenEntrantsListView.setAdapter(adapter);

        // Set up resample button
        resample_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosen_entrants = LotterySystem.sampleEntrants(event.getWaitingList(), sample_amount);

                updateChosenEntrantsCount();
            }
        });


        // Set up invite button
        send_invites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (UsersList entrant : event.getChosenList()) {
                    try {
                        event.queueInvitedList(entrant);
                        event.unqueueChosenList(entrant);
                    } catch (Exception e) {
                        Toast.makeText(ChosenListActivity.this, "Entrant " + entrant.getName() + " is already in the invited list or is not in chosen list anymore.", Toast.LENGTH_SHORT).show();
                    }
                }
                // update database with invited entrants and emptied out chosen list
                eventRef.set(event);
            }
        });
    }


    /**
     * Samples entrants from an ArrayList, removing them from that ArrayList, and updating both event model and Firestore with the chosen entrants
     * @param entrants The ArrayList of entrants to choose and remove entrants from
     * @param sample_amount How many entrants to sample from the ArrayList of entrants
     */
    private void sampleEntrants(ArrayList<UsersList> entrants, Integer sample_amount) {
        // Sample a subset of entrants
        chosen_entrants = LotterySystem.sampleEntrants(entrants, sample_amount);
        // Removing chosen entrants from the provided ArrayList and updating event model with chosen entrants
        for (UsersList entrant : chosen_entrants) {
            try {
                removeEntrant(entrants, entrant);
                event.queueChosenList(entrant);
            }
            catch (IllegalArgumentException e) {
                Log.e("Error", e.toString());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "Entrant " + entrant.getName() + " is already in the chosen list.", Toast.LENGTH_SHORT).show();
            }
        }
        // Updating Firestore with the chosen entrants
        eventRef.set(event);
        // Updating remaining spots available for the event
        remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
    }

    /**
     * Updates the count text for chosen entrants.
     */
    private void updateChosenEntrantsCount() {
        String countText = Integer.toString(event.getChosenList().size());
        chosenEntrantsCount.setText(countText);
    }

    /**
     * A helper function to remove the provided entrant from the provided ArrayList of entrants
     * @param entrantsList The ArrayList of entrants
     * @param entrant The entrant to remove from the ArrayList
     */
    private void removeEntrant(ArrayList<UsersList> entrantsList, UsersList entrant) throws IllegalArgumentException {
        for (int i = 0; i < entrantsList.size(); i++) {
            if (entrantsList.get(i).getiD().equals(entrant.getiD())) {
                entrantsList.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Entrant " + entrant.getName() + " (" + entrant.getiD() + ") is not inside the ArrayList provided.");
    }

}
