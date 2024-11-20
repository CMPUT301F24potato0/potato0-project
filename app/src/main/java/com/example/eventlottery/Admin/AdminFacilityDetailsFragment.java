package com.example.eventlottery.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventlottery.CurrentUser;
import com.example.eventlottery.FacilityModel;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminFacilityDetailsFragment extends DialogFragment {
    private final FirebaseFirestore db;
    private FacilityModel facility;

    public AdminFacilityDetailsFragment(FacilityModel item) {
        db = FirebaseFirestore.getInstance();
        facility = item;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.admin_fragment_facility_details_dialog, null);
        ((TextView)rootView.findViewById(R.id.facility_details_text_facility_name)).setText(String.format("Name: %s", facility.getName()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_location)).setText(String.format("Location: %s", facility.getLocation()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_phone_number)).setText(String.format("Phone: %s", facility.getPhone()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_email)).setText(String.format("Email: %s", facility.getEmail()));
        ((TextView)rootView.findViewById(R.id.facility_details_text_capacity)).setText(String.format("Capacity: %d", facility.getCapacity()));AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ((Button) rootView.findViewById(R.id.delete_button)).setOnClickListener((View view) -> {
            db.collection("facilities").document(facility.getUserID()).delete();
            Task<DocumentSnapshot> task = db.collection("users").document(facility.getUserID()).get();
            task.addOnCompleteListener((Task<DocumentSnapshot> posttask) -> {
                CurrentUser owner = posttask.getResult().toObject(CurrentUser.class);
                owner.setFacilityID("");
                db.collection("users").document(owner.getiD()).set(owner);
            });
            dismiss();
        });
        ((Button) rootView.findViewById(R.id.cancel_button)).setOnClickListener((View view) -> {
            dismiss();
        });
        builder.setView(rootView);
        return builder.create();
    }
}
