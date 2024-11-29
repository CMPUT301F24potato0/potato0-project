package com.example.eventlottery.Entrant;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.eventlottery.MainActivity;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.EventsArrayAdapter;
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Notifications.SubscribeToTopic;
import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This class is the Waitlisting Fragment
 * This class inflates fragment_waitlisted_events
 * Not being used currently. Planned to be used in part 4
 */
public class EntrantEventsFragment extends Fragment{


    public EntrantEventsFragment(){
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
        View rootview = inflater.inflate(R.layout.fragment_entrant_events, container, false);
        ListView events_list = rootview.findViewById(R.id.events_listview);
        ArrayList<EventModel> events = new ArrayList<>();
        EventsArrayAdapter adapter = new EventsArrayAdapter(requireContext(), events);
        events_list.setAdapter(adapter);
        FirebaseFirestore.getInstance().collection("events")
                .whereArrayContains("entrantIDs", MainActivity.curUser.getiD())
                .get().addOnCompleteListener(task -> {
                    events.clear();
                    for (QueryDocumentSnapshot event : task.getResult())
                        events.add(event.toObject(EventModel.class));
                    adapter.notifyDataSetChanged();
                });
        events_list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (adapter.getItem(position) != null) {
                EventModel item = (EventModel) adapter.getItem(position);
                Intent i = new Intent(getActivity(), EventEntrantActivity.class);
                RemoteUserRef userList = new RemoteUserRef(MainActivity.curUser.getiD(), MainActivity.curUser.getfName() + " " + MainActivity.curUser.getlName());
                i.putExtra("userList", userList);
                i.putExtra("eventModel", item);
                startActivity(i);
            }
        });
        return rootview;
    }
}