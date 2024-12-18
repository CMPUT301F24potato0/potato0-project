package com.example.eventlottery.Organizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *This class creates a dialogue fragment where the organizer can input information to create an
 * event, this information is then collected to the Firebase so it can be use in other places
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

    private UserModel organizer;
    private FacilityModel facility;
    private FirebaseFirestore db;
    private EventModel event;
    private EventOrganizerActivity eventActivity;
    private ImageView poster;
    private Boolean uploaded = false;

    private HashMap<String, Object> temp_hashmap = null;
    private Bitmap temp_bitmap = null;
    private Boolean fileTooLarge = false;

    /** Empty constructor
     */
    public CreateEventDialogueFragment() {
        // initial values
        eventTitle = "";
        capacity = 0;
        waitListLimit = -1;
        joinDeadline = createCalendarTodayMidnight().getTime();
        strLocation = "";
        geolocationRequired = Boolean.FALSE;
        eventDescription = "";
    }

    /**
     * It personalize the color of the button and background of the Dialog when the Dialog is created
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
     * Constructor of CreateEventDialogueFragment
     * @param organizer   The organizer of the event
     * @param facility    The facility that the event takes places
     * @param db          The firebase that holds the information
     */
    // for creating a new event (organizer information required)
    public CreateEventDialogueFragment(UserModel organizer, FacilityModel facility, FirebaseFirestore db) {
        this();
        this.organizer = organizer;
        this.facility = facility;
        this.event = null;
        this.db = db;
    }

    /**
     * Constructor
     * @param event The event
     * @param facility The facility
     * @param db The database
     * @param eventActivity A reference to the event activity
     */
    // for editing an existing event (event information required, organizer information not required)
    public CreateEventDialogueFragment(EventModel event, FacilityModel facility, FirebaseFirestore db, EventOrganizerActivity eventActivity) {
        this.eventTitle = event.getEventTitle();
        this.facility = facility;
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
     * This method create the dialogue view so the user can interact with it
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the view created
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
     *
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
                poster = stateView.findViewById(R.id.poster);
                // Testing
                // ************************************************************************************************************************
                if (event != null) {
                    // editing event
                    if (temp_bitmap != null){
                        // selected a new poster
                        poster.setImageBitmap(temp_bitmap);
                    }
                    else{
                        // shows current poster
                        DocumentReference posterReference = db.collection("posters").document(event.getEventID());
                        posterReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Log.e("Document","checking document existence");
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if(document.exists()){
                                        // document exists
                                        Log.e("Document", "exists");
                                        Blob blob = document.getBlob("Blob");
                                        byte[] bytes = blob.toBytes();
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                        poster.setImageBitmap(bitmap);
                                        uploaded = true;
                                    } else {
                                        // deleted poster
                                        // letting the user choose whether he wants to add a poster or not when editing
                                        uploaded = true;
                                    }
                                }
                            }
                        });
                    }
                }else{
                    // creating a new event
                    if (temp_bitmap != null){
                        // selected a new poster
                        poster.setImageBitmap(temp_bitmap);
                    }
                }

                Button add_poster = stateView.findViewById(R.id.add_poster);

                add_poster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Clicked Upload Image","Choosing image");
                        imageChoose();
                    }
                });
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
                if (event != null && organizer == null) {  // if editing an event
                    geolocationRequiredSwitch.setEnabled(Boolean.FALSE);  // disable changing geolocation requirement
                    if (event.getWaitingListLimit().equals(-1)) { // if no limit to waitlist, disable changing it
                        waitListLimitEditText.setHint("No limit");
                        waitListLimitEditText.setEnabled(Boolean.FALSE);
                    }
                }
                strLocationEditText.setText(strLocation);
                geolocationRequiredSwitch.setChecked(geolocationRequired);
                if (joinDeadline == null) {
                    joinDeadline = createCalendarTodayMidnight().getTime();
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
                                Log.e("EventID","***************");
                                Log.e("EventID",eventID);
                                Log.e("EventID",eventID);
                                Log.e("EventID",eventID);
                                Log.e("EventID","***************");
                                event.setEventID(eventID);
                                db.collection("events").document(eventID).set(event);

                                if(temp_bitmap != null){
                                    Log.e("Saving","Saving new image");
                                    db.collection("posters").document(event.getEventID()).set(temp_hashmap);
                                }
                                dismiss();
                            }
                        }
                    });


                    dismiss();
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
                    // ***
                    if (temp_hashmap != null){
                        db.collection("posters").document(event.getEventID()).set(temp_hashmap);

                    }
//                    db.collection("posters").document("default").set(temp_hashmap);
                    // ***
                    eventActivity.updateViews();
                    dismiss();
                }
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
                // if poster has been uploaded
                EditText eventTitleEditText = stateView.findViewById(R.id.create_event_edittext_event_title);

                if (isValidString(eventTitleEditText.getText().toString())) {
                    eventTitle = eventTitleEditText.getText().toString();
                    validInput = Boolean.TRUE;
                }
                // doesn't force user to upload a poster
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

                // if no number for capacity or empty string for location, do not allow user to proceed
                if (!isValidNumber(capacity_before_validation) || !isValidString(strLocation_before_validation)) {
                    break;
                }
                // waitlist limit can only either be a valid number or empty
                if (!(isValidNumber(waitListLimit_before_validation) || waitListLimit_before_validation.equals(""))) {
                    break;
                }
                // after the basic input checks, parse the numbers into Integer objects
                Integer newCapacity = Integer.parseInt(capacity_before_validation);
                Integer newWaitlistLimit = -1;  // assume initially no limit
                if (!waitListLimit_before_validation.equals("")) {
                    newWaitlistLimit = Integer.parseInt(waitListLimit_before_validation);
                }

                // making sure the new capacity and waiting list limits are valid
                if (!isValidNewLimits(newCapacity, newWaitlistLimit)) {
                    break;
                }

                capacity = newCapacity;
                waitListLimit = newWaitlistLimit;
                strLocation = strLocation_before_validation;
                geolocationRequired = geolocationRequiredSwitch.isChecked();
                // joinDeadline's value is set from inside initDatePicker onDateSet
                // joinDeadline also does not need validation because it restricts input values already
                validInput = Boolean.TRUE;
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
     * This method get the date from a calendar that is going to be use as the deadline to join
     * the event
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
                cal.set(Calendar.HOUR, 11);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                cal.set(Calendar.AM_PM, Calendar.PM);
                joinDeadline = cal.getTime();
                month += 1;
                String joinDeadlineStr = getMonth(month) + " " + day + ", " + year;
                joinDeadlineButton.setText(joinDeadlineStr);
            }
        };

        int style =  android.R.style.Theme_Material_Dialog_Alert;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
        Calendar cal = createCalendarTodayMidnight();
        datePickerDialog.getDatePicker().setMinDate(cal.getTime().getTime());
    }

    /**
     * A helper function that creates a Calendar instance that is set to the current date, and the time is set at 11:59:59PM
     * @return The Calendar object representing the current date just before midnight
     */
    private Calendar createCalendarTodayMidnight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 11);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.set(Calendar.AM_PM, Calendar.PM);
        return cal;
    }

    /**
     * https://youtu.be/qCoidM98zNk?si=1rTgJIFOLwVypGbi
     * This method transform the month that is number to words
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
            int number = Integer.parseInt(str);
            if (number < 0) {
                return Boolean.FALSE;
            }
        }
        catch(NumberFormatException e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Validates both the capacity and waiting list limit inputted by the organizer
     * @param newCapacity The new capacity
     * @param newWaitlistLimit The new waiting list limit
     * @return True if both capacity and waiting list limit are valid, false otherwise
     */
    private Boolean isValidNewLimits(Integer newCapacity, Integer newWaitlistLimit) {
        if (newCapacity > newWaitlistLimit && !newWaitlistLimit.equals(-1)) {
            // compares new capacity to new waitlist limit if there is a specified waitlist limit (not limitless)
            Toast.makeText(getActivity(), "Waiting list limit must be at least the capacity", Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        }
        if (!isValidNewCapacity(newCapacity)) {
            return Boolean.FALSE;
        }
        if (!isValidNewWaitlistLimit(newWaitlistLimit)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Validates that the new capacity is valid. This function ensures that the facility can accommodate
     * the event's capacity, and that the organizer can only edit the capacity before the join deadline
     * @param newCapacity Integer representing the new capacity
     * @return true if the new capacity is valid, false otherwise
     */
    private Boolean isValidNewCapacity(Integer newCapacity) {
        // Event capacity must be at most the facility's capacity
        if (newCapacity > facility.getCapacity()) {
            Toast.makeText(getActivity(), "Event capacity cannot be greater than the facility capacity of " + facility.getCapacity(), Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        }

        // When creating an event, simply return true (there is nothing else to compare)
        if (event == null && organizer != null) {
            return Boolean.TRUE;
        }

        // Otherwise, when editing an event
        // Event capacity must be at least the number of people enrolled, invited and chosen by the lottery system
        Integer minCapacity = event.getEnrolledList().size() + event.getInvitedList().size() + event.getChosenList().size();
        if (newCapacity < minCapacity) {
            Toast.makeText(getActivity(), "Event capacity must be at least " + minCapacity, Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        }

        // Make sure organizer can only edit the capacity before the join deadline
        Date currentDate = new Date();
        if (currentDate.before(event.getJoinDeadline())) {
            return Boolean.TRUE;
        }
        Toast.makeText(getActivity(), "Cannot edit the event capacity after the join deadline has passed.", Toast.LENGTH_SHORT).show();
        return Boolean.FALSE;
    }

    /**
     * Validates that the new waiting list limit is valid. This function ensures that when organizer is editing
     * the event, they can only increase the waiting list limit.
     * @param newWaitlistLimit Integer representing the new waitlist limit
     * @return true if the new waitlist limit is valid, false otherwise
     */
    private Boolean isValidNewWaitlistLimit(Integer newWaitlistLimit) {
        // When creating a new event, simply return True (there is no previous limit to compare to)
        if (event == null && organizer != null) {
            return Boolean.TRUE;
        }

        // Otherwise, when editing an event, only allow organizer to increase the waiting list limit
        if (newWaitlistLimit.equals(-1)) {  // return true when removing limit
            return Boolean.TRUE;
        }
        else if (newWaitlistLimit.compareTo(event.getWaitingListLimit()) >= 0) {  // new limit is greater than or equal to the old limit
            return Boolean.TRUE;
        }
        Toast.makeText(getActivity(), "Waiting list limit must be at least the previous limit of " + event.getWaitingListLimit(), Toast.LENGTH_SHORT).show();
        return Boolean.FALSE;
    }

    /**
     * choose a image of your gallery to be the event poster
     */
    public void imageChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageChooser.launch(intent);
    }
    ActivityResultLauncher<Intent> imageChooser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if(data != null && data.getData() != null){
                        Uri uri = data.getData();
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                            // initialize byte stream
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            byte[] bytes = new byte[0];
                            // ****************************************************************************************
                            final int targetSize = 1024 * 1024;
                            // Start with 100% quality, and reduce quality until we get under the target size
                            int quality = 100;
                            int compressedSize = 0;

                            // Compress the image and check the size after each compression


                            while (compressedSize > targetSize || quality >= 10){
                                if (compressedSize < targetSize && compressedSize > 0){
                                    // Stops when the JPEG size is just under 1 mb
                                    break;
                                }
                                stream.reset();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                                bytes = stream.toByteArray();
                                compressedSize = bytes.length;
                                if (compressedSize > 5000000){ // set size limit to 5mb for a picture
                                    Log.e("fileSize","fileTooLarge");
                                    Toast.makeText(getContext(), "File too large", Toast.LENGTH_LONG).show();
                                    fileTooLarge = true;
                                    break;
                                }

                                Log.e("Image compression","Compressed size: " + compressedSize + " bytes");
                                Log.e("Image quality", "Image qualit is: " + quality + "/100");
                                // Reduce the quality by 10% for the next iteration if the image is still too large
                                quality -= 10;
                                fileTooLarge = false;
                            }
                            Blob blob = Blob.fromBytes(bytes);

                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            hashMap.put("Blob",blob);
                            if (event == null){
                                // editing event
                                // saving temp imageSelected
                                // saving temp hashmap for db
                                temp_bitmap = bitmap;
                                temp_hashmap = hashMap;

                                // creating new event
                                Log.e("document","temp");
                                Log.e("document","editing event");
                                decode();
                            } else {
                                // creating event
                                Log.e("creating","Event");
                                temp_hashmap = hashMap;
                                temp_bitmap = bitmap;
                                decode();

                            }
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    public void decode(){
        if(!fileTooLarge){
            if (event == null){
                poster.setImageBitmap(temp_bitmap);
                uploaded = true;
            } else{
                // editing image
                if(temp_bitmap == null){
                    DocumentReference docref = db.collection("posters").document(event.getEventID());
//                    DocumentReference docref = db.collection("posters").document("default");
                    docref.get().addOnCompleteListener( task -> {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            Blob blob = document.getBlob("Blob");
                            byte[] bytes = blob.toBytes();
                            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                            poster.setImageBitmap(bitmap);
                            uploaded = true;
                        }
                        else{
//                            poster.setImageDrawable(getResources().getDrawable(R.drawable.defaultposter));
                            if (getContext() != null){
                                poster.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.defaultposter));
                            }
                        }
                    });
                } else{
                    poster.setImageBitmap(temp_bitmap);
                }
            }
        }

    }
}