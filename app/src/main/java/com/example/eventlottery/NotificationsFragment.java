package com.example.eventlottery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the Notification fragment
 * This class inflates fragment_notifications
 */
public class NotificationsFragment extends Fragment {

    private FirebaseFirestore db;
    private CurrentUser curUser;
    private ArrayList<HashMap<String, String>> notifications;
    private ListView notification_listview;

    public NotificationsFragment(){
        // require a empty public constructor
    }

    public NotificationsFragment(FirebaseFirestore db, CurrentUser curUser, ArrayList<HashMap<String, String>> notifications) {
        this.db = db;
        this.curUser = curUser;
        this.notifications = notifications;
    }



    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notification_listview = view.findViewById(R.id.notification_listview);
        notifications = curUser.getNotifications();
        NotificationFragmentAdapter adapter = new NotificationFragmentAdapter(
                requireContext(),
                100,
                notifications,
                curUser,
                db
        );
        notification_listview.setAdapter(adapter);

        db.collection("users")
                .document(curUser.getiD())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), "error: " + error.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (value != null && value.exists()) {
                            CurrentUser tempCurUser = value.toObject(CurrentUser.class);
                            curUser.getNotifications().clear();
                            for (HashMap<String, String> notification : tempCurUser.getNotifications()) {
                                curUser.addNotifications(notification);
                            }
                            notifications = curUser.getNotifications();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        return view;
    }
}