package com.example.eventlottery.Entrant;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventlottery.MainActivity;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.EventsArrayAdapter;
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Notifications.SubscribeToTopic;
import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This class is the Waitlisting Fragment
 * This class inflates fragment_waitlisted_events
 * Not being used currently. Planned to be used in part 4
 */
public class EntrantEventsFragment extends Fragment{

    static RemoteUserRef userRef;
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
        userRef = new RemoteUserRef(MainActivity.curUser.getiD(), MainActivity.curUser.getfName() + " " + MainActivity.curUser.getlName());
        View rootview = inflater.inflate(R.layout.fragment_entrant_events, container, false);
        ListView events_list = rootview.findViewById(R.id.events_listview);
        ArrayList<EventModel> events = new ArrayList<>();
        EventsAdapter adapter = new EventsAdapter(requireContext(), events);
        events_list.setAdapter(adapter);
        FirebaseFirestore.getInstance().collection("events")
                .whereArrayContains("entrantIDs", MainActivity.curUser.getiD())
                .addSnapshotListener((task, t) -> {
                    events.clear();
                    for (DocumentSnapshot eventDoc : task.getDocuments()) {
                        EventModel event = eventDoc.toObject(EventModel.class);
                        if (event != null && !event.checkUserInList(userRef, event.getCancelledList()))
                            events.add(event);
                    };
                    adapter.notifyDataSetChanged();
                });
        events_list.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (adapter.getItem(position) != null) {
                EventModel item = (EventModel) adapter.getItem(position);
                Intent i = new Intent(getActivity(), EventEntrantActivity.class);
                i.putExtra("userList", userRef);
                i.putExtra("eventModel", item);
                startActivity(i);
            }
        });
        return rootview;
    }
}

class EventsAdapter extends ArrayAdapter<EventModel> {

    /**
     * Constructor for EventsArrayAdapter
     * @param context The context
     * @param events An ArrayList of EventModel objects
     */
    public EventsAdapter(Context context, ArrayList<EventModel> events) {
        super(context, 0, events);
    }

    /**
     * Get view override
     * @param pos The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view for the specified position
     */
    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.event_listview_content, null);
        } else {
            view = convertView;
        }
        EventModel event = getItem(pos);
        TextView eventTitle = view.findViewById(R.id.event_listview_title);
        TextView eventFacility = view.findViewById(R.id.event_listview_facility);
        TextView eventDate = view.findViewById(R.id.event_listview_date);
        eventTitle.setText(event.getEventTitle());
        eventFacility.setText(event.getEventStrLocation());
        CharSequence timeFormat = DateFormat.format("MMMM d, yyyy ", event.getJoinDeadline().getTime());
        eventDate.setText("Deadline: " + timeFormat);
        if (event.checkUserInList(EntrantEventsFragment.userRef, event.getEnrolledList())) {
            eventDate.setText("Enrolled");
        } else if (event.checkUserInList(EntrantEventsFragment.userRef, event.getInvitedList())) {
            eventDate.setText("Waiting to accept your invitation");
        }
        return view;
    }
}