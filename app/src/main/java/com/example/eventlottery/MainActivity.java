package com.example.eventlottery;

import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.eventlottery.Entrant.ProfileFragment;
import com.example.eventlottery.Entrant.ScanFragment;
import com.example.eventlottery.Entrant.WaitlistedEventsFragment;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Notifications.NotificationsFragment;
import com.example.eventlottery.Organizer.FacilityFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the MainActivity class
 * This class currently handles checking the user in the database, if the user exists then updating the current instance of the user
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    public FirebaseFirestore db;
    public CollectionReference usersRef;
    public CollectionReference facilitiesRef;
    public DocumentReference userDocRef;
    private String androidIDStr;
    private UserModel curUser;
//    public CollectionReference facilitiesRef;
    private FacilityModel facility;
    ConstraintLayout mainActivityView;
    ConstraintLayout mainActivityProgressBar;

    /**
     * This method is called when opening the app.
     * This method gets the AndroidID creates a new user.
     * The method then checks if the user is in the database and then updates the information accordingly
     * @param savedInstanceState Stores a small amount of data needed to reload UI state if the system stops and then recreates the UI. This was taken from https://developer.android.com/topic/libraries/architecture/saving-states
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidIDStr = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check this for Qr scanner in fragment
        // https://stackoverflow.com/questions/40725336/android-studio-start-qr-code-scanner-from-fragment
        // https://medium.com/@dev.jeevanyohan/zxing-qr-code-scanner-android-implementing-in-activities-fragment-custom-colors-faa68bfc761d
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mainActivityView = findViewById(R.id.main_activity_view);
        mainActivityProgressBar = findViewById(R.id.main_activity_progressbar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        curUser = new UserModel("", "", "","", false, "", androidIDStr, false, new ArrayList<String>(), new ArrayList<HashMap<String, String>>());
        facility = new FacilityModel("", "", "", "", 0, androidIDStr);
        usersRef = db.collection("users");
        facilitiesRef = db.collection("facilities");

        DocumentReference currentUserReference = usersRef.document(androidIDStr);
        DocumentReference userFacility = db.collection("facilities").document(androidIDStr);

        Task<DocumentSnapshot> task = currentUserReference.get();

        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        curUser = document.toObject(UserModel.class);
                    } else {
                        newUser(curUser);
                    }
                    userFacility.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    facility = document.toObject(FacilityModel.class);
                                    curUser.setFacilityID(facility.getUserID());
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "User doesn't have a facility", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                Log.d("Firestore", "get failed with ", task.getException());
                                throw new RuntimeException(task.getException().toString());
                            }
                        }
                    });
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                    throw new RuntimeException(task.getException().toString());
                }
            }
        });

        // https://stackoverflow.com/questions/66698325/how-to-wait-for-firebase-task-to-complete-to-get-result-as-an-await-function
        task.onSuccessTask(task1 -> {
            mainActivityProgressBar.setVisibility(View.GONE);
            mainActivityView.setVisibility(View.VISIBLE);
            Bundle getExtras = getIntent().getExtras();
            if (getExtras != null){
                String redirect = getExtras.getString("redirect");
                if (redirect != null){
                    if (redirect.equals("NotificationsFragment")){
                        bottomNavigationView.setSelectedItemId(R.id.notifications);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.flFragment, new NotificationsFragment(db, curUser, curUser.getNotifications()))
                                .commit();
                    }
                }
            } else {
                bottomNavigationView.setSelectedItemId(R.id.scanQR);
            }
            return null;
        });
    }

    /**
     * This method is to add a new user to the database
     */
    public void newUser(UserModel cUser) {
        // Adding a new User
        usersRef.document(androidIDStr).set(cUser);
    }

    /**
     * This method implements the bottom navigation view.
     * This method finds the id of the item clicked and uses switch case to create a fragment accordingly.
     * @param item The selected item
     * @return true or false
     */
    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scanQR:
                // Ask for camera permissions if not already allowed to use camera
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ScanFragment(db, curUser))
                        .commit();
                return true;

            case R.id.notifications:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new NotificationsFragment(db, curUser, curUser.getNotifications()))
                        .commit();
                return true;

            case R.id.facility:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new FacilityFragment(db, curUser, facility))
                        .commit();
                return true;
            case R.id.waitlist:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new WaitlistedEventsFragment())
                        .commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ProfileFragment(db, curUser))
                        .commit();
                return true;
        }
        return false; // if nothing was found then return false
    }

    public UserModel getUser() {
        return curUser;
    }

    public FacilityModel getFacility() {
        return facility;
    }
}