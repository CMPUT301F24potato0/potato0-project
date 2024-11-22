package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
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
    private EventModel event;
    private EventOrganizerActivity eventActivity;

    /** Empty constructor
     */
    public CreateEventDialogueFragment() {
        // initial values
        eventTitle = "";
        capacity = 0;
        waitListLimit = -1;
        joinDeadline = new Date();
        strLocation = "";
        geolocationRequired = Boolean.FALSE;
        eventDescription = "";
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background);
    }

    /**
     * Constructor
     * @param organizer The organizer
     * @param db The database
     */
    // for creating a new event (organizer information required)
    public CreateEventDialogueFragment(CurrentUser organizer, FirebaseFirestore db) {
        this();
        this.organizer = organizer;
        this.event = null;
        this.db = db;
    }

    /**
     * Constructor
     * @param event The event
     * @param db The database
     * @param eventActivity A reference to the event activity
     */
    // for editing an existing event (event information required, organizer information not required)
    public CreateEventDialogueFragment(EventModel event, FirebaseFirestore db, EventOrganizerActivity eventActivity) {
        this.eventTitle = event.getEventTitle();
        this.capacity = event.getCapacity();
        this.waitListLimit = event.getWaitingListLimit();
        this.joinDeadline = event.getJoinDeadline();
        this.strLocation = event.getEventStrLocation();
        this.geolocationRequired = event.getGeolocationRequired();
        this.eventDescription = event.getEventDescription();
        this.organizer = null;
        this.event = event;
        this.db = db;
        this.eventActivity = eventActivity;
    }

    /**
     * On create override
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the view
     */
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
                Boolean validInput = dialogStateGetInfo();
                if (validInput) {
                    dialogState += 1;
                    dialogStateSwitch();
                    if (dialogState > 1) {
                        backButton.setText("Go back");
                    }
                    if (dialogState == 3) {
                        nextButton.setText("Confirm");
                    }
                }
                else {
                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
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

    /**
     * from https://stackoverflow.com/questions/25737817/using-a-single-fragment-with-multiple-layout-in-android
     */
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
                if (waitListLimit.equals(-1)) {
                    waitListLimitEditText.setText("");
                }
                else {
                    waitListLimitEditText.setText(waitListLimit.toString());
                }
                strLocationEditText.setText(strLocation);
                geolocationRequiredSwitch.setChecked(geolocationRequired);
                if (joinDeadline == null) {
                    joinDeadline = new Date();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(joinDeadline);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String dateStr = getMonth(month + 1) + " " + day + ", " + year;
                joinDeadlineButton.setText(dateStr);
                initDatePicker(joinDeadlineButton, year, month, day);
                joinDeadlineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datePickerDialog.show();
                    }
                });
                break;
            case 3: // switch UI to third page of the dialog
                stateView = inflater.inflate(R.layout.fragment_create_event_3, frameLayout);
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                eventDescriptionEditText.setText(eventDescription);
                break;
            case 4: // user selects "confirm"
                // pass info to database
                if (event == null && organizer != null) {  // creating a new event
                    EventModel event = new EventModel(
                            organizer.getFacilityID(),
                            geolocationRequired,
                            waitListLimit,
                            capacity,
                            joinDeadline,
                            strLocation,
                            eventTitle,
                            eventDescription,
                            organizer.getfName() + " " + organizer.getlName()
                    );
                    db.collection("events").add(event).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                DocumentReference documentReference = task.getResult();
                                String eventID = documentReference.getId();
                                event.setEventID(eventID);
                                db.collection("events").document(eventID).set(event);
                            }
                        }
                    });
                }
                else {  // editing (updating) an existing event)
                    event.setEventTitle(eventTitle);
                    event.setCapacity(capacity);
                    event.setWaitingListLimit(waitListLimit);
                    event.setEventStrLocation(strLocation);
                    event.setGeolocationRequired(geolocationRequired);
                    event.setJoinDeadline(joinDeadline);
                    event.setEventDescription(eventDescription);
                    db.collection("events").document(event.getEventID()).set(event);
                    eventActivity.updateViews();
                }
                dismiss();
                break;
        }
    }

    /**
     * Get info from the dialog
     * @return true if valid input, false otherwise
     */
    private Boolean dialogStateGetInfo() {
        assert (1 <= dialogState) && (dialogState <= 3);
        Boolean validInput = Boolean.FALSE;
        switch (dialogState) {
            case 1: // get input from state 1
                EditText eventTitleEditText = stateView.findViewById(R.id.create_event_edittext_event_title);
                if (isValidString(eventTitleEditText.getText().toString())) {
                    eventTitle = eventTitleEditText.getText().toString();
                    validInput = Boolean.TRUE;
                }
                break;
            case 2: // get input from state 2
                EditText capacityEditText = stateView.findViewById(R.id.create_event_edittext_event_capacity);
                EditText waitListLimitEditText = stateView.findViewById(R.id.create_event_edittext_event_waitlist_limit);
                EditText strLocationEditText = stateView.findViewById(R.id.create_event_edittext_event_location);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);
                Button joinDeadlineButton = stateView.findViewById(R.id.create_event_join_deadline_button);

                String capacity_before_validation = capacityEditText.getText().toString();
                String waitListLimit_before_validation = waitListLimitEditText.getText().toString();
                String strLocation_before_validation = strLocationEditText.getText().toString();

                if (isValidNumber(capacity_before_validation) && isValidString(strLocation_before_validation)) {
                    if (isValidNumber(waitListLimit_before_validation)) {
                        waitListLimit = Integer.parseInt(waitListLimit_before_validation);
                    }
                    else if (waitListLimit_before_validation.equals("")) {
                        waitListLimit = -1; // default value for no limit on waiting list
                    }
                    else {
                        break;
                    }
                    capacity = Integer.parseInt(capacity_before_validation);
                    strLocation = strLocation_before_validation;
                    geolocationRequired = geolocationRequiredSwitch.isChecked();
                    // joinDeadline's value is set from inside initDatePicker onDateSet
                    validInput = Boolean.TRUE;
                }
                break;
            case 3: // get input from state 3
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                if (isValidString(eventDescriptionEditText.getText().toString())) {
                    eventDescription = eventDescriptionEditText.getText().toString();
                    validInput = Boolean.TRUE;
                }
                break;
        }
        return validInput;
    }

    /**
     * From https://youtu.be/qCoidM98zNk?si=1rTgJIFOLwVypGbi
     * @param joinDeadlineButton Button to set the date on
     * @param year Year
     * @param month Month
     * @param day day
     */
    private void initDatePicker(Button joinDeadlineButton, int year, int month, int day) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // https://stackoverflow.com/questions/30494687/java-util-dateint-int-int-deprecated
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                joinDeadline = cal.getTime();
                month += 1;
                String joinDeadlineStr = getMonth(month) + " " + day + ", " + year;
                joinDeadlineButton.setText(joinDeadlineStr);
            }
        };

        int style =  android.R.style.Theme_Material_Dialog_Alert;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    /**
     * https://youtu.be/qCoidM98zNk?si=1rTgJIFOLwVypGbi
     * @param month Month
     * @return String representation of the month
     */
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

    /**
     * Checks to make sure the string is not empty
     * @param str passed string
     * @return true if not empty, false otherwise
     */
    private Boolean isValidString(String str) {
        // checks to make sure it is not an empty string
        return !str.equals("");
    }

    /**
     * Checks to make sure the string is a non negative number
     * @param str passed string
     * @return true if non negative number, false otherwise
     */
    private Boolean isValidNumber(String str) {
        // checks to make sure it is a nonnegative number
        if (!isValidString(str)) {
            return Boolean.FALSE;
        }
        try {
            Integer number = Integer.parseInt(str);
            if (number < 0) {
                return Boolean.FALSE;
            }
        }
        catch(NumberFormatException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}