package com.example.eventlottery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.primitives.Booleans;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

/**
 * This class is the geo_requirement_dialog
 */
public class geo_requirement_dialog extends DialogFragment {

    private UsersList user;
    private EventModel event;
    private FirebaseFirestore db;
    private CurrentUser cuUser;
    private Button joinBtn;
    private Button unjoinBtn;

    /**
     * Constructor
     * @param user User
     * @param event Event Model
     * @param db Firebase Firestore
     */
    public geo_requirement_dialog(UsersList user, EventModel event, FirebaseFirestore db, Button joinBtn, Button unjoinBtn ) {
        this.user = user;
        this.event = event;
        this.db = FirebaseFirestore.getInstance();
        this.joinBtn = joinBtn;
        this.unjoinBtn = unjoinBtn;
    }

    /**
     * On create dialog override
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     * @return Dialog
     */
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.geo_requirement_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        AlertDialog alertDialog = builder.setView(view)
                .setView(view)
                .setTitle("Geo location required")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Accept", null)
                .create();

        // Adapted from https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if location permission has been granted
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                user.setLatitude(lastLocation.getLatitude());
                                user.setLongitude(lastLocation.getLongitude());
                                event.queueWaitingList(user);
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "The waiting list is already full or the user is already inside the waiting list", Toast.LENGTH_SHORT).show();
                            }
                            Task<DocumentSnapshot> task = db.collection("users").document(user.getiD()).get();
                            task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        cuUser = documentSnapshot.toObject(CurrentUser.class);
                                    }
                                }
                            });
                            db.collection("events").document(event.getEventID()).set(event);  // updates
                            task.onSuccessTask(t1 -> {
                                cuUser.addTopics(event.getEventID() + "_" + user.getiD());
                                // Subscribing to topic when joining event to receive notification
                                SubscribeToTopic subscribeToTopic = new SubscribeToTopic(event.getEventID() + "_" + user.getiD(),getContext());
                                subscribeToTopic.subscribe();
                                cuUser.addTopics(event.getEventID() + "_" + user.getiD());
                                // ****************************************************************************************
                                db.collection("users").document(user.getiD()).set(cuUser);
                                joinBtn.setVisibility(View.GONE);
                                unjoinBtn.setVisibility(View.VISIBLE);
                                return null;

//                                cuUser.addTopics(event.getEventID() + "_" + user.getiD());
//                                SubscribeToTopic subscribeToTopic = new SubscribeToTopic(event.getEventID() + "_" + user.getiD(),getContext());
//                                subscribeToTopic.subscribe();
//                                db.collection("users").document(user.getiD()).set(cuUser);
//                                joinBtn.setVisibility(View.GONE);
//                                unjoinBtn.setVisibility(View.VISIBLE);
//                                return null;
                            });
                            alertDialog.dismiss();
                        } else {
                            // Ask for location permission if not yet granted
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                            Toast.makeText(getContext(), "Please allow precise location permissions.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return alertDialog;
    }
}