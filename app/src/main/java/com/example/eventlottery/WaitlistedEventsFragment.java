package com.example.eventlottery;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class is the Waitlisting Fragment
 * This class inflates fragment_waitlisted_events
 */
public class WaitlistedEventsFragment extends Fragment{

    private String token_m;
    private Button button;
    private TextView textView;
    private Button notification_btn;
    public WaitlistedEventsFragment(){
        // require a empty public constructor
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
        View rootview = inflater.inflate(R.layout.fragment_waitlisted_events, container, false);
        button = (Button) rootview.findViewById(R.id.Button_id);
        textView = (TextView) rootview.findViewById(R.id.textview_id);
        notification_btn = (Button) rootview.findViewById(R.id.notification_button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        Log.e("token","" + task.getResult());
                        textView.setText(task.getResult());
                        token_m = task.getResult();

                    }
                });

            }
        });

        notification_btn.setOnClickListener(view -> {
            Context test_context = requireContext();
            if (test_context == null){Log.e("Context","It's null");}
            FcmNotificationSender fcmNotificationSender = new FcmNotificationSender(
                    token_m,
                    "This is the test Title","Testing message", test_context
            );
            fcmNotificationSender.SendNotifications();

        });





        return rootview;
    }

}