package com.example.eventlottery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Invited List Array Adapter
 */
public class InvitedListArrayAdapter extends ArrayAdapter {
    /**
     * Constructor for InvitedListArrayAdapter
     * @param context The context of the application
     * @param resource resource
     */
    public InvitedListArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    private ArrayList<UsersList> chosenEntrants;
    private EventModel event;
    FirebaseFirestore db;

    /**
     * Constructor for InvitedListArrayAdapter
     * @param context The context of the application
     * @param entrants The list of users to be displayed
     * @param event The event model
     * @param db Firebase Firestore
     */
    public InvitedListArrayAdapter(Context context, ArrayList<UsersList> entrants, EventModel event, FirebaseFirestore db) {
        super(context, 0, entrants);
        this.chosenEntrants = entrants;
        this.event = event;
        this.db = db;
    }

    /**
     * Get View override
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_listview_content, parent, false);
        }

        UsersList entrant = chosenEntrants.get(position);

        // Set up the views in chosen_entrant_item.xml
        TextView entrantName = convertView.findViewById(R.id.listview_user_name);
        entrantName.setText(entrant.getName());

        Button inviteButton = convertView.findViewById(R.id.listview_send_invite_button);
        inviteButton.setVisibility(View.GONE);
        Button removeButton = convertView.findViewById(R.id.listview_remove_button);
        // Set up remove button functionality
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    event.unqueueInvitedList(entrant);
                    event.queueCancelledList(entrant);
                    db.collection("events").document(event.getEventID()).set(event);
                }
                catch (Exception e) {
                    Log.e("Event Queue/Unqueue Error", "Error: " + e);
                }
            }
        });
        return convertView;
    }
}
