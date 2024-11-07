package com.example.eventlottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ChosenEntrantsAdapter extends ArrayAdapter<UsersList> {

    private ArrayList<UsersList> chosenEntrants;
    private EventModel event;

    public ChosenEntrantsAdapter(Context context, ArrayList<UsersList> entrants) {
        super(context, 0, entrants);
        this.chosenEntrants = entrants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chosen_entrant_item, parent, false);
        }

        UsersList entrant = chosenEntrants.get(position);

        // Set up the views in chosen_entrant_item.xml
        TextView entrantName = convertView.findViewById(R.id.entrant_name);
        entrantName.setText(entrant.getName());

        Button inviteButton = convertView.findViewById(R.id.send_invite_button);
        Button removeButton = convertView.findViewById(R.id.remove_button);

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
                chosenEntrants.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
