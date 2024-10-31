package com.example.eventlottery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class EventEntrantFragment extends Fragment {

    private FirebaseFirestore db;
    private String eventID;
    private ImageView eventPoster;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventDate;
    private Button joinUnjoinButton;
    private ImageView organizerProfilePicture;
    private TextView organizerName;
    private TextView eventDescription;

    private EventModel event;

    private DocumentReference facilityRef;

    private DocumentReference organizerRef;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private String p4p4;

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

        event = new EventModel();
        p4p4 = "before";
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

        // getting immediate fields from the event document and updating event page UI
//        eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore", error.toString());
//                    return;
//                }
//                if (value != null) {
//                    // TODO: eventPoster in part 4
//                    // TODO: refactor so that it creates a new EventModel object
//
//
//
//                    eventTitle.setText(value.get("title").toString());
//                    eventLocation.setText(value.get("location_string").toString());
//                    eventDate.setText(value.get("join_deadline").toString());
//                    eventDescription.setText(value.get("description").toString());
//
//                    //facilityRef = (DocumentReference) value.get("facility");
//                    p4p4 = value.getString("facility");
//                    Toast.makeText(getActivity(), p4p4, Toast.LENGTH_SHORT).show();
////                    facilityRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
////                        @Override
////                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
////                            if (error != null) {
////                            Log.e("Firestore", error.toString());
////                            return;
////                            }
////                            if (value != null) {
////                                organizerRef = (DocumentReference) value.get("organizer");
////                                organizerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
////                                    @Override
////                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
////                                        if (error != null) {
////                                        Log.e("Firestore", error.toString());
////                                        return;
////                                        }
////                                        if (value != null) {
////                                            // TODO: organizer profile picture in part 4
////                                            organizerName.setText(value.get("f_name").toString());
////                                        }
////                                    }
////                                });
////                            }
////                        }
////                    });
//                }
//            }
//        });
//        Toast.makeText(getActivity(), p4p4, Toast.LENGTH_SHORT).show();


//        if (facilityRef == null) {
//            Toast.makeText(getActivity(), "This is null", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(getActivity(), "This: " + facilityRef.getClass().toString(), Toast.LENGTH_SHORT).show();
//        }

        //Toast.makeText(getActivity(), "facilityRef = " + facilityRef, Toast.LENGTH_SHORT).show();
        // TODO: figure out how to access document through a reference field
        // getting organizer document reference from facility document reference
        //Toast.makeText(getContext(), facilityRef.getId(), Toast.LENGTH_SHORT).show();
//        facilityRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Log.e("Firestore", error.toString());
//                    return;
//                }
//                if (value != null) {
//                    organizerRef = (DocumentReference) value.get("organizer");
//                }
//            }
//        });
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