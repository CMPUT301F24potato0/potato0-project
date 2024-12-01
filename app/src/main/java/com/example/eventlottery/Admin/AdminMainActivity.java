package com.example.eventlottery.Admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eventlottery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Class that creates a bottom Navigation View, that makes the user able to navigate between
 * fragments
 */
public class AdminMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private FloatingActionButton back;

    /**
     * This method generates the bottom Navigation View
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);


        bottomNavigationView = findViewById(R.id.admin_bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.eventsAdmin);


        back = findViewById(R.id.back_main);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    /**
     * This method makes the bottom navigation view interactable, making the user navigate between
     * the fragments that the botton navigation view offers
     * @param item The selected item
     * @return
     *      It creates a new fragment depening on the button the user clicked
     */
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