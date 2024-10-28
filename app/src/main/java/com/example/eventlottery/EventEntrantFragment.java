package com.example.eventlottery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;


public class EventEntrantFragment extends Fragment {

    FirebaseFirestore db;
    String eventID;
    ImageView eventPoster;
    TextView eventTitle;
    TextView eventLocation;
    TextView eventDate;
    Button joinUnjoinButton;
    ImageView organizerProfilePicture;
    TextView organizerName;
    TextView eventDescription;

    DocumentReference facilityRef;
    DocumentReference organizerRef;


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

        // getting event information from Firestore
        DocumentReference eventRef = db.collection("events").document(eventID);
        // getting immediate fields from the event document and updating event page UI
        eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    // TODO: eventPoster in part 4
                    // TODO: refactor so that it creates a new EventModel object
                    eventTitle.setText(value.get("title").toString());
                    eventLocation.setText(value.get("location_string").toString());
                    eventDate.setText(value.get("join_deadline").toString());
                    eventDescription.setText(value.get("description").toString());

                    facilityRef = db.document(value.get("facility").toString());
                }
            }
        });

        // TODO: figure out how to access document through a reference field
        // getting organizer document reference from facility document reference
        facilityRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (value != null) {
                    organizerRef = (DocumentReference) value.get("organizer");
                }
            }
        });
        // getting organizer information and updating event page UI
//        organizerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore", error.toString());
//                    return;
//                }
//                if (value != null) {
//                    // TODO: organizer profile picture in part 4
//                    organizerName.setText(value.get("f_name").toString());
//                }
//            }
//        });

        // TODO: add functionality to join button

        // TODO: add functionality to unjoin button
        // TODO: 1) check if user already joined
        // TODO: 2) if joined, change text in button to "Unjoin"
        // TODO: 3) add functionality to unjoin the event


        return rootView;
    }




}