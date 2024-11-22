package com.example.eventlottery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * Event Waitlist Activity
 */
public class EventWaitlistActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button notify;
    private ListView waitlist;
    private MapView mapView;
    private GoogleMap googleMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private EventModel event;
    private WaitlistEventAdapter adapter;
    private Button remove;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button drawSample;
    private EditText drawSampleEditText;
    private int remaining_spots;
    private String title;
    private String body;

    private SendNotification sendNotification;

    /**
     * On create Override
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_waitlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            event = (EventModel) extra.getSerializable("eventModel");
        }

        notify = findViewById(R.id.notify_btn_id);
        waitlist = findViewById(R.id.waitList_listview);
        mapView = findViewById(R.id.waitlist_mapview);
        drawSample = findViewById(R.id.draw_sample_button);
        drawSampleEditText = findViewById(R.id.draw_sample_edittext);

        // MapView implementation adapted from Google Maps SDK for Android Samples (RawMapViewDemoActivity.java)
        // https://github.com/googlemaps-samples/android-samples/blob/main/ApiDemos/java/app/src/main/java/com/example/mapdemo/RawMapViewDemoActivity.java
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendNotificationDialog(event, "Waitlist", false, db).show(getSupportFragmentManager(), "Send Notification");
            }
        });

        adapter = new WaitlistEventAdapter(this, 100, event, db);
        waitlist.setAdapter(adapter);

        // calculate remaining spots for event and update edittext
        remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
        drawSampleEditText.setText(Integer.toString(remaining_spots));

        drawSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update remaining spots in case the organizer returns to this page and clicks draw sample button again
                remaining_spots = event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size();
                // Check for empty input
                if (drawSampleEditText.getText().toString().equals("")) {
                    Toast.makeText(EventWaitlistActivity.this, "Please enter a number.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer sample_amount = Integer.parseInt(drawSampleEditText.getText().toString());
                    // Check if the waiting list has enough people to sample
                    if (remaining_spots < sample_amount) {
                        Toast.makeText(EventWaitlistActivity.this, "Your event only has " + remaining_spots + " remaining spots left!", Toast.LENGTH_SHORT).show();
                    }
                    // Check if organizer is trying to sample more than the remaining amount of spots available for the event
                    else if (event.getWaitingList().size() < sample_amount) {
                        Toast.makeText(EventWaitlistActivity.this, "Waiting list only has " + event.getWaitingList().size() + " entrants left!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(EventWaitlistActivity.this, ChosenListActivity.class);
                        intent.putExtra("sample_amount", sample_amount);
                        intent.putExtra("remaining_spots", remaining_spots);
                        intent.putExtra("eventModel", event);
                        startActivity(intent);
                    }
                }
            }
        });

        // Updates all the event's lists
        db.collection("events")
                .document(event.getEventID())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("EventWaitlistActivity", e.toString());
                        }
                        if (doc != null && doc.exists()) {
                            // Get the EventModel object
                            EventModel FireStoreEvent = doc.toObject(EventModel.class);

                            // Update Waiting List
                            event.getWaitingList().clear();
                            event.getWaitingList().addAll(FireStoreEvent.getWaitingList());
                            if (event.getGeolocationRequired()) {
                                updateMapMarkers(event.getWaitingList());
                            }
                            // Update Cancelled List
                            event.getCancelledList().clear();
                            event.getCancelledList().addAll(FireStoreEvent.getCancelledList());
                            // Update Chosen List
                            event.getChosenList().clear();
                            event.getChosenList().addAll(FireStoreEvent.getChosenList());
                            // Update Enrolled List
                            event.getEnrolledList().clear();
                            event.getEnrolledList().addAll(FireStoreEvent.getEnrolledList());
                            // Update Invited List
                            event.getInvitedList().clear();
                            event.getInvitedList().addAll(FireStoreEvent.getInvitedList());
                            // Notify adapter of changes
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void updateMapMarkers(ArrayList<UsersList> entrantsList) {
        googleMap.clear();
        for (UsersList entrant : entrantsList) {
            // The following code is adapted from https://developers.google.com/maps/documentation/android-sdk/map-with-marker
            Double latitude = entrant.getLatitude();
            Double longitude = entrant.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(entrant.getName()));
        }
    }

    // The following overrides are adapted from
    // https://github.com/googlemaps-samples/android-samples/blob/main/ApiDemos/java/app/src/v3/java/com/example/mapdemo/RawMapViewDemoActivity.java
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Check if location permission is granted
        if (ActivityCompat.checkSelfPermission(EventWaitlistActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            // Permission not yet granted, request for location permission
            ActivityCompat.requestPermissions(EventWaitlistActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(EventWaitlistActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, zoom map to area of the organizer
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Double latitude = lastLocation.getLatitude();
            Double longitube = lastLocation.getLongitude();
            LatLng latLng = new LatLng(latitude, longitube);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9f));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
