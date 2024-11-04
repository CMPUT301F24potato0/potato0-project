package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class FacilityDetailsDialogueFragment extends DialogFragment {

    private FirebaseFirestore db;
    private CurrentUser user;
    private Boolean facilityDne;
    private FacilityModel facility;
    private FacilityFragment facilityFragment;

    public FacilityDetailsDialogueFragment() {
        super();
    }

    public FacilityDetailsDialogueFragment(FirebaseFirestore db, CurrentUser user) {
        this.db = db;
        this.user = user;
    }

    public FacilityDetailsDialogueFragment(FirebaseFirestore db, CurrentUser curUser, Boolean facilityDne, FacilityModel facility, FacilityFragment facilityFragment) {
        this.db = db;
        this.user = curUser;
        this.facilityDne = facilityDne;
        this.facility = facility;
        this.facilityFragment = facilityFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_facility_details_dialogue, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(rootView);


        EditText facilityNameEditText = rootView.findViewById(R.id.facility_details_edittext_facility_name);
        EditText facilityLocationEditText = rootView.findViewById(R.id.facility_details_edittext_location);
        EditText facilityPhoneEditText = rootView.findViewById(R.id.facility_details_edittext_phone_number);
        EditText facilityEmailEditText = rootView.findViewById(R.id.facility_details_edittext_email);
        EditText facilityCapacityEditText = rootView.findViewById(R.id.facility_details_edittext_capacity);

        Button confirmButton = rootView.findViewById(R.id.facility_details_confirm_button);
        Button cancelButton = rootView.findViewById(R.id.facility_details_cancel_button);

        facilityPhoneEditText.setText(user.getPhone());

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String facilityName = facilityNameEditText.getText().toString();
                String facilityLocation = facilityLocationEditText.getText().toString();
                String facilityPhone = facilityPhoneEditText.getText().toString();
                String facilityEmail = facilityEmailEditText.getText().toString();
                Integer facilityCapacity = Integer.parseInt(facilityCapacityEditText.getText().toString());
                db.collection("facilities").document(user.getiD()).set(new FacilityModel(facilityName, facilityLocation, facilityPhone, facilityEmail, facilityCapacity, user.getiD()));
                user.setFacilityID(user.getiD());
                db.collection("users").document(user.getiD()).set(user);
                facilityFragment.changeView(1);
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }
}