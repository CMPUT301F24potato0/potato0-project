package com.example.eventlottery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class WaitlistEventAdapter extends ArrayAdapter<UsersList> {
    private ArrayList<UsersList> list;
    private EventModel event;
    private FirebaseFirestore db;

    public WaitlistEventAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public WaitlistEventAdapter(@NonNull Context context, int resource, ArrayList<UsersList> list, EventModel event, FirebaseFirestore db) {
        super(context, resource, list);
        this.list = list;
        this.event = event;
        this.db = db;
    }

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
        UsersList user = getItem(position);
        TextView userName = view.findViewById(R.id.listview_user_name);
        userName.setText(user.getName());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.unqueueWaitingList(user);
                removeFromList(user, list);

                db.collection("events").document(event.getEventID()).set(event);
            }
        });
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