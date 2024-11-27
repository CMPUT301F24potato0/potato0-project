package com.example.eventlottery.Admin;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventlottery.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;


abstract public class AdminGenericFragment<T extends Serializable> extends Fragment {

    protected final FirebaseFirestore db;

    private final Class<T> typeParameterClass;
    private final int layout;
    private final int listViewID;
    private final String collection;
    private final boolean doSearch;
    private ArrayAdapter<T> adapter;
    private String search;

    public AdminGenericFragment(
            Class<T> typeParameterClass,
            int layout,
            int listViewID,
            String collection,
            boolean doSearch) {
        this.db = FirebaseFirestore.getInstance();
        this.typeParameterClass = typeParameterClass;
        this.layout = layout;
        this.listViewID = listViewID;
        this.collection = collection;
        this.doSearch = doSearch;
        search = "";
    }

    public abstract ArrayAdapter<T> adaptorFactory(Context context, ArrayList<T> items);

    public abstract void handleClick(T item);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(layout, container, false);
        ListView listView = rootView.findViewById(listViewID);
        ArrayList<T> items = new ArrayList<T>();
        ArrayList<T> items_filtered = new ArrayList<T>();
        adapter = adaptorFactory(requireContext(), items_filtered);
        listView.setAdapter(adapter);

        CollectionReference collectRef = db.collection(collection);
        collectRef.addSnapshotListener((@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) -> {
            if (e != null) {
                Log.w("FIREBASE", "Listen failed.", e);
                return;
            }
            adapter.clear();
            items.clear();
            for (QueryDocumentSnapshot doc : value) items.add(doc.toObject(typeParameterClass));
            filter_into(items, items_filtered, search);
        });
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                if (listView.getAdapter().getItem(position) != null) {
                    T item = (T) listView.getAdapter().getItem(position);
                    handleClick(item);
                }
        });
        if (doSearch) {
            ((EditText)rootView.findViewById(R.id.search_bar)).addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    search = s.toString();
                    filter_into(items, items_filtered, search);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        return rootView;
    }

    private void filter_into(ArrayList<T> items, ArrayList<T> itemsFiltered, String search) {
        itemsFiltered.clear();
        for (T item : items) {
            if (match(item, search)) {
                itemsFiltered.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public abstract boolean match(T item, String search);
}