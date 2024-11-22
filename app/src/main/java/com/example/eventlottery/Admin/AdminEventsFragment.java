package com.example.eventlottery.Admin;

import android.content.Context;

import android.content.Intent;
import android.widget.ArrayAdapter;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.EventsArrayAdapter;
import com.example.eventlottery.R;

import java.util.ArrayList;




public class AdminEventsFragment extends AdminGenericFragment<EventModel> {
    public AdminEventsFragment() {
        super(
                EventModel.class,
                R.layout.admin_current_event_list,
                R.id.current_events_page_events_listview,
                "events",
                true);
    }

    @Override
    public void handleClick(EventModel item) {
        Intent i = new Intent(getActivity(), AdminEventDetailsActivity.class);
        i.putExtra("item", item);
        startActivity(i);
    }

    @Override
    public boolean match(EventModel item, String search) {
        return item.getEventTitle().contains(search);
    }

    @Override
    public ArrayAdapter<EventModel> adaptorFactory(Context context, ArrayList<EventModel> items) {
        return new EventsArrayAdapter(context, items);
    }

}