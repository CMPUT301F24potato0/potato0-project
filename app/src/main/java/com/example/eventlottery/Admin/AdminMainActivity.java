package com.example.eventlottery.Admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventlottery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private FloatingActionButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.eventsAdmin);
        back = findViewById(R.id.back_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

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
                        .replace(R.id.adminFragments, new AdminFacilitiesFragment())
                        .commit();
                return true;

            case R.id.profilesAdmin:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.adminFragments, new AdminUsersFragment())
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