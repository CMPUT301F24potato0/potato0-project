package com.example.eventlottery.Admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventlottery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    public FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        db = FirebaseFirestore.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.eventsAdmin);

    }

    @Override
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.eventsAdmin:
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.adminFragments, new AdminEventsFragment(db))
                    .commit();
            return true;

            case R.id.facilitiesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminFacilityFragment(db))
                        .commit();
                return true;

            case R.id.profilesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminUserFragment(db))
                        .commit();
                return true;

            case R.id.imagesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminImagesFragment(db))
                        .commit();
                return true;



        }
        return false; // if nothing was found then return false
    }
}