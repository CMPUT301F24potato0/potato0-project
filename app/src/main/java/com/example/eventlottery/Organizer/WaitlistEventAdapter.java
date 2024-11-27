package com.example.eventlottery.Organizer;

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
 */
public class WaitlistEventAdapter extends ArrayAdapter<RemoteUserRef> {
    private ArrayList<RemoteUserRef> waitList;
    private ArrayList<RemoteUserRef> cancelList;
    private EventModel event;
    private FirebaseFirestore db;
    private TextView waitListCount;

    /**
     * This is the constructor for WaitlistEventAdapter
     * @param context The context
     * @param resource The resource
     */
    public WaitlistEventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     * This is the constructor for WaitlistEventAdapter
     * @param context The context
     * @param resource The resource
     */
    public WaitlistEventAdapter(@NonNull Context context,
                                int resource,
                                EventModel event,
                                FirebaseFirestore db,
                                TextView waitListCount) {
        super(context, resource, event.getWaitingList());
        this.waitList = event.getWaitingList();
        this.cancelList = event.getCancelledList();
        this.event = event;
        this.db = db;
        this.waitListCount = waitListCount;
    }

    /**
     * getView override
     * @param position The position
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
        assert user != null;
        user.sync(() -> {userName.setText(user.getName());});

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    event.unqueueWaitingList(user);

                    event.queueCancelledList(user);

                    db.collection("events").document(event.getEventID()).set(event);
                    waitListCount.setText(event.getWaitingList().size() + "");
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
