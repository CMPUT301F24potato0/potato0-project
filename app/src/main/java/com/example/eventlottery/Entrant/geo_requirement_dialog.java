package com.example.eventlottery.Entrant;

import android.Manifest;
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
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Notifications.SubscribeToTopic;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class create a dialog to tell the user that the specific event require geolocation
 * then the user has the option to accept or reject the geolocation, if they accept the user is
 * added to the event, if the user decline then nothing happened
 */
public class geo_requirement_dialog extends DialogFragment {

    private final RemoteUserRef user;
    private final EventModel event;
    private final FirebaseFirestore db;
    private UserModel cuUser;
    private final Button joinBtn;
    private final Button unjoinBtn;

    /**
     * Constructor
     * @param user User
     *             The User that wants to join the event
     * @param event Event Model
     *              The Event the user wants to join
     * @param db Firebase Firestore
     *           The Firebase that contains the info of the event and user
     */
    public geo_requirement_dialog(RemoteUserRef user, EventModel event, FirebaseFirestore db, Button joinBtn, Button unjoinBtn ) {
        this.user = user;
        this.event = event;
        this.db = FirebaseFirestore.getInstance();
        this.joinBtn = joinBtn;
        this.unjoinBtn = unjoinBtn;
    }

    /**
     *This method configure the color of the positive button and negative button, also configure
     * the color of the background of the dialog
     */
    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }

    /**
     * This method create the dialogue view so the user can see it and interact with it
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     * @return Dialog
     *      return the generated Dialog
     */
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.geo_requirement_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        AlertDialog alertDialog = builder.setView(view)
                .setView(view)
                .setTitle("   ")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Accept", null)
                .create();

        // Adapted from https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
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
                                event.registerUserID(user);
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "The waiting list is already full or the user is already inside the waiting list", Toast.LENGTH_SHORT).show();
                            }
                            Task<DocumentSnapshot> task = db.collection("users").document(user.getiD()).get();
                            task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        cuUser = documentSnapshot.toObject(UserModel.class);
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