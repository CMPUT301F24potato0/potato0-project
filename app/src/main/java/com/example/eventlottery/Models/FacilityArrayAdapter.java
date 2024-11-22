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

public class FacilityArrayAdapter extends ArrayAdapter<FacilityModel> {

    public FacilityArrayAdapter(Context context, ArrayList<FacilityModel> facilities) {
        super(context, 0, facilities);
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(getContext(), R.layout.admin_facility_listview, null);
        } else {
            view = convertView;
        }
        FacilityModel facility = getItem(pos);
        TextView facTitle = view.findViewById(R.id.facility_name);
        facTitle.setText(facility.getName());

        return view;
    }
}

