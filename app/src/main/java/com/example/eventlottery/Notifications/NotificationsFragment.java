package com.example.eventlottery.Notifications;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.R;
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
    private UserModel curUser;
    private ArrayList<HashMap<String, String>> notifications;
    private ListView notification_listview;
    private ConstraintLayout notification_off_textView;
    private LinearLayout notification_view;
    private Button clearAll;
    private NotificationFragmentAdapter adapter;
    /**
     * Empty constructor
     */
    public NotificationsFragment(){
        // require a empty public constructor
    }

    /**
     * Constructor for NotificationsFragment
     * @param db The database
     * @param curUser The current user
     * @param notifications The ArrayList of notifications
     */
    public NotificationsFragment(FirebaseFirestore db, UserModel curUser, ArrayList<HashMap<String, String>> notifications) {
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

        notification_view = view.findViewById(R.id.notification_view);
        notification_listview = view.findViewById(R.id.notification_listview);
        notification_off_textView = view.findViewById(R.id.notification_off_textView);

        if (curUser.isMuted()) {
            notification_off_textView.setVisibility(View.VISIBLE);
            notification_view.setVisibility(View.GONE);
        } else {
            notification_off_textView.setVisibility(View.GONE);
            notification_view.setVisibility(View.VISIBLE);
        }
        notifications = curUser.getNotifications();
        adapter = new NotificationFragmentAdapter(
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
                            Toast.makeText(getContext(), "error: " + error, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (value != null && value.exists()) {
                            UserModel tempCurUser = value.toObject(UserModel.class);
                            curUser.getNotifications().clear();
                            for (HashMap<String, String> notification : tempCurUser.getNotifications()) {
                                curUser.addNotifications(notification);
                            }
                            notifications = curUser.getNotifications();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


        // working
        clearAll = view.findViewById(R.id.clearAll_btn);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curUser.getNotifications().clear();
                db.collection("users").document(curUser.getiD()).set(curUser);

            }
        });
        return view;
    }
}