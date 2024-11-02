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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

public class geo_requirement_dialog extends DialogFragment {

    private String userID;
    private DocumentReference eventRef;

    public geo_requirement_dialog(String userID, DocumentReference eventRef) {
        this.userID = userID;
        this.eventRef = eventRef;
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
                    eventRef.update("waiting_list", FieldValue.arrayUnion(userID));
                })
                .create();
    }
}