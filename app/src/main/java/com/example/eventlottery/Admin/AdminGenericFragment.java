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

/**
 * Generic fragment to create a fragment with a search bar and listview to view elements of the
 * desired type, it is a superclass that make that it subclass can personalize the type of elements
 * to display on the list and then use their features
 * @param <T>
 *     the type of the object the listview is going to take, the objects are classes (Entrants,
 *     Events and Facilities)
 */
abstract public class AdminGenericFragment<T extends Serializable> extends Fragment {

    protected final FirebaseFirestore db;

    private final Class<T> typeParameterClass;
    private final int layout;
    private final int listViewID;
    private final String collection;
    private final boolean doSearch;
    private ArrayAdapter<T> adapter;
    private String search;

    /**
     * Constructor of AdminGenericFragment
     * @param typeParameterClass
     *      The type of the elements this class is going to manage
     * @param layout
     *      What layout is going to be use
     * @param listViewID
     *      ID of the listview so the program can know what information is going to be use
     * @param collection
     *      Collection of objects in the firebase to take them and use them
     * @param doSearch
     *      boolean to know if the search match an element on the listview
     */
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

    /**
     *abstract class that purpose is to create and adapter that handle the elemtns of a listview
     * of a desire type
     * @param context
     *      The current state of the application
     * @param items
     *      The arraylist with  objects of a specific type to display on a listview
     * @return
     *      an adapter class object of the desire type
     */
    public abstract ArrayAdapter<T> adaptorFactory(Context context, ArrayList<T> items);

    /**
     * This method handle the moment the user click an object on the listview
     * @param item
     *      The object that is click in the listview
     */
    public abstract void handleClick(T item);

    /**
     *It creates the view that show the listview with it elements and a search bar to be able to
     * search for specific elements of the listview
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     *      it return a rootView, showing the view of the desired fragment with it features
     *      available
     */
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

    /**
     *
     * @param items
     *      Elements on the listview
     * @param itemsFiltered
     *      Items that are left out after the search
     * @param search
     *      String that show the input inside the search bar that the user input
     */
    private void filter_into(ArrayList<T> items, ArrayList<T> itemsFiltered, String search) {
        itemsFiltered.clear();
        for (T item : items) {
            if (match(item, search)) {
                itemsFiltered.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * This method interact with the search bar
     * and search to match what the user input in the search bar
     * ith the objects on the list view, to see if the 2 match or not
     * @param item
     *      the object inside the listview
     * @param search
     *      the input the user put in the search bar
     * @return
     *      return true if the object on the listview match with input on the search bar
     *      in the contrary returns false
     */
    public abstract boolean match(T item, String search);
}