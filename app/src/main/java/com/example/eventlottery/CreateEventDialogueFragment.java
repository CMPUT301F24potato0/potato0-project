package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class CreateEventDialogueFragment extends DialogFragment {

    private FrameLayout frameLayout;
    private Integer dialogState;
    private View stateView;
    private DatePickerDialog datePickerDialog;

    private String eventTitle;
    private Integer capacity;
    private Integer waitListLimit;
    private Date joinDeadline;
    private String strLocation;
    private Boolean geolocationRequired;
    private String eventDescription;

    private CurrentUser organizer;
    private FirebaseFirestore db;

    public CreateEventDialogueFragment() {
        // initial values
        eventTitle = "";
        capacity = 0;
        waitListLimit = 0;
        joinDeadline = new Date();
        strLocation = "";
        geolocationRequired = Boolean.FALSE;
        eventDescription = "";
    }

    // for creating a new event
    public CreateEventDialogueFragment(CurrentUser organizer, FirebaseFirestore db) {
        this();
        this.organizer = organizer;
        this.db = db;
    }

    // for editing an existing event
    public CreateEventDialogueFragment(String eventTitle,
                                       Integer capacity,
                                       Integer waitListLimit,
                                       Date joinDeadline,
                                       String strLocation,
                                       Boolean geolocationRequired,
                                       String eventDescription,
                                       CurrentUser organizer,
                                       FirebaseFirestore db) {
        this(organizer, db);
        this.eventTitle = eventTitle;
        this.capacity = capacity;
        this.waitListLimit = waitListLimit;
        this.joinDeadline = joinDeadline;
        this.strLocation = strLocation;
        this.geolocationRequired = geolocationRequired;
        this.eventDescription = eventDescription;
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
                EditText strLocationEditText = stateView.findViewById(R.id.create_event_edittext_event_location);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);
                Button joinDeadlineButton = stateView.findViewById(R.id.create_event_join_deadline_button);
                capacityEditText.setText(capacity.toString());
                waitListLimitEditText.setText(waitListLimit.toString());
                strLocation = strLocationEditText.getText().toString();
                geolocationRequiredSwitch.setChecked(geolocationRequired);
                joinDeadlineButton.setText(getTodaysDate()); // TODO: make it so it is initially today's date, then last picked date for saved data
                break;
            case 3: // switch UI to third page of the dialog
                stateView = inflater.inflate(R.layout.fragment_create_event_3, frameLayout);
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                eventDescriptionEditText.setText(eventDescription);
                break;
            case 4: // user selects "confirm"
                // pass info to database
                EventModel event = new EventModel(
                        organizer.getFacilityID(),
                        geolocationRequired,
                        capacity,
                        joinDeadline,
                        strLocation,
                        eventTitle,
                        eventDescription
                );
                Task<DocumentReference> eventRef = db.collection("events").add(event);
                eventRef.addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            String eventID = documentReference.getId();
                            event.setEventID(eventID);
                            db.collection("events").document(eventID).update("eventID", eventID);
                        }
                    }
                });
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
                EditText strLocationEditText = stateView.findViewById(R.id.create_event_edittext_event_location);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);
                Button joinDeadlineButton = stateView.findViewById(R.id.create_event_join_deadline_button);
                capacity = Integer.parseInt(capacityEditText.getText().toString());
                waitListLimit = Integer.parseInt(waitListLimitEditText.getText().toString());
                strLocation = strLocationEditText.getText().toString();
                geolocationRequired = geolocationRequiredSwitch.isChecked();
                // TODO: figure out how to implement date picker and get its output
                initDatePicker(joinDeadlineButton);
                joinDeadline = new Date();
                break;
            case 3: // get input from state 3
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                eventDescription = eventDescriptionEditText.getText().toString();
                break;
        }
    }


    // https://youtu.be/qCoidM98zNk?si=1rTgJIFOLwVypGbi
    private void initDatePicker(Button joinDeadlineButton) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String joinDeadlineStr = getMonth(month) + " " + day + ", " + year;
                joinDeadlineButton.setText(joinDeadlineStr);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.R.style.Theme_Material_Light_Dialog_Alert;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return getMonth(month) + " " + day + ", " + year;
    }

    private String getMonth(int month) {
        String monthStr = "";
        switch(month) {
            case 1:
                monthStr = "Jan.";
                break;
            case 2:
                monthStr = "Feb.";
                break;
            case 3:
                monthStr = "Mar.";
                break;
            case 4:
                monthStr = "Apr.";
                break;
            case 5:
                monthStr = "May";
                break;
            case 6:
                monthStr = "June";
                break;
            case 7:
                monthStr = "July";
                break;
            case 8:
                monthStr = "Aug.";
                break;
            case 9:
                monthStr = "Sep.";
                break;
            case 10:
                monthStr = "Oct.";
                break;
            case 11:
                monthStr = "Nov.";
                break;
            case 12:
                monthStr = "Dec.";
                break;
        }
        return monthStr;
    }

    // https://youtu.be/qCoidM98zNk?si=Ax2uZXrxNyzHTL44
    public void joinDeadlinePicker(View view) {
        datePickerDialog.show();
    }
}