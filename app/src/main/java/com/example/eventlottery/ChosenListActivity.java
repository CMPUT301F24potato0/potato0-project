package com.example.eventlottery;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

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
    private ChosenListActivity chosenListActivity = this;

    private ArrayList<UsersList> chosenEntrantsTemp;  // for storing entrants chosen by the LotterySystem class but not yet added to the event model
    private ArrayList<UsersList> chosenEntrantsModel; // a reference to the chosen entrants list from the event model

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
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

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = (EventModel) extras.getSerializable("eventModel");
            sample_amount =  extras.getInt("sample_amount");
            remaining_spots = extras.getInt("remaining_spots");
        }

        eventRef = db.collection("events").document(event.getEventID());
        chosenEntrantsModel = event.getChosenList();

        // Empty the chosen list (this happens only once after sample button is clicked)
        chosenEntrantsModel.clear();
        // Make a copy of waitlist
        ArrayList<UsersList> waitlist_copy = (ArrayList<UsersList>) event.getWaitingList().clone();

        // Set up adapter for ListView
        adapter = new ChosenEntrantsAdapter(this, waitlist_copy, event, db, chosenListActivity);
        chosenEntrantsListView.setAdapter(adapter);

        // Initial draw random sample based on provided sample_amount
        sampleEntrants(waitlist_copy, sample_amount);
        updateChosenCountAndRemainingSpotsLeft();

        // Set up resample button
        resample_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChosenListActivity.this);
                Integer chooseUpto = remaining_spots <= waitlist_copy.size() ? remaining_spots : waitlist_copy.size();
                builder.setTitle("Please enter a number up to " + chooseUpto);
                EditText input = new EditText(ChosenListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (input.getText().toString().equals("")) {
                            Toast.makeText(chosenListActivity, "Please enter a number.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            sample_amount = Integer.parseInt(input.getText().toString());
                            if (waitlist_copy.size() < sample_amount) {
                                Toast.makeText(chosenListActivity, "Waiting list only has " + waitlist_copy.size() + " entrants remaining!", Toast.LENGTH_SHORT).show();
                            }
                            else if (remaining_spots < sample_amount) {
                                Toast.makeText(chosenListActivity, "Your event only has " + remaining_spots + " remaining spots left!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                sampleEntrants(waitlist_copy, Integer.parseInt(input.getText().toString()));
                                updateChosenCountAndRemainingSpotsLeft();
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });

        //ActivityResultContracts<Intent> startActivityIntent =

        // Set up invite button
        send_invites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: implement notifications
                Boolean sent = false;
                new SendNotificationDialog(event, "Chosen", sent, db, chosenListActivity).show(getSupportFragmentManager(), "Send Notification");
//                .show(getSupportFragmentManager(), "Send Notification");
            }
        });


        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("EventWaitlistActivity", e.toString());
                        }
                        if (doc != null && doc.exists()) {
                            // Get the EventModel object
                            EventModel FireStoreEvent = doc.toObject(EventModel.class);

                            // Update Waiting List
                            event.getWaitingList().clear();
                            event.getWaitingList().addAll(FireStoreEvent.getWaitingList());
                            // Update Cancelled List
                            event.getCancelledList().clear();
                            event.getCancelledList().addAll(FireStoreEvent.getCancelledList());
                            // Update Chosen List
                            event.getChosenList().clear();
                            event.getChosenList().addAll(FireStoreEvent.getChosenList());
                            // Update Enrolled List
                            event.getEnrolledList().clear();
                            event.getEnrolledList().addAll(FireStoreEvent.getEnrolledList());
                            // Update Invited List
                            event.getInvitedList().clear();
                            event.getInvitedList().addAll(FireStoreEvent.getInvitedList());
                            // Notify adapter of changes
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * Samples entrants from an ArrayList, removing them from that ArrayList, and updating both event model and Firestore with the chosen entrants
     * @param entrantsList The ArrayList of entrants to choose and remove entrants from
     * @param sample_amount How many entrants to sample from the ArrayList of entrants
     */
    private void sampleEntrants(ArrayList<UsersList> entrantsList, Integer sample_amount) {
        // Sample a subset of entrants
        chosenEntrantsTemp = LotterySystem.sampleEntrants(entrantsList, sample_amount);
        // Removing chosen entrants from the provided ArrayList and updating event model with chosen entrants
        for (UsersList entrant : chosenEntrantsTemp) {
            try {
                removeEntrant(entrantsList, entrant);
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
        // Updating Firestore with the chosen entrants and notifying ArrayAdapter
        eventRef.set(event);
        adapter.notifyDataSetChanged();
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

    /**
     * A helper function to update how many chosen entrants there are in the UI
     * and also how many remaining spots are left
     */
    public void updateChosenCountAndRemainingSpotsLeft() {
        // Updating how many are chosen in the UI
        String chosenCount = Integer.toString(chosenEntrantsModel.size());
        chosenEntrantsCount.setText(chosenCount);
        // Updating remaining spots available for the event
        remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size() - chosenEntrantsModel.size();
    }

    /**
     * A helper function to send notifications to the entrants
     */
    public void sendNotification(){

        ArrayList<UsersList> invitedEntrants = new ArrayList<>();
        for (UsersList entrant : chosenEntrantsModel) {
            try {
                event.queueInvitedList(entrant);
                event.unqueueWaitingList(entrant);
                invitedEntrants.add(entrant);
            } catch (Exception e) {
                Toast.makeText(ChosenListActivity.this, "Entrant " + entrant.getName() + " is already in the invited list or is not in chosen list anymore.", Toast.LENGTH_SHORT).show();
            }
        }
        event.getChosenList().removeAll(invitedEntrants);
        eventRef.set(event);
        Toast.makeText(chosenListActivity, "Sent Notification", Toast.LENGTH_SHORT).show();
    }
}
