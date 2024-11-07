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

public class CancelledListArrayAdapter extends ArrayAdapter<UsersList> {
    private String state;
    private ArrayList<UsersList> list;
    private EventModel event;
    private FirebaseFirestore db;

    public CancelledListArrayAdapter(@NonNull Context context, int resource, ArrayList<UsersList> list, String state, EventModel event, FirebaseFirestore db) {
        super(context, resource, list);
        this.list = list;
        this.state = state;
        this.event = event;
        this.db = db;
    }

    public CancelledListArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
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
        UsersList user = getItem(position);
        TextView userName = view.findViewById(R.id.listview_user_name);
        userName.setText(user.getName());
        Button sendInviteButton = view.findViewById(R.id.listview_send_invite_button);
        Button removeButton = view.findViewById(R.id.listview_remove_button);
        sendInviteButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);
        return view;
    }
}