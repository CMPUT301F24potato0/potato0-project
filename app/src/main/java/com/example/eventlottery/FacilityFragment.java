package com.example.eventlottery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * This class is the facility fragment
 * This class inflates fragment_facility
 */

public class FacilityFragment extends Fragment {

    public FacilityFragment(){
        // require a empty public constructor
    }

    FacilityFragment currentFragment = this;

    private Boolean facility_dne;
    private CurrentUser curUser;
    private FirebaseFirestore db;
    private FacilityModel facilityModel;

    ConstraintLayout progressBar;

    public FacilityFragment(FirebaseFirestore db, CurrentUser curUser, FacilityModel facility) {
        this.db = db;
        this.curUser = curUser;
        this.facility_dne = curUser.getFacilityID().equals("");
        this.facilityModel = facility;
    }
    private ConstraintLayout createFacilityFirstPage;
    private ConstraintLayout facilityPage;
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
        createFacilityFirstPage = rootview.findViewById(R.id.createFacilityFirstPage);
        facilityPage = rootview.findViewById(R.id.FacilityPage);
        progressBar = rootview.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        if (curUser.getFacilityID().equals("")) {
            changeView(0);
        }
        else {
            changeView(1);
        }

//        db.collection("users").document(curUser.getiD()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    progressBar.setVisibility(View.GONE);
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        curUser = document.toObject(CurrentUser.class);
//                        if (curUser.getFacilityID().equals("")) {
//                            changeView(0);
//                        } else {
//                            changeView(1);
//                        }
//                    } else {
//                        Log.d("Firestore", "No such document");
//                    }
//                } else {
//                    Log.d("Firestore", "get failed with ", task.getException());
//                }
//            }
//        });

//        if (facility_dne) {
//            changeView(0);
//        }
//        checkFacility(createFacilityFirstPage, facilityPage);

        Button createFacilityBtn = rootview.findViewById(R.id.create_facility_button);
        createFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FacilityDetailsDialogueFragment(db, curUser, facility_dne, facilityModel, currentFragment).show(getFragmentManager(), "FacilityDetailsDialogueFragment");
            }
        });

        Button editFacilityBtn = rootview.findViewById(R.id.facility_page_edit_facility_button);
        editFacilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FacilityDetailsDialogueFragment(db, curUser, facility_dne, facilityModel, currentFragment).show(getFragmentManager(), "FacilityDetailsDialogueFragment");
            }
        });

        return rootview;
    }
    public void checkFacility(ConstraintLayout createFacilityFirstPage, ConstraintLayout facilityPage){
        if(curUser.getFacilityID().equals("")){
            createFacilityFirstPage.setVisibility(View.VISIBLE);
            facilityPage.setVisibility(View.GONE);
        } else {
            createFacilityFirstPage.setVisibility(View.GONE);
            facilityPage.setVisibility(View.VISIBLE);
        }
    }

    public void changeView(int fac){
        if (fac == 0) {
            createFacilityFirstPage.setVisibility(View.VISIBLE);
            facilityPage.setVisibility(View.GONE);
        } else {
            createFacilityFirstPage.setVisibility(View.GONE);
            facilityPage.setVisibility(View.VISIBLE);
        }
    }
}
