package com.example.eventlottery;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

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

        userRef = db.collection("users");
        facilitiesRef = db.collection("facilities");

        userRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String userID = doc.getId();
                        String email = doc.getString("email");
                        String f_name = doc.getString("f_name");
                        String l_name = doc.getString("l_name");
                        String phone = doc.getString("phone");
                        boolean isAdmin = doc.getBoolean("isAdmin");
                        String facilityID = doc.getString("facilityID");
                        if (userID.equals(androidIDStr)) {
                            curUser.setEmail(email);
                            curUser.setfName(f_name);
                            curUser.setlName(l_name);
                            curUser.setPhone(phone);
                            curUser.setIsAdmin(isAdmin);
                            curUser.setFacilityID(facilityID);
                            return;
                        }
                    }
                }
                newUser();
            }
        });
    }

    /**
     * This method is to add a new user to the database
     */
    public void newUser() {
        // Adding a new User
        HashMap<String, Object> data = new HashMap<>();
        data.put("android_id", curUser.getiD());
        data.put("email", curUser.getEmail());
        data.put("f_name", curUser.getfName());
        data.put("l_name", curUser.getlName());
        data.put("isAdmin", curUser.getIsAdmin());
        data.put("phone", curUser.getPhone());
        userRef.document(curUser.getiD()).set(data);
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
                userRef.document(curUser.getiD()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String facilityID = documentSnapshot.getString("facilityID");
                            if (facilityID == "") {
                                Toast.makeText(MainActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                new CreateEventFragment(curUser).show(getSupportFragmentManager(), "Create Event");
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.flFragment, new CreateEventFragment())
//                        .commit();
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
}