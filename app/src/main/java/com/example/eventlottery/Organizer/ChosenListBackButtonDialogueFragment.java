package com.example.eventlottery.Organizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.eventlottery.R;


public class ChosenListBackButtonDialogueFragment extends DialogFragment {

    private ChosenListActivity chosenListActivity;

    /**
     * Required empty constructor for the dialogue
     */
    public ChosenListBackButtonDialogueFragment() {
        super();
    }

    /**
     * Main constructor for the dialogue
     * @param chosenListActivity A reference to the chosenListActivity
     */
    public ChosenListBackButtonDialogueFragment(ChosenListActivity chosenListActivity) {
        this.chosenListActivity = chosenListActivity;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chosen_list_back_button_dialogue, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Button positive_button = rootView.findViewById(R.id.chosenListBackDialog_positive_button);
        Button negative_button = rootView.findViewById(R.id.chosenListBackDialog_negative_button);

        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Runs the default code for onBackPressed(), which closes the activity
                dismiss();
                chosenListActivity.onBackDialogPositive();
            }
        });

        negative_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simply dismiss the dialog and do not close the activity
                dismiss();
            }
        });

        builder.setView(rootView);
        return builder.create();
    }
}