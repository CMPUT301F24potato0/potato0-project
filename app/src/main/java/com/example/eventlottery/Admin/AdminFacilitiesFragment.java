
package com.example.eventlottery.Admin;

import android.content.Context;

import android.widget.ArrayAdapter;

import com.example.eventlottery.Models.FacilityArrayAdapter;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.R;

import java.util.ArrayList;



/**
 * This class create a view where the user can see the list of facilities, it can use the search bar
 * to check for specific facilities and by clicking one event be able to see it details by open a
 * new fragment
 */
public class AdminFacilitiesFragment extends AdminGenericFragment<FacilityModel> {

    /**
     * Constructor of AdminFacilitiesFragment
     * it is a subclass of the superclass AdminGenericFragment
     */
    public AdminFacilitiesFragment() {
        super(
                FacilityModel.class,
                R.layout.admin_facility_list,
                R.id.facility_list,
                "facilities",
                true);
    }

    /**
     * This method handle the moment the user click and facility on the listview
     * after the click, a new fragment in open with the details of the facility
     * @param item
     *      This is the facility on the listview the user clicked
     */
    @Override
    public void handleClick(FacilityModel item) {
        new AdminFacilityDetailsFragment(item).show(getParentFragmentManager(), "Admin facility view");
    }

    /**
     * This method interact with the search bar
     * and search to match what the user input in the search bar
     * with the facilities on the list view, the purpose
     * is to show in the listview the facilities that match the search bar input
     * @param item
     *      the facility inside the listview
     * @param search
     *      the input the user put in the search bar
     * @return
     *      return true if the facility on the listview match with input on the search bar
     *      in the contrary returns false
     */
    @Override
    public boolean match(FacilityModel item, String search) {
        return item.getName().contains(search);
    }

    /**
     * This call the adapter to show the elements on the listview
     * @param context
     *      The current state of the application
     * @param items
     *      The arraylist with FacilityModel objects (facilities) to display on the listview
     * @return
     *      It return the facility array adapter class, to display the facilities on the listview
     *      in the desired way
     */
    @Override
    public ArrayAdapter<FacilityModel> adaptorFactory(Context context, ArrayList<FacilityModel> items) {
        return new FacilityArrayAdapter(context, items);
    }

}