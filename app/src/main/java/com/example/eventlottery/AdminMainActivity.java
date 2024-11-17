package com.example.eventlottery;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

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
                    .replace(R.id.adminFragments, new AdminEventsFragment())
                    .commit();
            return true;

            case R.id.facilitiesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminFacilityFragment())
                        .commit();
                return true;

            case R.id.profilesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminUserFragment())
                        .commit();
                return true;

            case R.id.imagesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminImagesFragment())
                        .commit();
                return true;



        }
        return false; // if nothing was found then return false
    }
}