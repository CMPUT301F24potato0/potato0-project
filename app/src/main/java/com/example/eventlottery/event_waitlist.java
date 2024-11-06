package com.example.eventlottery;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 *
 */
public class event_waitlist extends Fragment {

    private Button notify;
    private Button backButton;

    public event_waitlist() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_waitlist, container, false);

        notify = rootView.findViewById(R.id.notify_btn_id);
        notify.setOnClickListener(view -> {
            // Existing notify button logic
        });

//        // Back button functionality
//        backButton = rootView.findViewById(R.id.back_button);
//        backButton.setOnClickListener(view -> {
//            // Navigate back to EventOrganizerActivity
//            Intent intent = new Intent(getActivity(), EventOrganizerActivity.class);
//            startActivity(intent);
//            if (getActivity() != null) {
//                getActivity().finish();
//            }
//        });

        return rootView;
    }
}
