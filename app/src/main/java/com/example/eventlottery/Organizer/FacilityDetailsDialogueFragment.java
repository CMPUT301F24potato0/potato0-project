package com.example.eventlottery.Organizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Facility Details Dialogue Fragment
 * This class is in charge of generationg a dialog where the user can put information to create a
 * facility, and the firebase save the information of this facility and also who the owner of the
 * facility
 */
public class FacilityDetailsDialogueFragment extends DialogFragment {

    private FirebaseFirestore db;
    private UserModel user;
    private Boolean facilityDne;
    private FacilityModel facility;
    private FacilityFragment facilityFragment;

    /**
     * Constructor of FacilityDetailsDialogueFragment
     */
    public FacilityDetailsDialogueFragment() {
        super();
    }

    /**
     * Constructor of FacilityDetailsDialogueFragment
     * @param db Firebase Firestore
     * @param curUser Current User
     * @param facilityDne If the facility exists or not
     * @param facility Facility Model
     * @param facilityFragment Facility Fragment
     */
    public FacilityDetailsDialogueFragment(FirebaseFirestore db, UserModel curUser, Boolean facilityDne, FacilityModel facility, FacilityFragment facilityFragment) {
        this.db = db;
        this.user = curUser;
        this.facilityDne = facilityDne;
        this.facility = facility;
        this.facilityFragment = facilityFragment;
    }

    /**
     * This method purpose is to personalize the color of the buttons and background of the Dialogue
     * when is created (at the start)
     */
    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }

    /**
     * On create Dialog override
     * This method create the view of the Dialogue so the user can interact with it
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
        Button deleteButton = rootView.findViewById(R.id.facility_details_delete_button);

        // initial default values if creating facility for the first time are organizer's information
        if (user.getFacilityID().equals("")) {
            facilityPhoneEditText.setText(user.getPhone());
            facilityEmailEditText.setText(user.getEmail());
        }
        // an existing facility is passed to edit/update with new information
        // get details from the facility to fill out form as initial default values
        // also show delete button to remove facility if wanted
        else {
            facilityNameEditText.setText(facility.getName());
            facilityLocationEditText.setText(facility.getLocation());
            facilityPhoneEditText.setText(facility.getPhone());
            facilityEmailEditText.setText(facility.getEmail());
            facilityCapacityEditText.setText(facility.getCapacity().toString());
            deleteButton.setVisibility(View.VISIBLE);
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
                    db.collection("facilities").document(facility.getUserID()).set(facility);
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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFac(facility,db,facilityFragment,user);
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
            Long number = Long.parseLong(str);
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


    public void deleteFac(FacilityModel facility, FirebaseFirestore db, FacilityFragment facilityFragment, UserModel user) {
        facility.delete(db);
        facilityFragment.changeView(0);
        // after updating or creating a facility, update the view
        facilityFragment.updateViews();
        user.setFacilityID("");
        db.collection("users").document(user.getiD()).set(user);
    }
}