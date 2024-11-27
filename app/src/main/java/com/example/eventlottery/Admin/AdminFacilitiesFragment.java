
package com.example.eventlottery.Admin;

import android.content.Context;

import android.widget.ArrayAdapter;

import com.example.eventlottery.Models.FacilityArrayAdapter;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;

import java.util.ArrayList;




public class AdminFacilitiesFragment extends AdminGenericFragment<FacilityModel> {
    public AdminFacilitiesFragment() {
        super(
                FacilityModel.class,
                R.layout.admin_facility_list,
                R.id.facility_list,
                "facilities",
                true);
    }

    @Override
    public void handleClick(FacilityModel item) {
        new AdminFacilityDetailsFragment(item).show(getParentFragmentManager(), "Admin facility view");
    }

    @Override
    public boolean match(FacilityModel item, String search) {
        return item.getName().contains(search);
    }

    @Override
    public ArrayAdapter<FacilityModel> adaptorFactory(Context context, ArrayList<FacilityModel> items) {
        return new FacilityArrayAdapter(context, items);
    }

}