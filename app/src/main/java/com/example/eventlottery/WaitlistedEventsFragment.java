package com.example.eventlottery;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class is the Waitlisting Fragment
 * This class inflates fragment_waitlisted_events
 */
public class WaitlistedEventsFragment extends Fragment{


    private Button subscribe;
    private Button notification_btn;
    private Context context;
    private Button mute_btn;
    private Button unmute_btn;

    /**
     * This is the empty constructor
     */
    public WaitlistedEventsFragment(){
        // require a empty public constructor
    }

    /**
     * oncreateview override
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
        View rootview = inflater.inflate(R.layout.fragment_waitlisted_events, container, false);

        notification_btn = (Button) rootview.findViewById(R.id.notification_button_id);
        subscribe = (Button) rootview.findViewById(R.id.Button_id);
        mute_btn = (Button) rootview.findViewById(R.id.mute);
        unmute_btn = (Button) rootview.findViewById(R.id.unmute);

        notification_btn.setOnClickListener(view -> {
            // CAlLS SendNotification
            this.context = requireContext();
            String topic = "testTopic_signup";
//            SendNotification sendNotification = new SendNotification(context,topic);
//            sendNotification.popup();
            // CALLS

        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = "testTopic_signup";
                SubscribeToTopic subscribeToTopic = new SubscribeToTopic(topic,context);
                subscribeToTopic.subscribe();
            }
        });
        return rootview;
    }

}