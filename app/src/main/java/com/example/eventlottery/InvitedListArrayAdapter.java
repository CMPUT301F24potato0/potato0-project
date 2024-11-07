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
        Button removeButton = convertView.findViewById(R.id.listview_remove_button);

        // Set up invite button functionality (optional, to be implemented later)
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to send invite notification
            }
        });

        // Set up remove button functionality
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    event.unqueueInvitedList(entrant);
                    event.queueCancelledList(entrant);
                    chosenEntrants.remove(position);
                    notifyDataSetChanged();
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
