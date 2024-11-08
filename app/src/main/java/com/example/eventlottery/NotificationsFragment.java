package com.example.eventlottery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is the Notification fragment
 * This class inflates fragment_notifications
 */
public class NotificationsFragment extends Fragment {

    private FirebaseFirestore db;
    private CurrentUser curUser;
;
    private ListView notification_listview;

    public NotificationsFragment(){
        // require a empty public constructor
    }

    public NotificationsFragment(FirebaseFirestore db, CurrentUser curUser) {
        this.db = db;
        this.curUser = curUser;
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
        NotificationFragmentAdapter adapter = new NotificationFragmentAdapter(
                getContext(),
                100,
                curUser.getNotifications(),
                curUser,
                db
        );
//        NotificationFragmentAdapter adapter = new NotificationFragmentAdapter(
//                getContext(),
//                100
//        );
        notification_listview.setAdapter(adapter);




        return view;
    }
}