
package com.example.eventlottery.Admin;

import android.content.Context;

import android.widget.ArrayAdapter;

import com.example.eventlottery.Models.CurrentUser;
import com.example.eventlottery.Models.UserArrayAdapter;
import com.example.eventlottery.R;

import java.util.ArrayList;




public class AdminUsersFragment extends AdminGenericFragment<CurrentUser> {
    public AdminUsersFragment() {
        super(
                CurrentUser.class,
                R.layout.admin_user_list,
                R.id.admin_user_list_page_listview,
                "users",
                true);
    }

    @Override
    public void handleClick(CurrentUser item) {
        new AdminUserDetailsFragment(item).show(getParentFragmentManager(), "Admin user view");
    }

    @Override
    public ArrayAdapter<CurrentUser> adaptorFactory(Context context, ArrayList<CurrentUser> items) {
        return new UserArrayAdapter(context, items);
    }

    @Override
    public boolean match(CurrentUser item, String search) {
        return item.getfName().contains(search) || item.getlName().contains(search);
    }
}