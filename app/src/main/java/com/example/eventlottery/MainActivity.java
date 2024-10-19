package com.example.eventlottery;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.scanQR);
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    ScanFragment scanFragment = new ScanFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();
    FacilityFragment facilityFragment = new FacilityFragment();
    WaitlistedEventsFragment waitlistedEventsFragment = new WaitlistedEventsFragment();
    Profile profileFragment = new Profile();

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