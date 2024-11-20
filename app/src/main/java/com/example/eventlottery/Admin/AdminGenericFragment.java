package com.example.eventlottery.Admin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


abstract public class AdminGenericFragment<T> extends Fragment {

    private FirebaseFirestore db;

    private ListView listView;
    private ArrayAdapter<T> adapter;
    private ArrayList<T> items;

    public Class<T> typeParameterClass;
    public int layout;
    public int listViewID;
    public String collection;

    public AdminGenericFragment(FirebaseFirestore db) {
        // Required empty public constructor
        this.db = db;
    }

    public abstract ArrayAdapter<T> adaptorFactory(Context context, ArrayList<T> items);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(layout, container, false);
        listView = rootView.findViewById(listViewID);
        items = new ArrayList<T>();
        adapter = adaptorFactory(requireContext(), items);
        listView.setAdapter(adapter);

        CollectionReference eventsRef = db.collection(collection);
        eventsRef.addSnapshotListener((@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) -> {
            if (e != null) {
                Log.w("FIREBASE", "Listen failed.", e);
                return;
            }
            adapter.clear();
            for (QueryDocumentSnapshot doc : value) items.add(doc.toObject(typeParameterClass));
            adapter.notifyDataSetChanged();
        });
        return rootView;
    }
}