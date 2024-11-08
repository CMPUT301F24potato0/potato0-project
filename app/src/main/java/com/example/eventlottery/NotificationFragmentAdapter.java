package com.example.eventlottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.checker.units.qual.Current;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationFragmentAdapter extends ArrayAdapter<HashMap<String, String>> {


    private CurrentUser currentUser;
    private FirebaseFirestore db;

    public NotificationFragmentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public NotificationFragmentAdapter
            (@NonNull Context context,
             int resource, ArrayList<HashMap<String, String>> notifications,
             CurrentUser currentUser,
             FirebaseFirestore db) {
        super(context, resource);
        this.currentUser = currentUser;
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.notification_adapter_view, null);
        } else {
            view = convertView;
        }

        // get the notification from the data base using db and curUser
        TextView title = (TextView) view.findViewById(R.id.title_id);
        TextView message = (TextView) view.findViewById(R.id.message_id);
        TextView eventID = (TextView) view.findViewById(R.id.envent_id);
        TextView flagID = (TextView) view.findViewById(R.id.flag_id);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_id);
        Button signup_btn = (Button) view.findViewById(R.id.signup_id);
        HashMap<String, String> notification = getItem(position);
        //        currentUser = getItem(position);
//        ArrayList<HashMap<String, String>> notification = currentUser.getNotifications();
        title.setText(notification.get("title"));
        message.setText(notification.get("body"));
        eventID.setText(notification.get("eventID"));
        flagID.setText(notification.get("flag"));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_btn.setVisibility(View.GONE);
                signup_btn.setVisibility(View.GONE);
                // TODO remove from invited

            }
        });
//
//        Task<DocumentSnapshot> t = db.collection()
//
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_btn.setVisibility(View.GONE);
                signup_btn.setVisibility(View.GONE);
                // remove from invited
                // move to enrolled
            }
        });



        db
            .collection("users")
            .document(currentUser.getiD())
            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getContext(), "error: " + error.toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (value != null && value.exists()) {
                        currentUser = value.toObject(CurrentUser.class);
                        notifyDataSetChanged();
                    }
                }
            });

        // if flag "chose" then button visible
        // button will add user to enrolled, remove from invited, remove button and update data base
        // if button cancel then remove both buttons, move user to canceled list and remove from invited


        return view;
    }
}
