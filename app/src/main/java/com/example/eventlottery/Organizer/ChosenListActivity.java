package com.example.eventlottery.Organizer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Notifications.SendNotificationDialog;
import com.example.eventlottery.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class ChosenListActivity extends AppCompatActivity implements ChosenEntrantsAdapter.ChosenEntrantsAdapterCallback, NumberPickerDialogFragment.NumberPickerDialogFragmentListener {

    private Integer sample_amount;
    private Integer remaining_spots;
    private EventModel event;
    private TextView chosenEntrantsCount;
    private Button send_invites_button;
    private Button resample_button;
    private EditText searchbar;
    private ListView chosenEntrantsListView;
    private ChosenEntrantsAdapter chosenAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference eventRef;

    // ArrayLists used for the lottery system
    private ArrayList<RemoteUserRef> chosenEntrants;
    private ArrayList<RemoteUserRef> waitlistedEntrants;


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
        searchbar = findViewById(R.id.chosen_entrants_search_edittext);  // not to be implemented for part 4
        chosenEntrantsListView = findViewById(R.id.chosen_entrants_listview);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            event = (EventModel) extras.getSerializable("eventModel");
            sample_amount =  extras.getInt("sample_amount");
            remaining_spots = extras.getInt("remaining_spots");
        }

        // Prepare event document reference and necessary lists
        eventRef = db.collection("events").document(event.getEventID());
        chosenEntrants = event.getChosenList();
        waitlistedEntrants = event.getWaitingList();

        // Set up adapter for ListView
        chosenAdapter = new ChosenEntrantsAdapter(this, chosenEntrants);
        chosenAdapter.setCallback(this);
        chosenEntrantsListView.setAdapter(chosenAdapter);

        // Initial draw random sample based on provided sample_amount
        sampleEntrants(sample_amount);

        // When the sample button is pressed and the activity is launched, after sampling, automatically open
        // the invitation notification dialog, so the user can immediately send invites (and to prevent them from forgetting)
        if (!chosenEntrants.isEmpty()) {
            openInvitationNotificationDialog();
        }

        // TODO: reimplement with new dialog fragment that matches the UI theme
        // Set up resample button
        resample_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remaining_spots == 0) {
                    Toast.makeText(ChosenListActivity.this, "There is no more capacity in the event!", Toast.LENGTH_SHORT).show();
                }
                else if (waitlistedEntrants.size() == 0) {
                    Toast.makeText(ChosenListActivity.this, "There are no more entrants in the waiting list!", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Allow organizer to sample only up to remaining spots left, and cannot pick more than the number of entrants inside the waitlist
                    Integer numberSampleable = Integer.min(remaining_spots, waitlistedEntrants.size());
                    new NumberPickerDialogFragment(0, numberSampleable).show(getSupportFragmentManager(), "NumberPickerDialogFragment");
                }
            }
        });

//        resample_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChosenListActivity.this);
//                Integer chooseUpto = remaining_spots <= waitlistedEntrants.size() ? remaining_spots : waitlistedEntrants.size();
//                builder.setTitle("Please enter a number up to " + chooseUpto);
//                EditText input = new EditText(ChosenListActivity.this);
//                input.setInputType(InputType.TYPE_CLASS_NUMBER);
//                builder.setView(input);
//                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (input.getText().toString().equals("")) {
//                            Toast.makeText(getBaseContext(), "Please enter a number.", Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            sample_amount = Integer.parseInt(input.getText().toString());
//                            if (waitlistedEntrants.size() < sample_amount) {
//                                Toast.makeText(getBaseContext(), "Waiting list only has " + waitlistedEntrants.size() + " entrants remaining!", Toast.LENGTH_SHORT).show();
//                            }
//                            else if (remaining_spots < sample_amount) {
//                                Toast.makeText(getBaseContext(), "Your event only has " + remaining_spots + " remaining spots left!", Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                sampleEntrants(Integer.parseInt(input.getText().toString()));
//                                updateChosenCountAndRemainingSpotsLeft();
//                            }
//                        }
//                    }
//                });
//                builder.create().show();
//            }
//        });


        // Set up invite button
        send_invites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInvitationNotificationDialog();
            }
        });

        // Keeping the lists updated
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
                            chosenAdapter.notifyDataSetChanged();
                            // Update counts and remaining spots left
                            updateChosenCountAndRemainingSpotsLeft();
                        }
                    }
                });
    }


    /**
     * The lottery functionality where the waitlisted entrants are sampled and turned into chosen entrants
     * @param sample_amount How many entrants to sample from the waitlist
     */
    private void sampleEntrants(Integer sample_amount) {
        Boolean queueUnqueueSuccess = Boolean.TRUE;
        // Sample a subset of entrants
        ArrayList<RemoteUserRef> lotteryWinners = LotterySystem.sampleEntrants(waitlistedEntrants, sample_amount);

        // Duplicate the original states of the lists in case of an exception
        ArrayList<RemoteUserRef> waitlistedEntrantsOriginal = (ArrayList<RemoteUserRef>) waitlistedEntrants.clone();
        ArrayList<RemoteUserRef> chosenEntrantsOriginal = (ArrayList<RemoteUserRef>) chosenEntrants.clone();

        // Moving lottery winners from waiting list to chosen list
        for (RemoteUserRef winner : lotteryWinners) {
            try {
                event.unqueueWaitingList(winner);
                event.queueChosenList(winner);
            } catch (Exception e) {
                queueUnqueueSuccess = Boolean.FALSE;
                Toast.makeText(this, "Failed moving winners from wait list to chosen list", Toast.LENGTH_SHORT).show();
            }
        }

        if (queueUnqueueSuccess) {
            // Updating FireStore database if queue and unqueue were all successful
            eventRef.set(event);
        }
        else {
            // In the case of an exception, revert the state of the ArrayLists
            Log.e("Event Model Changes Error", "Failed queue and unqueue for all winners.");
            waitlistedEntrants.clear();
            chosenEntrants.clear();
            for (RemoteUserRef entrant : waitlistedEntrantsOriginal) {
                waitlistedEntrants.add(entrant);
            }
            for (RemoteUserRef entrant : chosenEntrantsOriginal) {
                chosenEntrants.add(entrant);
            }
        }
//        chosenAdapter.notifyDataSetChanged(); // unnecessary due to snapshot listener
    }

    /**
     * A helper function to update how many chosen entrants there are in the UI
     * and also how many remaining spots are left
     */
    public void updateChosenCountAndRemainingSpotsLeft() {
        // Updating how many are chosen in the UI
        String chosenCount = Integer.toString(chosenEntrants.size());
        chosenEntrantsCount.setText(chosenCount);
        // Updating remaining spots available for the event
        remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size() - chosenEntrants.size();
    }


    /**
     * A helper function to open the dialog for sending the invitation notification
     */
    public void openInvitationNotificationDialog() {
        Boolean sent = false;
        new SendNotificationDialog(event, "Chosen", sent, db, this).show(getSupportFragmentManager(), "Send Notification");
    }


    /**
     * A helper function to send notifications to the entrants
     */
    public void sendNotification(){
        ArrayList<RemoteUserRef> invitedEntrants = new ArrayList<>();
        for (RemoteUserRef entrant : chosenEntrants) {
            try {
                event.queueInvitedList(entrant);
                invitedEntrants.add(entrant);
            } catch (Exception e) {
                Toast.makeText(ChosenListActivity.this, "Entrant " + entrant.getName() + " is already in the invited list or is not in chosen list anymore.", Toast.LENGTH_SHORT).show();
            }
        }
        event.getChosenList().removeAll(invitedEntrants);
        eventRef.set(event);
        Toast.makeText(this, "Sent Notification", Toast.LENGTH_SHORT).show();
    }


    /**
     * Overwrites the back button functionality, so it warns the organizer about uninvited chosen entrants
     */
    @Override
    public void onBackPressed() {
        if (!chosenEntrants.isEmpty()) {
            new ChosenListBackButtonDialogueFragment().show(getSupportFragmentManager(), "Pressed back button when there are still uninvited chosen entrants");
        }
        else {
            super.onBackPressed();
        }
    }


    /**
     * Implementation of the interface function of ChosenEntrantsAdapter, which removes the chosen entrant from
     * the chosen list and adds them to the cancelled list
     * @param entrant The chosen entrant to be removed from the chosen list and added to the cancelled list
     */
    @Override
    public void removeChosenEntrant(RemoteUserRef entrant) {
        try {
            event.unqueueChosenList(entrant);
            event.queueCancelledList(entrant);
        }
        catch (Exception e) {
            Toast.makeText(this, "Failed to remove entrant", Toast.LENGTH_SHORT).show();
        }
        eventRef.set(event);
        chosenAdapter.notifyDataSetChanged();
    }

    /**
     * Implementation of the interface function of NumberPickerDialogFragment,
     * which uses the lottery system by the amount picked from the number picker
     * @param numberPicked The number picked by the number picker
     */
    @Override
    public void numberPickerResult(Integer numberPicked) {
        sampleEntrants(numberPicked);
    }
}
