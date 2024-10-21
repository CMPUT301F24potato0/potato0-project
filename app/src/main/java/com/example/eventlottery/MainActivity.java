package com.example.eventlottery;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    public FirebaseFirestore db;
    public CollectionReference userRef;
    private CollectionReference photosRef;
    protected AndroidID androidID;
    private CurrentUser curUser;

    private String androidIDStr;

//    private Map<String, Object> user_map;

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

        curUser = new CurrentUser("Test1", "test1", "test1@email.com",true, androidIDStr);

        userRef = db.collection("users");
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
                        if (userID.equals(androidIDStr)) {
                            curUser.setEmail(email);
                            curUser.setfName(f_name);
                            curUser.setlName(l_name);
                            return;
                        }
                        //Log.d("Firestore", String.format("City(%s, %s) fetched", city, province));
                        //cityDataList.add(new City(city, province));
                    }

                    //cityArrayAdapter.notifyDataSetChanged();
                }
                newUser();
            }
        });




//        DocumentReference userRef = db.collection("users").document(androidIDStr);
//        final String[] f_name = new String[1];
//        final String[] l_name = new String[1];
//        String phone;
//        final String[] email = new String[1];
//        final boolean[] is_admin = new boolean[1];
//
//        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//
//                    if (document.exists()) {
//                        Log.d("Firestore", "DocumentSnapshot data: " + document.getData());
////                        user_map = document.getData();
//                        f_name[0] = document.getString("f_name");
//                        l_name[0] = document.getString("f_name");
//                        email[0] = document.getString("email");
//                        is_admin[0] = document.getBoolean("isAdmin");
//                    } else {
//                        Log.d("Firestore", "No such document");
////                        user_map = new HashMap<String, Object>();
////                        user_map.put("android_id", androidIDStr);
////                        user_map.put("isAdmin", false);
//                    }
//                } else {
//                    Log.d("Firestore", "get failed with ", task.getException());
//                }
//            }
//        });
    }

    public void newUser() {
        // Add the city to the local list
        //cityDataList.add(city);

        // Add the city to the Firestore collection with the city name as the document Id
        HashMap<String, String> data = new HashMap<>();
        data.put("android_id", curUser.getiD());
        data.put("email", curUser.getEmail());
        data.put("f_name", curUser.getfName());
        data.put("l_name", curUser.getlName());

        userRef.document(curUser.getiD()).set(data);
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
                        .replace(R.id.flFragment, new Profile(db, curUser))
                        //.replace(R.id.flFragment, new Profile(db, profileFragment))
                        .commit();
                return true;
        }
        return false;
    }
}