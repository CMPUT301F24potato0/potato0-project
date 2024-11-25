package com.example.eventlottery.Admin;

import android.content.Context;

import android.content.Intent;
import android.widget.ArrayAdapter;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.EventsArrayAdapter;
import com.example.eventlottery.R;

import java.util.ArrayList;


/**
 * This class create a view where the user can see the list of events, it can use the search bar
 * to check for specific events and by clicking one event be able to see it details by open a new
 * activity
 */
public class AdminEventsFragment extends AdminGenericFragment<EventModel> {
    /**
     * Constructor of AdminEventsFragment
     * it is a subclass of the superclass AdminGenericFragment
     */
    public AdminEventsFragment() {
        super(
                EventModel.class,
                R.layout.admin_current_event_list,
                R.id.current_events_page_events_listview,
                "events",
                true);
    }

    /**
     * This method handle the moment the user click and event on the listview
     * after the click, a new activity in open with the details of the event
     * @param item
     *      This is the event on the listview the user clicked
     */
    @Override
    public void handleClick(EventModel item) {
        Intent i = new Intent(getActivity(), AdminEventDetailsActivity.class);
        i.putExtra("item", item);
        startActivity(i);
    }

    /**
     * This method interact with the search bar
     * and search to match what the user input in the search bar
     * with the events on the list view the purpose
     * is to show in the listview the events that match the search bar input
     * @param item
     *      the event inside the listview
     * @param search
     *      the input the user put in the search bar
     * @return
     *      return true if the event on the listview match with input on the search bar
     *      in the contrary returns false
     */
    @Override
    public boolean match(EventModel item, String search) {
        return item.getEventTitle().contains(search);
    }

    /**
     * This call the adapter to show the elements on the listview
     * @param context
     *      The current state of the application
     * @param items
     *      The arraylist with EventModel objects (events) to display on the listview
     * @return
     *      It return the events array adapter class, to display the events on the listview
     *      in the desired way
     */
    @Override
    public ArrayAdapter<EventModel> adaptorFactory(Context context, ArrayList<EventModel> items) {
        return new EventsArrayAdapter(context, items);
    }

}
