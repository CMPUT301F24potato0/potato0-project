package com.example.eventlottery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is the facility fragment
 * This class inflates fragment_facility
 */

public class FacilityFragment extends Fragment {

    public FacilityFragment(){
        // require a empty public constructor
    }
    private Boolean facility_dne;
    private CurrentUser curUser;
    private FirebaseFirestore db;

    public FacilityFragment(FirebaseFirestore db, CurrentUser curUser, Boolean facility_dne) {
        this.db = db;
        this.curUser = curUser;
        this.facility_dne = facility_dne;
    }

    /**
     *
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

        ConstraintLayout createFacilityFirstPage = rootview.findViewById(R.id.createFacilityFirstPage);
        ConstraintLayout facilityPage = rootview.findViewById(R.id.FacilityPage);
        checkFacility(createFacilityFirstPage, facilityPage);

        Button createFacilityBtn = (Button) rootview.findViewById(R.id.create_facility_button);
        createFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FacilityDetailsDialogueFragment(db, curUser, facility_dne).show(getFragmentManager(), "FacilityDetailsDialogueFragment");
                checkFacility(createFacilityFirstPage, facilityPage);
            }
        });

        Button editFacilityBtn = (Button) rootview.findViewById(R.id.facility_page_edit_facility_button);
        editFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FacilityDetailsDialogueFragment(db, curUser, facility_dne).show(getFragmentManager(), "FacilityDetailsDialogueFragment");
            }
        });

        return rootview;
    }
    public void checkFacility(ConstraintLayout createFacilityFirstPage, ConstraintLayout facilityPage){
        if(facility_dne){
            createFacilityFirstPage.setVisibility(View.VISIBLE);
            facilityPage.setVisibility(View.GONE);
        } else {
            createFacilityFirstPage.setVisibility(View.GONE);
            facilityPage.setVisibility(View.VISIBLE);
        }
    }
}
