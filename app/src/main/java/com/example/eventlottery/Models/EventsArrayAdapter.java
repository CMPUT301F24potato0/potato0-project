package com.example.eventlottery.Models;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventlottery.R;

import java.util.ArrayList;
/*
Most of the code from lab 3.
 */

/**
 * Events Array Adapter
 * This class is an adapter that manage the listview of events to configure the view of the elements
 * on the listview in a specific way
 */
public class EventsArrayAdapter extends ArrayAdapter<EventModel> {

    /**
     * Constructor for EventsArrayAdapter
     * @param context The context, current state of the app
     * @param events An ArrayList of EventModel objects
     */
    public EventsArrayAdapter(Context context, ArrayList<EventModel> events) {
        super(context, 0, events);
    }

    /**
     * Get view override
     * This method create the view of the elements in the listview in the desire way, so the user
     * can see the events that exists
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
        CharSequence timeFormat  = DateFormat.format("MMMM d, yyyy ", event.getJoinDeadline().getTime());
        eventDate.setText(timeFormat);

        return view;
    }
}
