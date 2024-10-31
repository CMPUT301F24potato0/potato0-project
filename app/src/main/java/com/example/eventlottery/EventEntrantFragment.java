package com.example.eventlottery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;


public class EventEntrantFragment extends Fragment {
    // Current User
    private String curUser;
    // Database Stuff
    private FirebaseFirestore db;
    private TextView organizerName;
    private DocumentReference facilityRef;
    private DocumentReference organizerRef;
    // Event stuff
    private String eventID;
    private EventModel event;
    // XML stuff
    private TextView eventDescription;
    private ImageView eventPoster;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventDate;
    private Button joinUnjoinButton;
    private ImageView organizerProfilePicture;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    public EventEntrantFragment() {
        super();
    }

    public EventEntrantFragment(FirebaseFirestore db, String eventID) {
        super();
        this.db = db;
        this.eventID = eventID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_entrant, container, false);

        // getting views from event entrant UI page (fragment)
        eventPoster = rootView.findViewById(R.id.event_entrant_page_event_poster);
        eventTitle = rootView.findViewById(R.id.event_entrant_page_event_title);
        eventLocation = rootView.findViewById(R.id.event_entrant_page_event_location);
        eventDate = rootView.findViewById(R.id.event_entrant_page_event_date);
        joinUnjoinButton = rootView.findViewById(R.id.event_entrant_page_join_unjoin_button);
        organizerProfilePicture = rootView.findViewById(R.id.event_entrant_page_profile_picture);
        organizerName = rootView.findViewById(R.id.event_entrant_page_organizer_name);
        eventDescription = rootView.findViewById(R.id.event_entrant_page_event_details);

        progressBar = rootView.findViewById(R.id.progressBar);
        linearLayout = rootView.findViewById(R.id.linearLayout);

        // getting event information from Firestore
        DocumentReference eventRef = db.collection("events").document(eventID);
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Firebase_Data", "Document data: " + document.getData());
                        eventTitle.setText(document.getString("title"));
                        eventLocation.setText(document.getString("location_string"));
                        Date javaDate = document.getTimestamp("join_deadline").toDate();
                        eventDate.setText(javaDate.toString());

                        organizerRef = document.getDocumentReference("organizer");

                        assert organizerRef != null;
                        organizerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                progressBar.setVisibility(View.GONE); // Hide Progress bar
                                linearLayout.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    DocumentSnapshot userDoc = task.getResult();
                                    if (userDoc.exists()) {
                                        organizerName.setText(userDoc.getString("f_name") + " " + userDoc.getString("l_name"));
                                    }
                                }
                            }
                        });
                        eventDescription.setText(document.getString("description"));
                    } else {
                        Log.d("Firebase_Data", "No such document");
                    }
                } else {
                    Log.d("Firebase_Data", "get failed with ", task.getException());
                }
            }
        });

        // TODO: add functionality to join button
        joinUnjoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventRef.update("waiting_list", FieldValue.arrayUnion(Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID)));
            }
        });
        // TODO: add functionality to unjoin button
        // TODO: 1) check if user already joined
        // TODO: 2) if joined, change text in button to "Unjoin"
        // TODO: 3) add functionality to unjoin the event


        return rootView;
    }

    private void addUser(FirebaseFirestore db) {

    }


}