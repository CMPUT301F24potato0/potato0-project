package com.example.eventlottery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    public FirebaseFirestore db;
    public CollectionReference usersRef;
    public CollectionReference facilitiesRef;
    private String androidIDStr;
    private CurrentUser curUser;
    private FacilityModel facility;
    ConstraintLayout mainActivityView;
    ConstraintLayout mainActivityProgressBar;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.e("Permission", "Granted");
                }
            });

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidIDStr = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        askNotificationPermission();
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        mainActivityView = findViewById(R.id.main_activity_view);
        mainActivityProgressBar = findViewById(R.id.main_activity_progressbar);

        curUser = new CurrentUser("", "", "", "", false, "", androidIDStr);
        facility = new FacilityModel("", "", "", "", 0, androidIDStr);

        usersRef = db.collection("users");
        facilitiesRef = db.collection("facilities");

        DocumentReference currentUserReference = usersRef.document(androidIDStr);
        currentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        curUser = document.toObject(CurrentUser.class);
                        DocumentReference userFacility = db.collection("facilities").document(androidIDStr);
                        userFacility.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        facility = document.toObject(FacilityModel.class);
                                        curUser.setFacilityID(facility.getUserID());
                                    } else {
                                        Toast.makeText(MainActivity.this, "User doesn't have a facility", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d("Firestore", "get failed with ", task.getException());
                                    throw new RuntimeException(task.getException().toString());
                                }
                            }
                        });
                    } else {
                        newUser(curUser);
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                    throw new RuntimeException(task.getException().toString());
                }
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.scanQR);
        mainActivityView.setVisibility(View.VISIBLE);
    }

    public void newUser(CurrentUser cUser) {
        usersRef.document(androidIDStr).set(cUser);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scanQR:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ScanFragment(db, curUser))
                        .commit();
                return true;
            case R.id.notifications:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new NotificationsFragment())
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
                        .replace(R.id.flFragment, new Profile(db, curUser))
                        .commit();
                return true;
        }
        return false;
    }
}
