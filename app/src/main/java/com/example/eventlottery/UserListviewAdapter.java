package com.example.eventlottery;

import android.content.Context;
import android.media.metrics.Event;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

/**
 * This class is the UserListviewAdapter
 */
public class UserListviewAdapter extends ArrayAdapter<UsersList> {
    private String state;
    private ArrayList<UsersList> list;
    private EventModel event;
    private FirebaseFirestore db;

    public UserListviewAdapter(@NonNull Context context, int resource, ArrayList<UsersList> list) {
        super(context, resource, list);
    }

    public UserListviewAdapter(@NonNull Context context, int resource, ArrayList<UsersList> list, String state, EventModel event, FirebaseFirestore db) {
        super(context, resource, list);
        this.list = list;
        this.state = state;
        this.event = event;
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.user_listview_content, null);
        }
        else {
            view = convertView;
        }
        UsersList user = getItem(position);
        TextView userName = view.findViewById(R.id.listview_user_name);
        userName.setText(user.getName());
        Button sendInviteButton = view.findViewById(R.id.listview_send_invite_button);
        Button removeButton = view.findViewById(R.id.listview_remove_button);
        if (state == "chosen") {
            sendInviteButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO make sure when accepting the event they get removed only get added to the wait list
                    event.queueCancelledList(user);
//                    removeFromList(user, list);
                    db.collection("events").document(event.getEventID()).set(event);
                }
            });
        } else if (state == "cancelled") {
            sendInviteButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.VISIBLE);

        } else if (state == "enrolled") {
            sendInviteButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);
        }

        return view;
    }
    private void removeFromList(UsersList user, ArrayList<UsersList> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(user.getName())) {
                list.remove(i);
                notifyDataSetChanged();
            }
        }
    }
}