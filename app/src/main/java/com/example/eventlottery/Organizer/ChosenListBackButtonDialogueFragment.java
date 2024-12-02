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


/**
 * class that represent the button to go back to the past fragment or view
 */
public class ChosenListBackButtonDialogueFragment extends DialogFragment {

    /**
     * Main, empty constructor for the dialogue
     */
    public ChosenListBackButtonDialogueFragment() {
        super();
    }


    /**
     * onStart() override to change the background color of the dialogue popup window
     */
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }

    /**
     * The onCreateDialog() function where the dialogue is built and its logic defined
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return The dialogue
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chosen_list_back_button_dialogue, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Button positive_button = rootView.findViewById(R.id.chosenListBackDialog_close_button);

        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(rootView);
        return builder.create();
    }
}