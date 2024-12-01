package com.example.eventlottery.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * class that generates a fragment that shows the details or information of a FacilityModel object
 * and also the ability of delete that facility
 */
public class AdminFacilityDetailsFragment extends DialogFragment {
    private final FirebaseFirestore db;
    private final FacilityModel facility;

    /**
     * Constructor of AdminFacilityDetailsFragment
     * @param item
     *      A FacilityModel object
     */
    public AdminFacilityDetailsFragment(FacilityModel item) {
        db = FirebaseFirestore.getInstance();
        facility = item;
    }

    /**
     * This method generate the fragment with the information of the facility
     * it has 2 buttons one for deleting the event and the other one
     * for going back
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return
     *      It return the fragment that was create so the user can see it and interact with it
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.admin_fragment_facility_details_dialog, null);
        ((TextView)rootView.findViewById(R.id.facility_details_text_facility_name)).setText(String.format("Name: %s", facility.getName()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_location)).setText(String.format("Location: %s", facility.getLocation()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_phone_number)).setText(String.format("Phone: %s", facility.getPhone()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_email)).setText(String.format("Email: %s", facility.getEmail()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_capacity)).setText(String.format("Capacity: %d", facility.getCapacity()));AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        rootView.findViewById(R.id.delete_button).setOnClickListener((View view) -> {
            facility.delete(db);
            dismiss();
        });
        rootView.findViewById(R.id.cancel_button).setOnClickListener((View view) -> {
            dismiss();
        });
        builder.setView(rootView);
        return builder.create();
    }
}
