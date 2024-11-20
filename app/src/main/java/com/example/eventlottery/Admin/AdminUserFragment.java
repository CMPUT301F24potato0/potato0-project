
package com.example.eventlottery.Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eventlottery.CurrentUser;
import com.example.eventlottery.EventModel;
import com.example.eventlottery.EventsArrayAdapter;
import com.example.eventlottery.FacilityModel;
import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;




public class AdminUserFragment extends AdminGenericFragment<CurrentUser> {
    public AdminUserFragment(FirebaseFirestore db) {
        super(db);
        typeParameterClass = CurrentUser.class;
        layout = R.layout.admin_user_list;
        listViewID = R.id.admin_user_list_page_listview;
        collection = "users";
    }

    @Override
    public ArrayAdapter<CurrentUser> adaptorFactory(Context context, ArrayList<CurrentUser> items) {
        return new UserArrayAdapter(context, items);
    }

}