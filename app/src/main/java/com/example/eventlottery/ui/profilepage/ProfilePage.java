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

public class ProfilePage extends Fragment {

    private ProfilePageViewModel mViewModel;

    private FragmentProfilePageBinding binding;

    public static ProfilePage newInstance() {
        return new ProfilePage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentProfilePageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.fNameTextView;
        mViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
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