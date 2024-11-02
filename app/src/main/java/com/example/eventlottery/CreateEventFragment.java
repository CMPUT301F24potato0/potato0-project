package com.example.eventlottery;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class CreateEventFragment extends DialogFragment {

    FrameLayout frameLayout;
    Integer dialogState;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create_event, null);
        dialogState = 0;

        frameLayout = rootView.findViewById(R.id.create_event_frameLayout);
        Button nextButton = rootView.findViewById(R.id.create_event_positive_button);
        Button backButton = rootView.findViewById(R.id.create_event_negative_button);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayout.removeAllViews();
            }
        });

        return super.onCreateDialog(savedInstanceState);
    }

    // Adapted from https://stackoverflow.com/questions/25737817/using-a-single-fragment-with-multiple-layout-in-android
    private void dialogStateSwitch() throws Exception {
        LayoutInflater inflater = getLayoutInflater();
        frameLayout.;

        switch (dialogState) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                throw new Exception("Invalid value for dialogState: " + dialogState.toString());
        }

    }
}