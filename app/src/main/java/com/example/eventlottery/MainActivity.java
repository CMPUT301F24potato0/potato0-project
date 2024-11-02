package com.example.eventlottery;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the MainActivity class
 * This class currently handles checking the user in the database, if the user exists then updating the current instance of the user
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    public FirebaseFirestore db;
    public CollectionReference userRef;
    public CollectionReference facilitiesRef;
    private CurrentUser curUser;
    private String androidIDStr;
    private FacilityModel facility;

    /**
     * This method is called when opening the app.
     * This method gets the AndroidID creates a new user.
     * The method then checks if the user is in the database and then updates the information accordingly
     * @param savedInstanceState Stores a small amount of data needed to reload UI state if the system stops and then recreates the UI. This was taken from https://developer.android.com/topic/libraries/architecture/saving-states
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        androidIDStr = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);;
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.scanQR);

        curUser = new CurrentUser("", "", "","", false, "", androidIDStr);
        facility = new FacilityModel("", "", "", "", 0, androidIDStr);

        userRef = db.collection("users");
        facilitiesRef = db.collection("facilities");

        DocumentReference userFacility = db.collection("facilities").document(androidIDStr);

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
                }
            }
        });

        DocumentReference currentUserReference = userRef.document(androidIDStr);

        currentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        curUser = document.toObject(CurrentUser.class);
                        Log.d("Firestore", "DocumentSnapshot data: " + document.getData());
                        Toast.makeText(MainActivity.this, "Document exists", Toast.LENGTH_SHORT).show();
                    } else {
                        newUser(curUser);
                        Toast.makeText(MainActivity.this, "Document doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * This method is to add a new user to the database
     */
    public void newUser(CurrentUser cUser) {
        // Adding a new User
        userRef.document(androidIDStr).set(cUser);
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
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ScanFragment())
                        .commit();
                return true;

            case R.id.notifications:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new NotificationsFragment())
                        .commit();
                return true;

            case R.id.facility:
                if (curUser.getFacilityID().equals("")) {
                    Toast.makeText(MainActivity.this, "Creating a facility", Toast.LENGTH_SHORT).show();
                    new FacilityDetailsDialogueFragment(db, curUser).show(getSupportFragmentManager(), "Create facility");
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new FacilityFragment(db, curUser, (Boolean) curUser.getFacilityID().equals(""), facility))
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
                        .replace(R.id.flFragment, new Profile(db, curUser))
                        //.replace(R.id.flFragment, new Profile(db, profileFragment))
                        .commit();
                return true;
        }
        return false; // if nothing was found then return false
    }

    public void createFacility(String userID) {
        curUser.setFacilityID(androidIDStr);
        userRef.document(userID).set(curUser);
    }
}