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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Facility Details Dialogue Fragment
 */
public class FacilityDetailsDialogueFragment extends DialogFragment {

    private FirebaseFirestore db;
    private CurrentUser user;
    private Boolean facilityDne;
    private FacilityModel facility;
    private FacilityFragment facilityFragment;

    /**
     * Constructor
     */
    public FacilityDetailsDialogueFragment() {
        super();
    }

    /**
     * Constructor
     * @param db Firebase Firestore
     * @param curUser Current User
     * @param facilityDne If the facility exists or not
     * @param facility Facility Model
     * @param facilityFragment Facility Fragment
     */
    public FacilityDetailsDialogueFragment(FirebaseFirestore db, CurrentUser curUser, Boolean facilityDne, FacilityModel facility, FacilityFragment facilityFragment) {
        this.db = db;
        this.user = curUser;
        this.facilityDne = facilityDne;
        this.facility = facility;
        this.facilityFragment = facilityFragment;
    }

    /**
     * On create Dialog override
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return A new Dialog instance to be displayed by the Fragment.
     */
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

        // initial default values if creating facility for the first time are organizer's information
        if (user.getFacilityID().equals("")) {
            facilityPhoneEditText.setText(user.getPhone());
            facilityEmailEditText.setText(user.getEmail());
        }
        // an existing facility is passed to edit/update with new information
        // get details from the facility to fill out form as initial default values
        else {
            facilityNameEditText.setText(facility.getName());
            facilityLocationEditText.setText(facility.getLocation());
            facilityPhoneEditText.setText(facility.getPhone());
            facilityEmailEditText.setText(facility.getEmail());
            facilityCapacityEditText.setText(facility.getCapacity().toString());
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_before_validation = facilityNameEditText.getText().toString();
                String location_before_validation = facilityLocationEditText.getText().toString();
                String phone_before_validation = facilityPhoneEditText.getText().toString();
                String email_before_validation = facilityEmailEditText.getText().toString();
                String capacity_before_validation = facilityCapacityEditText.getText().toString();

                // check if input is valid first before creating/updating a facility
                if (isValidString(name_before_validation) &&
                    isValidString(location_before_validation) &&
                    (isValidNumber(phone_before_validation) || phone_before_validation.equals("")) &&
                    isValidString(email_before_validation) &&
                    isValidNumber(capacity_before_validation)) {
                    facility.setName(facilityNameEditText.getText().toString());
                    facility.setLocation(facilityLocationEditText.getText().toString());
                    facility.setPhone(facilityPhoneEditText.getText().toString());
                    facility.setEmail(facilityEmailEditText.getText().toString());
                    facility.setCapacity(Integer.parseInt(facilityCapacityEditText.getText().toString()));
                    db.collection("facilities").document(user.getiD()).set(facility);
                    if (user.getFacilityID().equals("")) {  // after creating a facility, change the view and update user to have a faciltyID
                        facilityFragment.changeView(1);
                        user.setFacilityID(user.getiD());
                        db.collection("users").document(user.getiD()).set(user);
                    }
                    // after updating or creating a facility, update the view
                    facilityFragment.updateViews();
                    dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "Invalid input", Toast.LENGTH_SHORT).show();
                }
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

    /**
     * Check if the input is valid
     * @param str String to check
     * @return If the string is valid or not
     */
    private Boolean isValidString(String str) {
        return !str.equals("");
    }

    /**
     * Check if the input is valid
     * @param str String to check
     * @return If the string is valid or not
     */
    private Boolean isValidNumber(String str) {
        if (!isValidString(str)) {
            return Boolean.FALSE;
        }
        try {
            Integer number = Integer.parseInt(str);
            if (number < 0) {
                return Boolean.FALSE;
            }
            else {
                return Boolean.TRUE;
            }
        }
        catch (NumberFormatException e) {
            return Boolean.FALSE;
        }
    }

}