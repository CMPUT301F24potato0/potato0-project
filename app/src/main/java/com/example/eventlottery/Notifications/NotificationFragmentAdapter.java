package com.example.eventlottery.Notifications;

import android.content.Context;
import android.media.metrics.Event;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is the NotificationFragmentAdapter class
 */
public class NotificationFragmentAdapter extends ArrayAdapter<HashMap<String, String>> {

    /**
     * Constructor for NotificationFragmentAdapter
     */

    private UserModel currentUser;
    private FirebaseFirestore db;
    private ArrayList<HashMap<String, String>> notifications;
    public NotificationFragmentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     * Constructor for NotificationFragmentAdapter
     * @param context The context
     * @param resource The resource
     * @param notifications ArrayList of notifications
     * @param currentUser The current user
     * @param db The database
     */
    public NotificationFragmentAdapter
            (@NonNull Context context,
             int resource, ArrayList<HashMap<String, String>> notifications,
             UserModel currentUser,
             FirebaseFirestore db) {
        super(context, resource, notifications);
        this.notifications = notifications;
        this.currentUser = currentUser;
        this.db = db;
    }

    /**
     * Get View override
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
        Button delete_notification = view.findViewById(R.id.delete_notification);
        HashMap<String, String> notification = getItem(position);

        title.setText(notification.get("title"));
        message.setText(notification.get("body"));
        flagID.setText(notification.get("flag"));
        DocumentReference eventRef = db.collection("events").document(notification.get("eventID"));
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        EventModel eventFireStore = document.toObject(EventModel.class);
                        if (eventFireStore != null)
                            eventID.setText(eventFireStore.getEventTitle());
                        else
                            Toast.makeText(getContext(), "This event was deleted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Event Doesn't Exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot != null) {
//                    EventModel eventFireStore = documentSnapshot.toObject(EventModel.class);
//                    eventID.setText(eventFireStore.getEventTitle());
//                }
//            }
//        });

        if (flagID.getText().toString().equals("Chosen")) {
            // Make sure the buttons are visible
            cancel_btn.setVisibility(View.VISIBLE);
            signup_btn.setVisibility(View.VISIBLE);

            // Setting up Sign Up button
            signup_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference eventRef = db.collection("events").document(notification.get("eventID"));
                    eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists() && documentSnapshot != null) {
                                    EventModel eventFireStore = documentSnapshot.toObject(EventModel.class);
                                    if (eventFireStore != null){
                                        RemoteUserRef entrantRemoteUserRef = null;
                                        for (RemoteUserRef entrant : eventFireStore.getInvitedList()) {
                                            if (entrant.getiD().equals(currentUser.getiD())) {
                                                entrantRemoteUserRef = entrant;
                                                break;
                                            }
                                        }
                                        // Update event model after user chooses to join the event
                                        try {
                                            eventFireStore.unqueueInvitedList(entrantRemoteUserRef);
                                            eventFireStore.queueEnrolledList(entrantRemoteUserRef);
                                            // Update event in FireStore database
                                            db.collection("events").document(notification.get("eventID")).set(eventFireStore);
                                            // Remove notification and update the user on FireStore
                                            remove(notification);
                                            currentUser.removeNotifications(notification);
                                            db.collection("users").document(currentUser.getiD()).set(currentUser);
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "There was an error. Please contact the organizer/admin.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "This event was deleted", Toast.LENGTH_SHORT).show();
                                        remove(notification);
                                    }
                                } else {
                                    Toast.makeText(getContext(), "This event was deleted", Toast.LENGTH_SHORT).show();
                                    remove(notification);
                                }
                            }
                        }
                    });
                }
            });

            // Setting up Cancel button
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference eventRef = db.collection("events").document(notification.get("eventID"));
                    eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists() && documentSnapshot != null) {
                                    EventModel eventFireStore = documentSnapshot.toObject(EventModel.class);
                                    if (eventFireStore != null) {
                                        // Getting the RemoteUserRef object that represents the same entrant (has the same unique ID)
                                        RemoteUserRef entrantRemoteUserRef = null;
                                        for (RemoteUserRef entrant : eventFireStore.getInvitedList()) {
                                            if (entrant.getiD().equals(currentUser.getiD())) {
                                                entrantRemoteUserRef = entrant;
                                                break;
                                            }
                                        }
                                        // Update event model after user chooses to not join the event
                                        try {
                                            eventFireStore.unqueueInvitedList(entrantRemoteUserRef);
                                            eventFireStore.queueCancelledList(entrantRemoteUserRef);
                                            // Update event in FireStore database
                                            db.collection("events").document(notification.get("eventID")).set(eventFireStore);
                                            // Remove notification and update the user on FireStore
                                            remove(notification);
                                            currentUser.removeNotifications(notification);
                                            db.collection("users").document(currentUser.getiD()).set(currentUser);
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            Toast.makeText(getContext(), "There was an error. Please contact the organizer/admin.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getContext(), "This event was deleted", Toast.LENGTH_SHORT).show();
                                    remove(notification);
                                }
                            } else {
                                Toast.makeText(getContext(), "This event was deleted", Toast.LENGTH_SHORT).show();
                                remove(notification);
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

        delete_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(notification);
                currentUser.removeNotifications(notification);
                db.collection("users").document(currentUser.getiD()).set(currentUser);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
