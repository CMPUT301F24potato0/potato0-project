package com.example.eventlottery.ui.profilepage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventlottery.R;
import com.example.eventlottery.databinding.FragmentProfilePageBinding;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.widget.Button;

public class ProfilePage extends Fragment {

    private ProfilePageViewModel mViewModel;

    private FragmentProfilePageBinding binding;

    private FirebaseFirestore db;

    private Button saveButton;

    public static ProfilePage newInstance() {
        return new ProfilePage();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        CollectionReference userRef = db.collection("users");

        binding = FragmentProfilePageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.fNameTextView;

        mViewModel = new ViewModelProvider(this).get(ProfilePageViewModel.class);
        // TODO: Use the ViewModel

        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfilePageViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

/*
// This was taken from https://firebase.google.com/docs/firestore/manage-data/add-data#data_types October 18 2024
        HashMap<String, Object> user_data = new HashMap<>();
        user_data.put("android_id", androidID);
        user_data.put("f_name", "Chirayu");
        user_data.put("l_name", "Shah");
        user_data.put("email", "cshah1@ualberta.ca");
        user_data.put("isAdmin", true);

        userRef.document("admin_1").set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "Document successfully written");
            }
        });
 */