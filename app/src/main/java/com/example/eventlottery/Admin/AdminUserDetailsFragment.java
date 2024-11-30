package com.example.eventlottery.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.R;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminUserDetailsFragment extends DialogFragment {
    private final UserModel user;
    private final FirebaseFirestore db;

    public AdminUserDetailsFragment(UserModel item) {
        user = item;
        db = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.admin_user_details, null);
        ((TextView)rootView.findViewById(R.id.admin_name_info)).setText(String.format("%s %s", user.getfName(), user.getlName()));
        ((TextView)rootView.findViewById(R.id.admin_email_info)).setText(String.format("%s", user.getEmail()));
        ((TextView)rootView.findViewById(R.id.admin_phone_info)).setText(String.format("%s", user.getPhone()));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        rootView.findViewById(R.id.admin_delete_user).setOnClickListener((View view) -> {
            user.delete(db);
            dismiss();
        });
        rootView.findViewById(R.id.admin_user_cancel_button).setOnClickListener((View view) -> {
            dismiss();
        });
        Button delete_pfp = rootView.findViewById(R.id.admin_delete_pfp);

        ImageView pfp = rootView.findViewById(R.id.view_profile_pic);
        db.collection("photos").document(user.getiD()).get().addOnCompleteListener( task -> {
            DocumentSnapshot document = task.getResult();
            if (document.exists()){
                Blob blob = document.getBlob("Blob");
                byte[] bytes = blob.toBytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                pfp.setImageBitmap(bitmap);
            } else {
                pfp.setImageResource(R.drawable.defaultprofilepicture);
                delete_pfp.setVisibility(View.GONE);
            }
        });
        delete_pfp.setOnClickListener((view) -> {
            db.collection("photos").document(user.getiD()).delete();
            pfp.setImageBitmap(null);
            pfp.setImageResource(R.drawable.defaultprofilepicture);
            delete_pfp.setVisibility(View.GONE);
        });
        builder.setView(rootView);
        return builder.create();
    }
}
