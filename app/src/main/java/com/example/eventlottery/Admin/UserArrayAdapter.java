package com.example.eventlottery.Admin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventlottery.CurrentUser;
import com.example.eventlottery.FacilityModel;
import com.example.eventlottery.R;

import java.util.ArrayList;

public class UserArrayAdapter extends ArrayAdapter<CurrentUser> {
    public UserArrayAdapter(Context context, ArrayList<CurrentUser> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.admin_user_listview_content, null);
        } else {
            view = convertView;
        }
        CurrentUser user = getItem(pos);
        TextView nameview = view.findViewById(R.id.listview_user_name);
        nameview.setText(user.getfName() + " " + user.getlName());

        return view;
    }
}
