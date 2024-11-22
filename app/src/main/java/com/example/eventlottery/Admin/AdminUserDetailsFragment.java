package com.example.eventlottery.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminUserDetailsFragment extends DialogFragment {
    private final UserModel user;
    private FirebaseFirestore db;

    public AdminUserDetailsFragment(UserModel item) {
        user = item;
        db = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.admin_user_details, null);
        ((TextView)rootView.findViewById(R.id.admin_name_info)).setText(String.format("Name: %s %s", user.getfName(), user.getlName()));
        ((TextView)rootView.findViewById(R.id.admin_email_info)).setText(String.format("Email: %s", user.getEmail()));
        ((TextView)rootView.findViewById(R.id.admin_phone_info)).setText(String.format("Phone: %s", user.getPhone()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ((Button) rootView.findViewById(R.id.admin_delete_user)).setOnClickListener((View view) -> {
            user.delete(db);
            dismiss();
        });;
        ((Button) rootView.findViewById(R.id.cancel_button)).setOnClickListener((View view) -> {
            dismiss();
        });
        builder.setView(rootView);
        return builder.create();
    }
}
