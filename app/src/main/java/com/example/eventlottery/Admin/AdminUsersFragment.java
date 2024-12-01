
package com.example.eventlottery.Admin;

import android.content.Context;

import android.widget.ArrayAdapter;

import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.UserArrayAdapter;
import com.example.eventlottery.R;

import java.util.ArrayList;



/**
 * This class create a view where the user can see the list of entrants, it can use the search bar
 * to check for specific Entrants and by clicking one event be able to see it details by open a
 * new fragment
 */
public class AdminUsersFragment extends AdminGenericFragment<UserModel> {

    /**
     * Constructor of AdminUsersFragment
     * it is a subclass of the superclass AdminGenericFragment
     */
    public AdminUsersFragment() {
        super(
                UserModel.class,
                R.layout.admin_user_list,
                R.id.admin_user_list_page_listview,
                "users",
                true);
    }

    /**
     * This method handle the moment the user click and entrants on the listview
     * after the click, a new fragment in open with the details of the entrants
     * @param item
     *      This is the entrant on the listview the user clicked
     */
    @Override
    public void handleClick(UserModel item) {
        new AdminUserDetailsFragment(item).show(getParentFragmentManager(), "Admin user view");
    }

    /**
     * This call the adapter to show the elements on the listview
     * @param context
     *      The current state of the application
     * @param items
     *      The arraylist with UserModel objects (entrants) to display on the listview
     * @return
     *      It return the facility array adapter class, to display the entrants on the listview
     *      in the desired way
     */
    @Override
    public ArrayAdapter<UserModel> adaptorFactory(Context context, ArrayList<UserModel> items) {
        return new UserArrayAdapter(context, items);
    }

    /**
     * This method interact with the search bar
     * and search to match what the user input in the search bar
     * with the entrants on the list view, the purpose
     * is to show in the listview the entrants that match the search bar input
     * @param item
     *      the facility inside the listview
     * @param search
     *      the input the user put in the search bar
     * @return
     *      return true if the entrant on the listview match with input on the search bar
     *      in the contrary returns false
     */
    @Override
    public boolean match(UserModel item, String search) {
        return item.getfName().contains(search) || item.getlName().contains(search);
    }
}