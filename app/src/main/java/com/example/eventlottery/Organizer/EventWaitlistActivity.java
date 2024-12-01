package com.example.eventlottery.Organizer;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Notifications.SendNotification;
import com.example.eventlottery.Notifications.SendNotificationDialog;
import com.example.eventlottery.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * Event Waitlist Activity
 * This class create the view where the organizer can see the people on the waitlist and manage them
 * it also in this class the user can draw some user that will be chosen, so they can enroll to the event
 * if the geolocation is on, this class shows the location in a map of the moment where the user join the event
 * wait list
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
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Button drawSample;
    private EditText drawSampleEditText;
    private int remaining_spots;
    private String title;
    private String body;
    private TextView waitListLimit;
    private TextView eventCapacity;
    private TextView geoLocationRequired;
    private TextView waitListCount;
    private SendNotification sendNotification;
    private FloatingActionButton backFAB;

    /**
     * On create Override
     * Create the view so the organizer can interact with the event wait list
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
        waitListLimit = findViewById(R.id.eventWaitlistActivity_waitListLimit);
        eventCapacity = findViewById(R.id.eventWaitlistActivity_eventCapacity);
        geoLocationRequired = findViewById(R.id.eventWaitlistActivity_geolocationRequired);
        waitListCount = findViewById(R.id.eventWaitlistActivity_waitListCount);
        backFAB = findViewById(R.id.back);

        // Updated Logic for Waiting List Limit
        if (event.getWaitingListLimit() == -1) {
            waitListLimit.setText("No Limit");
        } else {
            waitListLimit.setText(event.getWaitingListLimit().toString());
        }

        eventCapacity.setText(event.getCapacity().toString());
        geoLocationRequired.setText(event.getGeolocationRequired() ? "Yes" : "No");
        waitListCount.setText(event.getWaitingList().size() + "");

        // MapView implementation adapted from Google Maps SDK for Android Samples (RawMapViewDemoActivity.java)
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

        adapter = new WaitlistEventAdapter(this, 100, event, db, waitListCount);
        waitlist.setAdapter(adapter);

        drawSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update remaining spots in case the organizer returns to this page and clicks draw sample button again
                remaining_spots = calculateRemainingSpots();
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

                            // Update remaining spots left for invitation
                            remaining_spots = calculateRemainingSpots();
                            drawSampleEditText.setText(Integer.toString(remaining_spots));
                        }
                    }
                });
        backFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * A helper function that returns the remaining spots left for invitations to the event
     * @return
     */
    private Integer calculateRemainingSpots() {
        return event.getCapacity() - event.getEnrolledList().size() - event.getInvitedList().size() - event.getChosenList().size();
    }

    /**
     * This method put in the map the location that is recorded on the firebase of the moment the entrant
     * join the waitlist
     * @param entrantsList
     *                  Arraylist with the entrants that are in the wait list
     */
    private void updateMapMarkers(ArrayList<RemoteUserRef> entrantsList) {
        googleMap.clear();
        for (RemoteUserRef entrant : entrantsList) {
            // The following code is adapted from https://developers.google.com/maps/documentation/android-sdk/map-with-marker
            Double latitude = entrant.getLatitude();
            Double longitude = entrant.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(entrant.getName()));
        }
    }

    /**
     * This method check for permission and if is true, zoom to the location of the organizer
     * @param googleMap
     *              GoogleMap object
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            LatLng organizerLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(organizerLocation, 12.0f));
        }
    }

    /**
     * This method start the map to show in the view
     */
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
