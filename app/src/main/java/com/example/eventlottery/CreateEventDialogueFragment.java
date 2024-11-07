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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

/**
 * Create Event Dialogue Fragment
 */
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

    private boolean isEditing = false;  // Flag to determine if we are editing an event
    private String eventId;  // Store event ID if editing

    /**
     * Default constructor for creating a new event
     */
    // Default constructor for creating a new event
    public CreateEventDialogueFragment() {
        initializeDefaults();
    }

    /**
     * Constructor for creating a new event with organizer and database references
     * @param organizer The organizer of the event
     * @param db The Firebase Firestore database
     */
    // Constructor for creating a new event with organizer and database references
    public CreateEventDialogueFragment(CurrentUser organizer, FirebaseFirestore db) {
        this();
        this.organizer = organizer;
        this.db = db;
    }

    /**
     * Constructor for editing an existing event
     * @param eventId The ID of the event to be edited
     * @param eventTitle The title of the event
     * @param capacity The capacity of the event
     * @param waitListLimit The waitlist limit of the event
     * @param joinDeadline The deadline for joining the event
     * @param strLocation The location of the event
     * @param geolocationRequired Whether geolocation is required for the event
     * @param eventDescription The description of the event
     * @param organizer The organizer of the event
     * @param db The Firebase Firestore database
     */
    // Constructor for editing an existing event
    public CreateEventDialogueFragment(String eventId,
                                       String eventTitle,
                                       Integer capacity,
                                       Integer waitListLimit,
                                       Date joinDeadline,
                                       String strLocation,
                                       Boolean geolocationRequired,
                                       String eventDescription,
                                       CurrentUser organizer,
                                       FirebaseFirestore db) {
        this(organizer, db);
        this.isEditing = true;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.capacity = capacity;
        this.waitListLimit = waitListLimit;
        this.joinDeadline = joinDeadline;
        this.strLocation = strLocation;
        this.geolocationRequired = geolocationRequired;
        this.eventDescription = eventDescription;
    }

    /**
     * Initialize default values for the event
     */
    private void initializeDefaults() {
        eventTitle = "";
        capacity = 0;
        waitListLimit = -1;
        joinDeadline = new Date();
        strLocation = "";
        geolocationRequired = Boolean.FALSE;
        eventDescription = "";
    }

    /**
     * Called when creating the dialog fragment.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return A new Dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create_event, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        dialogState = 1;

        frameLayout = rootView.findViewById(R.id.create_event_frameLayout);
        dialogStateSwitch();

        // Setting up buttons
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
                        nextButton.setText(isEditing ? "Update" : "Confirm");
                    }
                } else {
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
     * Switch between dialog states
     */
    private void dialogStateSwitch() {
        assert (0 <= dialogState) && (dialogState <= 4);
        LayoutInflater inflater = getLayoutInflater();

        frameLayout.removeAllViews();
        switch (dialogState) {
            case 0:
                dismiss();
                break;
            case 1:
                stateView = inflater.inflate(R.layout.fragment_create_event_1, frameLayout);
                EditText eventTitleEditText = stateView.findViewById(R.id.create_event_edittext_event_title);
                eventTitleEditText.setText(eventTitle);
                break;
            case 2:
                stateView = inflater.inflate(R.layout.fragment_create_event_2, frameLayout);
                EditText capacityEditText = stateView.findViewById(R.id.create_event_edittext_event_capacity);
                EditText waitListLimitEditText = stateView.findViewById(R.id.create_event_edittext_event_waitlist_limit);
                EditText strLocationEditText = stateView.findViewById(R.id.create_event_edittext_event_location);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);
                Button joinDeadlineButton = stateView.findViewById(R.id.create_event_join_deadline_button);

                capacityEditText.setText(capacity.toString());
                waitListLimitEditText.setText(waitListLimit.equals(-1) ? "" : waitListLimit.toString());
                strLocationEditText.setText(strLocation);
                geolocationRequiredSwitch.setChecked(geolocationRequired);

                Calendar cal = Calendar.getInstance();
                cal.setTime(joinDeadline);
                initDatePicker(joinDeadlineButton, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                joinDeadlineButton.setOnClickListener(v -> datePickerDialog.show());
                break;
            case 3:
                stateView = inflater.inflate(R.layout.fragment_create_event_3, frameLayout);
                EditText eventDescriptionEditText = stateView.findViewById(R.id.create_event_edittext_event_description);
                eventDescriptionEditText.setText(eventDescription);
                break;
            case 4:
                if (isEditing) {
                    updateEventInDatabase();
                } else {
                    createEventInDatabase();
                }
                dismiss();
                break;
        }
    }

    /**
     * Create event in database
     */
    private void createEventInDatabase() {
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

        db.collection("events").add(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentReference docRef = task.getResult();
                event.setEventID(docRef.getId());
                db.collection("events").document(docRef.getId()).set(event);
            }
        });
    }

    /**
     * Update event in database
     */
    private void updateEventInDatabase() {
        db.collection("events").document(eventId).update(
                "eventTitle", eventTitle,
                "capacity", capacity,
                "waitListLimit", waitListLimit,
                "joinDeadline", joinDeadline,
                "strLocation", strLocation,
                "geolocationRequired", geolocationRequired,
                "eventDescription", eventDescription
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Event updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Get information from the current dialog state
     * @return Whether the information is valid
     */
    private Boolean dialogStateGetInfo() {
        assert (1 <= dialogState) && (dialogState <= 3);
        Boolean validInput = Boolean.FALSE;
        switch (dialogState) {
            case 1:
                EditText eventTitleEditText = stateView.findViewById(R.id.create_event_edittext_event_title);
                if (isValidString(eventTitleEditText.getText().toString())) {
                    eventTitle = eventTitleEditText.getText().toString();
                    validInput = Boolean.TRUE;
                }
                break;
            case 2:
                EditText capacityEditText = stateView.findViewById(R.id.create_event_edittext_event_capacity);
                EditText waitListLimitEditText = stateView.findViewById(R.id.create_event_edittext_event_waitlist_limit);
                EditText strLocationEditText = stateView.findViewById(R.id.create_event_edittext_event_location);
                Switch geolocationRequiredSwitch = stateView.findViewById(R.id.create_event_switch_geolocation_required);

                String capacity_before_validation = capacityEditText.getText().toString();
                String waitListLimit_before_validation = waitListLimitEditText.getText().toString();
                String strLocation_before_validation = strLocationEditText.getText().toString();

                if (isValidNumber(capacity_before_validation) && isValidString(strLocation_before_validation)) {
                    if (isValidNumber(waitListLimit_before_validation)) {
                        waitListLimit = Integer.parseInt(waitListLimit_before_validation);
                    } else if (waitListLimit_before_validation.equals("")) {
                        waitListLimit = -1;
                    } else {
                        break;
                    }
                    capacity = Integer.parseInt(capacity_before_validation);
                    strLocation = strLocation_before_validation;
                    geolocationRequired = geolocationRequiredSwitch.isChecked();
                    validInput = Boolean.TRUE;
                }
                break;
            case 3:
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
     * Initialize date picker
     * @param joinDeadlineButton The button to display the date picker
     * @param year The year of the date
     * @param month The month of the date
     * @param day The day of the date
     */
    private void initDatePicker(Button joinDeadlineButton, int year, int month, int day) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                joinDeadline = cal.getTime();
                joinDeadlineButton.setText(getMonth(month + 1) + " " + day + ", " + year);
            }
        };

        int style = android.R.style.Theme_Material_Dialog_Alert;
        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    /**
     * Get month name
     * @param month The month
     * @return The month name
     */
    private String getMonth(int month) {
        switch (month) {
            case 1: return "Jan.";
            case 2: return "Feb.";
            case 3: return "Mar.";
            case 4: return "Apr.";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "Aug.";
            case 9: return "Sep.";
            case 10: return "Oct.";
            case 11: return "Nov.";
            case 12: return "Dec.";
        }
        return "";
    }

    /**
     * Check if string is valid
     * @param str The string to check
     * @return Whether the string is valid
     */
    private Boolean isValidString(String str) {
        return !str.equals("");
    }

    /**
     * Check if string is a valid number
     * @param str The string to check
     * @return Whether the string is valid
     */
    private Boolean isValidNumber(String str) {
        if (!isValidString(str)) {
            return Boolean.FALSE;
        }
        try {
            return Integer.parseInt(str) >= 0;
        } catch (NumberFormatException e) {
            return Boolean.FALSE;
        }
    }
}
