package com.example.eventlottery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import org.w3c.dom.Document;

import java.util.Date;
import java.util.List;

/**
 * Event Entrant Activity
 */
public class EventEntrantActivity extends AppCompatActivity {
    FloatingActionButton back;
    FirebaseFirestore db;

    private TextView organizerName;
    private DocumentReference facilityRef;
    private String organizer;
    // Event stuff and user stuff
    private UsersList userList;
    private EventModel event;
    // XML stuff
    private TextView eventDescription;
    private ImageView eventPoster;
    private TextView eventTitle;
    private TextView eventLocation;
    private TextView eventDate;
    private Button joinBtn;
    private Button unjoinBtn;
    private ImageView organizerProfilePicture;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    /**
     * Overriding on back pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(EventEntrantActivity.this, MainActivity.class);
        startActivity(i);
    }
    // NEED TO TEST THIS
    // ****************************************************************************************************************

    /**
     * Request permission launcher
     */
     private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    Log.e("Permission","Granted");
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Log.e("Permission","Not granted");
                }
            });

    /**
     * Ask notification permission
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.

            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    // ****************************************************************************************************************
    CurrentUser tempCurUser;
    CurrentUser tempTesting;
    /**
     * On create of the View
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_entrant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            event = (EventModel) extras.getSerializable("eventModel");
            userList = (UsersList) extras.getSerializable("userList");
        }

        db = FirebaseFirestore.getInstance();

        eventPoster = findViewById(R.id.event_entrant_page_event_poster1);
        eventTitle = findViewById(R.id.event_entrant_page_event_title1);
        eventLocation = findViewById(R.id.event_entrant_page_event_location1);
        eventDate = findViewById(R.id.event_entrant_page_event_date1);
        joinBtn = findViewById(R.id.event_entrant_page_join_button1);
        unjoinBtn = findViewById(R.id.event_entrant_page_unjoin_button1);
        organizerProfilePicture = findViewById(R.id.event_entrant_page_profile_picture1);
        organizerName = findViewById(R.id.event_entrant_page_organizer_name1);
        eventDescription = findViewById(R.id.event_entrant_page_event_details1);

        progressBar = findViewById(R.id.progressBar1);
        linearLayout = findViewById(R.id.linearLayout1);

        back = findViewById(R.id.floatingActionButton);

        update();

        // getting event information from Firestore
        final DocumentReference eventRef = db.collection("events").document(event.getEventID());
        eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("EventEntrantActivity", error.toString());
                }
                if (value != null) {
                    event = value.toObject(EventModel.class);
                    update();
                }
            }
        });

        progressBar.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        if (event.checkUserInList(userList, event.getWaitingList())) {
            unjoinBtn.setVisibility(View.VISIBLE);
            joinBtn.setVisibility(View.GONE);
            unjoinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Unjoining
                    try {
                        event.unqueueWaitingList(userList);
                        db.collection("events").document(event.getEventID()).set(event);


                        String eventID = event.getEventID();
                        String userID = userList.getiD();
                        String topic = eventID + "_" + userID;


                        Task<DocumentSnapshot> task = db.collection("users").document(userID).get();
                        task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    tempCurUser = documentSnapshot.toObject(CurrentUser.class);
                                    tempCurUser.removeTopics(topic);
                                    db.collection("users").document(userID).set(tempCurUser);
                                }
                            }
                        });


                        UnsubscribeFromTopic unsubscribeFromTopic = new UnsubscribeFromTopic(topic, getApplicationContext());
                        unsubscribeFromTopic.unsubscribe();
                    }
                    catch (Exception e) {
                        Toast.makeText(EventEntrantActivity.this, "This user is not in the waiting list!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            unjoinBtn.setVisibility(View.GONE);
            joinBtn.setVisibility(View.VISIBLE);
            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askNotificationPermission();
                    if (event.getGeolocationRequired()) {
                        // Joining
                        new geo_requirement_dialog(userList, event, db).show(getSupportFragmentManager(), "geo_requirement_dialog");
                        // Getting event specific topic
                    }
                    else {

                        try {
                            event.queueWaitingList(userList);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Task<DocumentSnapshot> task = db.collection("users").document(userList.getiD()).get();
                        task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    tempTesting = documentSnapshot.toObject(CurrentUser.class);
                                }
                            }
                        });
                        db.collection("events").document(event.getEventID()).set(event);
                        task.onSuccessTask(t1 -> {
                            tempTesting.addTopics(event.getEventID() + "_" + userList.getiD());
                            db.collection("users").document(userList.getiD()).set(tempTesting);
                            return null;
                        });
                    }
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventEntrantActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void update(){
        eventTitle.setText(event.getEventTitle());
        eventLocation.setText(event.getEventStrLocation());
        Date javaDate = event.getJoinDeadline();
        eventDate.setText(javaDate.toString());
        organizer = event.getOrganizer();
        organizerName.setText(organizer);
        eventDescription.setText(event.getEventDescription());
    }
}