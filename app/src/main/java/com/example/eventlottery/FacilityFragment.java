package com.example.eventlottery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private CurrentUser curUser;
    private FirebaseFirestore db;

    public FacilityFragment(FirebaseFirestore db, CurrentUser curUser) {
        this.db = db;
        this.curUser = curUser;
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

        return rootview;
    }
}
