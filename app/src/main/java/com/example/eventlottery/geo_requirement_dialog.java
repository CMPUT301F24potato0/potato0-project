package com.example.eventlottery;

//public class geo_requirement_dialog {
//}
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is the geo_requirement_dialog
 */
public class geo_requirement_dialog extends DialogFragment {

    private UsersList user;
    private EventModel event;
    private CurrentUser curUser;
    private FirebaseFirestore db;

    /**
     * Constructor
     * @param user User
     * @param event Event Model
     * @param db Firebase Firestore
     */
    public geo_requirement_dialog(UsersList user, CurrentUser curUser, EventModel event, FirebaseFirestore db ) {
        this.user = user;
        this.event = event;
        this.curUser = curUser;
        this.db = FirebaseFirestore.getInstance();
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

        Task<DocumentSnapshot> t = db.collection("users").document(user.getiD()).get();
        t.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    curUser = documentSnapshot.toObject(CurrentUser.class);
                }
            }
        });
        t.onSuccessTask(t1 -> {
            return null;
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Geo location required")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Accept", (dialog, which) -> {
                    try {
                        event.queueWaitingList(user);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    String eventID = event.getEventID();
                    String userID = user.getiD();
                    String topic = eventID + "_" + userID;
                    curUser.addTopics(topic);
                    db.collection("users").document(userID).set(curUser);
//                    Task<DocumentSnapshot> task = db.collection("users").document(userID).get();
//                    task.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            if (documentSnapshot.exists()) {
//                                curUser.addTopics(topic);
//                                db.collection("users").document(userID).set(curUser);
//                            }
//                        }
//                    });

                    SubscribeToTopic subscribeToTopic_geo = new SubscribeToTopic(topic,getContext());
                    subscribeToTopic_geo.subscribe();
                    db.collection("events").document(event.getEventID()).set(event);
                })
                .create();
    }
}