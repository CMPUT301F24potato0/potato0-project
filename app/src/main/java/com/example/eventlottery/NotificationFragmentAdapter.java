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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
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
    private ArrayList<HashMap<String, String>> notifications;
    public NotificationFragmentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public NotificationFragmentAdapter
            (@NonNull Context context,
             int resource, ArrayList<HashMap<String, String>> notifications,
             CurrentUser currentUser,
             FirebaseFirestore db) {
        super(context, resource, notifications);
        this.notifications = notifications;
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
        TextView title = view.findViewById(R.id.title_id);
        TextView message = view.findViewById(R.id.message_id);
        TextView eventID = view.findViewById(R.id.envent_id);
        TextView flagID = view.findViewById(R.id.flag_id);
        Button cancel_btn = view.findViewById(R.id.cancel_id);
        Button signup_btn = view.findViewById(R.id.signup_id);
        HashMap<String, String> notification = getItem(position);

        title.setText(notification.get("title"));
        message.setText(notification.get("body"));
        eventID.setText(notification.get("eventID"));
        flagID.setText(notification.get("flag"));

        if (flagID.getText().toString().equals("Chosen")) {
            // Make sure the buttons are visible
            cancel_btn.setVisibility(View.VISIBLE);
            signup_btn.setVisibility(View.VISIBLE);

            // Setting up Sign Up button
            signup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> notification = getItem(position);
                    DocumentReference eventRef = db.collection("events").document(notification.get("eventID"));
                    eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null) {
                                EventModel eventFireStore = documentSnapshot.toObject(EventModel.class);
                                UsersList entrantUsersList = new UsersList(currentUser.getiD(), currentUser.getfName() + " " + currentUser.getlName());
                                // Update event model after user chooses to join the event
                                try {
                                    eventFireStore.unqueueInvitedList(entrantUsersList);
                                    eventFireStore.queueEnrolledList(entrantUsersList);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                // Update event in FireStore database
                                db.collection("events").document(notification.get("eventID")).set(eventFireStore);
                                // Remove notification and update the user on FireStore
                                remove(notification);
                                currentUser.removeNotifications(notification);
                                db.collection("users").document(currentUser.getiD()).set(currentUser);
                                notifyDataSetChanged();
                            }
                        }
                    });


                }
            });

            // Setting up Cancel button
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> notification = getItem(position);
                    DocumentReference eventRef = db.collection("events").document(notification.get("eventID"));
                    eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null) {
                                EventModel eventFireStore = documentSnapshot.toObject(EventModel.class);
                                UsersList entrantUsersList = new UsersList(currentUser.getiD(), currentUser.getfName() + " " + currentUser.getlName());
                                // Update event model after user chooses to not join the event
                                try {
                                    eventFireStore.unqueueInvitedList(entrantUsersList);
                                    eventFireStore.queueCancelledList(entrantUsersList);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                // Update event in FireStore database
                                db.collection("events").document(notification.get("eventID")).set(eventFireStore);
                                // Remove notification and update the user on FireStore
                                remove(notification);
                                currentUser.removeNotifications(notification);
                                db.collection("users").document(currentUser.getiD()).set(currentUser);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }
        else {
            cancel_btn.setVisibility(View.GONE);
            signup_btn.setVisibility(View.GONE);
        }

        return view;
    }
}
