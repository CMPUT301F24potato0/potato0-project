package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

public class FacilityDetailsDialogueFragment extends DialogFragment {

    public FacilityDetailsDialogueFragment() {
        super();
    }

    private FirebaseFirestore db;
    private CurrentUser curUser;

    public FacilityDetailsDialogueFragment(FirebaseFirestore db, CurrentUser curUser) {
        this.db = db;
        this.curUser = curUser;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_facility_details_dialogue, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);


        builder
                .setTitle("Create a facility")
                .setPositiveButton("Confirm", (dialog, which) -> {

                }
                .create();
    }
}