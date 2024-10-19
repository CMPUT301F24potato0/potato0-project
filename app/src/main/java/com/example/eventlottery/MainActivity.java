package com.example.eventlottery;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    public FirebaseFirestore db;
    public CollectionReference userRef;
    private CollectionReference photosRef;
    protected AndroidID androidID;
    private CurrentUser curUser;

//    private Map<String, Object> user_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String androidIDStr = androidID.getAndroidId(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.scanQR);

        DocumentReference userRef = db.collection("users").document(androidIDStr);
        final String[] f_name = new String[1];
        final String[] l_name = new String[1];
        String phone;
        final String[] email = new String[1];
        final boolean[] is_admin = new boolean[1];

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d("Firestore", "DocumentSnapshot data: " + document.getData());
//                        user_map = document.getData();
                        f_name[0] = document.getString("f_name");
                        l_name[0] = document.getString("f_name");
                        email[0] = document.getString("email");
                        is_admin[0] = document.getBoolean("isAdmin");
                    } else {
                        Log.d("Firestore", "No such document");
//                        user_map = new HashMap<String, Object>();
//                        user_map.put("android_id", androidIDStr);
//                        user_map.put("isAdmin", false);
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            }
        });
        curUser = new CurrentUser("Test", "test", "test@email.com",true);
    }

    ScanFragment scanFragment = new ScanFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();
    FacilityFragment facilityFragment = new FacilityFragment();
    WaitlistedEventsFragment waitlistedEventsFragment = new WaitlistedEventsFragment();
    Profile profileFragment = new Profile(db, curUser);

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scanQR:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, scanFragment)
                        .commit();
                return true;

            case R.id.notifications:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, notificationsFragment)
                        .commit();
                return true;

            case R.id.facility:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, facilityFragment)
                        .commit();
                return true;
            case R.id.waitlist:
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, waitlistedEventsFragment)
                    .commit();
                return true;

            case R.id.profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, profileFragment)
                        .commit();
                return true;
        }
        return false;
    }
}