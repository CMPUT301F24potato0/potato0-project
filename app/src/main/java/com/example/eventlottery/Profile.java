package com.example.eventlottery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This class is the profile fragment.
 * This class is called when the user clicks the profile button on the bottom.
 */
public class Profile extends Fragment {

    private CurrentUser curUser;
    private FirebaseFirestore db;
    public CollectionReference userRef;

    private Button editUser;

    /**
     * Empty Constructor
     */
    public Profile(){
        // require a empty public constructor
    }

    /**
     * This constructor is used to pass in the instance of CurrentUser
     * @param db This is the database instance
     * @param curUser This is the information about current user which is passed from MainActivity
     */
    public Profile(FirebaseFirestore db, CurrentUser curUser) {
        this.db = db;
        this.curUser = curUser;
    }

    /**
     * This method is called when creating the fragment.
     * This method uses the CurrentUser instance and puts the information if any in the EditText in the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");


        EditText f_name = (EditText) rootView.findViewById(R.id.fNameEditText);
        EditText l_name = (EditText) rootView.findViewById(R.id.lNameEditText);
        EditText email = (EditText) rootView.findViewById(R.id.emailEditText);
        EditText phone = (EditText) rootView.findViewById(R.id.phoneEditText);

        f_name.setText(curUser.getfName());
        l_name.setText(curUser.getlName());
        email.setText(curUser.getEmail());
        phone.setText(curUser.getPhone());

        editUser = (Button) rootView.findViewById(R.id.saveProfileBtn);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String f_nameStr = f_name.getText().toString();
                String l_nameStr = l_name.getText().toString();
                String emailStr = email.getText().toString();
                String phoneStr = phone.getText().toString();

                curUser.setfName(f_nameStr);
                curUser.setlName(l_nameStr);
                curUser.setEmail(emailStr);
                curUser.setPhone(phoneStr);

                HashMap<String, String> data = new HashMap<>();
                data.put("android_id", curUser.getiD());
                data.put("email", curUser.getEmail());
                data.put("f_name", curUser.getfName());
                data.put("l_name", curUser.getlName());

                userRef.document(curUser.getiD()).set(data);

            }
        });

        return rootView;
    }
}
