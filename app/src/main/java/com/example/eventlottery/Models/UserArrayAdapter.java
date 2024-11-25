package com.example.eventlottery.Models;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventlottery.R;

import java.util.ArrayList;


/**
 * Adapter of a array of UserModel objects
 * This class is an adapter the manage an array of UserModel objects to be show in a specific
 * way in a listview
 */
public class UserArrayAdapter extends ArrayAdapter<UserModel> {
    public UserArrayAdapter(Context context, ArrayList<UserModel> users) {
        super(context, 0, users);
    }

    /**
     * it generates the view of the elements of the array
     * @param pos The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     *      The view that shows the elements of the array in the desired way
     */
    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.admin_user_listview_content, null);
        } else {
            view = convertView;
        }
        UserModel user = getItem(pos);
        TextView nameview = view.findViewById(R.id.listview_user_name);
        nameview.setText(user.getfName() + " " + user.getlName());

        return view;
    }
}
