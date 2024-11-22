package com.example.eventlottery.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.eventlottery.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminImagesFragment extends Fragment {

    private FirebaseFirestore db;
    public AdminImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.admin_image_gallery_list, container, false);

        return rootView;
    }

}
