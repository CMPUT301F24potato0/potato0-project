package com.example.eventlottery.Entrant;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class is the WaitlistEventAdapter
 * This method is the adapter that manage the entrants in the waiting list on a specific event
 * also the ability to delete the entrants on the waiting list
 * this class configure the elements on the list to look in a specific way
 */
public class WaitlistEventAdapter extends ArrayAdapter<RemoteUserRef> {
    private ArrayList<RemoteUserRef> waitList;
    private ArrayList<RemoteUserRef> cancelList;
    private EventModel event;
    private FirebaseFirestore db;

    /**
     * This is the constructor for WaitlistEventAdapter
     * @param context The context
     *                The current state of the program
     * @param resource
     *                The resource
     */
    public WaitlistEventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     *
     * @param context
     *              The current state of the program
     * @param resource
     *                The resource
     * @param event
     *            The event that the wait list get the entrants from
     * @param db
     *          THe Firebase where the information is collected
     */
    public WaitlistEventAdapter(@NonNull Context context, int resource, EventModel event, FirebaseFirestore db) {
        super(context, resource, event.getWaitingList());
        this.waitList = event.getWaitingList();
        this.cancelList = event.getCancelledList();
        this.event = event;
        this.db = db;
    }

    /**
     * getView override
     * It generates the view of the listview, where the elements (entrants) are displayed
     * in a specific way, also have the ability to remove the user by clicking the button that the
     * adapter view has on each element of the listview (waiting list)
     * @param position The position
     *                 Position on a element in the list
     * @param convertView The convertView
     * @param parent The parent
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.user_listview_content, null);
        } else {
            view = convertView;
        }
        Button remove = view.findViewById(R.id.listview_remove_button);
        Button sendInvite = view.findViewById(R.id.listview_send_invite_button);
        sendInvite.setVisibility(View.GONE);
        RemoteUserRef user = getItem(position);
        TextView userName = view.findViewById(R.id.listview_user_name);
        userName.setText(user.getName());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    event.unqueueWaitingList(user);

                    event.queueCancelledList(user);

                    db.collection("events").document(event.getEventID()).set(event);
                }
                catch (Exception e) {
                    Log.e("Event Queue/Unqueue Error", "Error: " + e);
                }
            }
        });
        return view;
    }

    /**
     * removeFromList override
     * @param user The user
     * @param list The list
     */
    private void removeFromList(RemoteUserRef user, ArrayList<RemoteUserRef> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(user.getName())) {
                list.remove(i);
                notifyDataSetChanged();
            }
        }
    }
}
