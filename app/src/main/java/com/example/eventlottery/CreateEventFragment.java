package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.text.DecimalFormatSymbols;

public class CreateEventFragment extends DialogFragment {

    FrameLayout frameLayout;
    Integer dialogState;

    View stateView;
    String eventTitle;
    Integer capacity;
    Integer waitListLimit;
    Boolean geolocationRequired;
    String eventDescription;

    CurrentUser organizer;

    public CreateEventFragment() {
        // initial values
        eventTitle = "";
        capacity = 0;
        waitListLimit = 0;
        geolocationRequired = Boolean.FALSE;
        eventDescription = "";
    }

    public CreateEventFragment(CurrentUser organizer) {
        this();
        this.organizer = organizer;
    }

    public CreateEventFragment(String eventTitle,
                               Integer capacity,
                               Integer waitListLimit,
                               Boolean geolocationRequired,
                               String eventDescription,
                               CurrentUser organizer) {
        this.eventTitle = eventTitle;
        this.capacity = capacity;
        this.waitListLimit = waitListLimit;
        this.geolocationRequired = geolocationRequired;
        this.eventDescription = eventDescription;
        this.organizer = organizer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create_event, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        dialogState = 1; // initial state of the fragment

        frameLayout = rootView.findViewById(R.id.create_event_frameLayout);
        dialogStateSwitch();

        // setting up buttons
        Button nextButton = rootView.findViewById(R.id.create_event_positive_button);
        Button backButton = rootView.findViewById(R.id.create_event_negative_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStateGetInfo();
                dialogState += 1;
                dialogStateSwitch();
                if (dialogState > 1) {
                    backButton.setText("Go back");
                }
                if (dialogState == 3) {
                    nextButton.setText("Confirm");
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogState != 1) {
                    dialogStateGetInfo();
                }
                dialogState -= 1;
                dialogStateSwitch();
                if (dialogState == 1) {
                    backButton.setText("Cancel");
                }
                if (dialogState < 3) {
                    nextButton.setText("Next");
                }
            }
        });

        builder.setView(rootView);
        return builder.create();
    }

    // Adapted from https://stackoverflow.com/questions/25737817/using-a-single-fragment-with-multiple-layout-in-android
    private void dialogStateSwitch() {
        assert (0 <= dialogState) && (dialogState <= 4);
        LayoutInflater inflater = getLayoutInflater();

        // switch out view
        frameLayout.removeAllViews();
        switch (dialogState) {
            case 0: // user selects "cancel"
                dismiss();
                break;
            case 1: // switch UI to first page of the dialog
                stateView = inflater.inflate(R.layout.fragment_create_event_1, frameLayout);
                EditText eventTitleEditText = stateView.findViewById(R.id.create_event_edittext_event_title);
                eventTitleEditText.setText(eventTitle);
                break;
            case 2: // switch UI to second page of the dialog
                stateView = inflater.inflate(R.layout.fragment_create_event_2, frameLayout);
                EditText capacityEditText = stateView.findViewById(R.id.create_event_edittext_event_capacity);
                EditText waitListLimitEditText = stateView.findViewById(R.id.create_event_edittext_event_waitlist_limit);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);
                capacityEditText.setText(capacity.toString());
                waitListLimitEditText.setText(waitListLimit.toString());
                geolocationRequiredSwitch.setChecked(geolocationRequired);
                break;
            case 3: // switch UI to third page of the dialog
                stateView = inflater.inflate(R.layout.fragment_create_event_3, frameLayout);
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                eventDescriptionEditText.setText(eventDescription);
                break;
            case 4: // user selects "confirm"
                // pass info to database
                dismiss();
                break;
        }
    }

    private void dialogStateGetInfo() {
        assert (1 <= dialogState) && (dialogState <= 3);
        switch (dialogState) {
            case 1: // get input from state 1
                EditText eventTitleEditText = stateView.findViewById(R.id.create_event_edittext_event_title);
                eventTitle = eventTitleEditText.getText().toString();
                break;
            case 2: // get input from state 2
                EditText capacityEditText = stateView.findViewById(R.id.create_event_edittext_event_capacity);
                EditText waitListLimitEditText = stateView.findViewById(R.id.create_event_edittext_event_waitlist_limit);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);
                capacity = Integer.parseInt(capacityEditText.getText().toString());
                waitListLimit = Integer.parseInt(waitListLimitEditText.getText().toString());
                geolocationRequired = geolocationRequiredSwitch.isChecked();
                break;
            case 3: // get input from state 3
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                eventDescription = eventDescriptionEditText.getText().toString();
                break;
        }
    }
}