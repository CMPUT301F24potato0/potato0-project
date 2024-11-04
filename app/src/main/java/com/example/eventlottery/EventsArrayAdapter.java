package com.example.eventlottery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
/*
Most of the code from lab 3.
 */
public class EventsArrayAdapter extends ArrayAdapter<EventModel> {
    public EventsArrayAdapter(Context context, ArrayList<EventModel> events) {
        super(context, 0, events);
    }

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
        eventFacility.setText(event.getFacilityID());
        eventDate.setText(event.getJoinDeadline().toString());

        return view;
    }
}
