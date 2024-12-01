package com.example.eventlottery.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.eventlottery.Models.EventsArrayAdapter;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class is the facility fragment
 * This class inflates fragment_facility
 * This class represent the management of the facility, so the organizer can manage the they facility
 * This class purposes is to check if a faciliy doesn't exist if the facility doesn't exist, the class will
 * generate a button to open a dialoge to create a facility
 * If a facility exist the class will show the list of events the facility has and the ability to edit the facility
 * or create new events
 */
public class FacilityFragment extends Fragment {
    /**
     * Constructor of FacilityFragment
     */
    public FacilityFragment(){
        // require a empty public constructor
    }

    FacilityFragment currentFragment = this;

    private Boolean facility_dne;
    private UserModel curUser;
    private FirebaseFirestore db;
    private FacilityModel facilityModel;

    private TextView facilityNameTextView;
    private ListView eventListView;
    private EventsArrayAdapter eventsAdapter;
    private ArrayList<EventModel> events;

    private EventModel eventClicked;

    private FacilityFragment thisFragment = this;

    /**
     * Constructor of Facility Fragment
     * @param db Firebase Firestore\
     *                          The database connection
     * @param curUser Current User
     *                          The user using the app at the moment
     * @param facility Facility Model
     *                          A facility object that belong to the current user
     */
    public FacilityFragment(FirebaseFirestore db, UserModel curUser, FacilityModel facility) {
        this.db = db;
        this.curUser = curUser;
        this.facility_dne = curUser.getFacilityID().equals("");
        this.facilityModel = facility;
    }
    private ConstraintLayout createFacilityFirstPage;
    private ConstraintLayout facilityPage;

    /**
     * On create view override
     * This method create the view of the class so the user can interact with it
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_facility, container, false);
        createFacilityFirstPage = rootview.findViewById(R.id.createFacilityFirstPage);
        facilityPage = rootview.findViewById(R.id.FacilityPage);

        facilityNameTextView =  rootview.findViewById(R.id.facility_page_facility_name);
        eventListView = rootview.findViewById(R.id.facility_page_events_listview);

        if (curUser.getFacilityID().equals("")) {
            changeView(0);
        }
        else {
            changeView(1);
            updateViews();
        }
        // TODO get events
        events = new ArrayList<EventModel>();
        eventsAdapter = new EventsArrayAdapter(requireContext(), events);
        eventListView.setAdapter(eventsAdapter);

        CollectionReference eventsRef = db.collection("events");
        // https://firebase.google.com/docs/firestore/query-data/listen
        eventsRef
                .whereEqualTo("facilityID", curUser.getiD())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Firebase Events", "Listen failed.", e);
                            return;
                        }
                        eventsAdapter.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("eventTitle") != null) {
                                events.add(doc.toObject(EventModel.class));
                            }
                        }
                        eventsAdapter.notifyDataSetChanged();
                    }
                });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (eventListView.getAdapter().getItem(position) != null) {
                    eventClicked = (EventModel) eventListView.getAdapter().getItem(position);
                    Intent i = new Intent(getActivity(), EventOrganizerActivity.class);
                    i.putExtra("event_id", eventClicked.getEventID());
                    i.putExtra("hashQR", eventClicked.getHashQR());
                    i.putExtra("eventModel", eventClicked);
                    i.putExtra("facilityModel", facilityModel);
                    i.putExtra("currentUser", curUser);
                    startActivity(i);
                }
            }
        });
        FacilityDetailsDialogueFragment facilityDetailsDF = new FacilityDetailsDialogueFragment(db, curUser, facility_dne, facilityModel, currentFragment);
        Button createFacilityBtn = rootview.findViewById(R.id.create_facility_button);
        createFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facilityDetailsDF.show(getFragmentManager(), "FacilityDetailsDialogueFragment");
            }
        });

        Button editFacilityBtn = rootview.findViewById(R.id.facility_page_edit_facility_button);
        editFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FacilityDetailsDialogueFragment(db, curUser, facility_dne, facilityModel, currentFragment).show(getFragmentManager(), "FacilityDetailsDialogueFragment");
            }
        });

        Button addEventBtn = rootview.findViewById(R.id.facility_page_add_event_button);
        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateEventDialogueFragment(curUser, facilityModel, db).show(getFragmentManager(), "CreateEventDialogueFragment");
            }
        });

        CollectionReference facilityCol = db.collection("facilities");
        facilityCol.whereEqualTo(FieldPath.documentId(), curUser.getiD()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("ProfileFragment", "listen:error", error);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case REMOVED:
                            Log.d("ProfileFragment", "Removed user: " + dc.getDocument().getData());
                            facilityDetailsDF.deleteFac(facilityModel, db, thisFragment, curUser);
                            break;
                    }
                }
            }
        });

        return rootview;
    }

    /**
     * Change view
     * @param fac Facility boolean
     */
    public void changeView(int fac){
        if (fac == 0) {
            createFacilityFirstPage.setVisibility(View.VISIBLE);
            facilityPage.setVisibility(View.GONE);
        } else {
            createFacilityFirstPage.setVisibility(View.GONE);
            facilityPage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Update views
     */
    public void updateViews(){
        facilityNameTextView.setText(facilityModel.getName());
    }
}
