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

import com.example.eventlottery.EventModel;
import com.example.eventlottery.EventsArrayAdapter;
import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;




public class AdminEventsFragment extends AdminGenericFragment<EventModel> {
    public AdminEventsFragment(FirebaseFirestore db) {
        super(db);
        typeParameterClass = EventModel.class;
        layout = R.layout.admin_current_event_list;
        listViewID = R.id.current_events_page_events_listview;
        collection = "events";
    }

    @Override
    public ArrayAdapter<EventModel> adaptorFactory(Context context, ArrayList<EventModel> items) {
        return new EventsArrayAdapter(context, items);
    }

}