package com.example.eventlottery;

//public class geo_requirement_dialog {
//}
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class geo_requirement_dialog extends DialogFragment {

    private UsersList user;
    private EventModel event;
    private FirebaseFirestore db;

    public geo_requirement_dialog(UsersList user, EventModel event, FirebaseFirestore db ) {
        this.user = user;
        this.event = event;
//        this.eventRef = eventRef;
        this.db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.geo_requirement_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Geo location required")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Accept", (dialog, which) -> {
                    try {
                        event.queueWaitingList(user);
                        db.collection("events").document(event.getEventID()).set(event);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .create();
    }
}