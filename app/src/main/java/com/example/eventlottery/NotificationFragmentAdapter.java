package com.example.eventlottery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationFragmentAdapter extends ArrayAdapter<CurrentUser> {


    private CurrentUser currentUser;
    private FirebaseFirestore db;

    public NotificationFragmentAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public NotificationFragmentAdapter(@NonNull Context context, int resource, CurrentUser currentUser, FirebaseFirestore db) {
        super(context, resource);
        this.currentUser = currentUser;
        this.db = db;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.notification_adapter_view, null);
        } else {
            view = convertView;
        }

        // get the notification from the data base using db and curUser
        TextView title = (TextView) view.findViewById(R.id.title_id);
        TextView message = (TextView) view.findViewById(R.id.message_id);
        TextView eventID = (TextView) view.findViewById(R.id.envent_id);
        TextView flagID = (TextView) view.findViewById(R.id.flag_id);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_id);
        Button signup_btn = (Button) view.findViewById(R.id.signup_id);
        CurrentUser user = getItem(position);
        ArrayList<HashMap<String, String>> notification = user.getNotifications();
        title.setText((CharSequence) notification.get(Integer.parseInt("title")));
        message.setText((CharSequence) notification.get(Integer.parseInt("body")));
        eventID.setText((CharSequence) notification.get(Integer.parseInt("eventID")));
        flagID.setText((CharSequence) notification.get(Integer.parseInt("flag")));

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_btn.setVisibility(View.GONE);
                signup_btn.setVisibility(View.GONE);

            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_btn.setVisibility(View.GONE);
                signup_btn.setVisibility(View.GONE);
                // remove from invited
                // move to enrolled
            }
        });





        // if flag "chose" then button visible
        // button will add user to enrolled, remove from invited, remove button and update data base
        // if button cancel then remove both buttons, move user to canceled list and remove from invited


        return view;
    }
}
