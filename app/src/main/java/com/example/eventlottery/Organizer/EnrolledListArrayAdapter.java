package com.example.eventlottery.Organizer;

import android.content.Context;
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
 * Enrolled List Array Adapter
 * It manages the entrants that are in the array to be displayed in the list in a specific way
 */
public class EnrolledListArrayAdapter extends ArrayAdapter<RemoteUserRef> {
    private String state;
    private ArrayList<RemoteUserRef> list;
    private EventModel event;
    private FirebaseFirestore db;

    /**
     * Constructor for EnrolledListArrayAdapter
     * @param context The context of the application
     * @param resource resource
     * @param list The list of users to be displayed
     * @param state The state of the list (enrolled or waiting)
     * @param event The event model
     * @param db Firebase Firestore
     */
    public EnrolledListArrayAdapter(@NonNull Context context, int resource, ArrayList<RemoteUserRef> list, String state, EventModel event, FirebaseFirestore db) {
        super(context, resource, list);
        this.list = list;
        this.state = state;
        this.event = event;
        this.db = db;
    }

    /**
     * Constructor for EnrolledListArrayAdapter
     * @param context The context of the application
     * @param resource resource
     */
    public EnrolledListArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    /**
     * Overriding getView
     * This method get view of the adapter make to display the entrants in the enrolled list
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view for the whole array in the listview
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
        RemoteUserRef user = getItem(position);
        TextView userName = view.findViewById(R.id.listview_user_name);
        userName.setText(user.getName());
        Button sendInviteButton = view.findViewById(R.id.listview_send_invite_button);
        Button removeButton = view.findViewById(R.id.listview_remove_button);
        sendInviteButton.setVisibility(View.GONE);
        removeButton.setVisibility(View.GONE);
        return view;
    }
}