package com.example.eventlottery.Organizer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class is the ChosenEntrantsAdapter
 */
public class ChosenEntrantsAdapter extends ArrayAdapter<RemoteUserRef> {

    private EventModel event;
    private ArrayList<RemoteUserRef> entrantsListCopy;
    private FirebaseFirestore db;
    private ChosenListActivity chosenListActivity;

    /**
     * Constructor for the ArrayAdapter which sets the given ArrayList as the list of items to show in the ListView
     * @param context The context that contains the ListView that will use the ArrayAdapter
     * @param chosenList The ArrayList to display in the ListView
     */
    public ChosenEntrantsAdapter(Context context, ArrayList<RemoteUserRef> chosenList) {
        super(context, 0, chosenList);
    }


    /**
     * An interface for the implementation of the remove button
     */
    // The interface implementation for ArrayAdapter is adapted from https://stackoverflow.com/questions/14822902/how-to-call-main-activitys-function-from-custom-arrayadapter
    public interface ChosenEntrantsAdapterCallback {
        public void removeChosenEntrant(RemoteUserRef entrant);
    }

    private ChosenEntrantsAdapterCallback callback;


    /**
     * Sets a callback reference for the interface.
     * Note that the function(s) in the interface must be implemented, and setCallback must
     * be called to properly set up the interface.
     * @param callback The Activity or Fragment that implements the interface
     */
    public void setCallback(ChosenEntrantsAdapterCallback callback) {
        this.callback = callback;
    }


    /**
     * Get view override
     * @param position The position of the item within the adapter's data set of the item whose view
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chosen_entrant_item, parent, false);
        }

        RemoteUserRef entrant = getItem(position);

        // Set up the views in chosen_entrant_item.xml
        TextView entrantName = convertView.findViewById(R.id.entrant_name);
        entrantName.setText(entrant.getName());

        Button inviteButton = convertView.findViewById(R.id.send_invite_button);
        Button removeButton = convertView.findViewById(R.id.remove_button);

        inviteButton.setVisibility(View.GONE);

        // Set up remove button functionality
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.removeChosenEntrant(entrant);
            }
        });

        return convertView;
    }
}
