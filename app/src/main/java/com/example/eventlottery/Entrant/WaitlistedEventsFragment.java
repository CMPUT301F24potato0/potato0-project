package com.example.eventlottery.Entrant;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.eventlottery.Notifications.SubscribeToTopic;
import com.example.eventlottery.R;
import com.example.eventlottery.TestingFirebaseStorage;

/**
 * This class is the Waitlisting Fragment
 * This class inflates fragment_waitlisted_events
 * Not being used currently. Planned to be used in part 4
 */
public class WaitlistedEventsFragment extends Fragment{


    private Button subscribe;
    private Button notification_btn;
    private Context context;
    private Button mute_btn;
    private Button unmute_btn;
    Button upload_image_btn;
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

        upload_image_btn = (Button) rootview.findViewById(R.id.open_upload_image);

        notification_btn.setOnClickListener(view -> {
            this.context = requireContext();
            String topic = "testTopic_signup";

        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = "testTopic_signup";
                SubscribeToTopic subscribeToTopic = new SubscribeToTopic(topic,context);
                subscribeToTopic.subscribe();
            }
        });

//        upload_image_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), TestingFirebaseStorage.class);
//                startActivity(intent);
//            }
//        });

        return rootview;
    }

}