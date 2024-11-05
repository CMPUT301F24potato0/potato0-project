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

import java.util.ArrayList;

public class UserListviewAdapter extends ArrayAdapter<UsersList> {

    public UserListviewAdapter(Context context, ArrayList<UsersList> events) {
        super(context, 0, events);
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
        sendInviteButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);
        return view;
    }
}