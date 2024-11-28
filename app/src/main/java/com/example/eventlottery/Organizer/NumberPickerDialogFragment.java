package com.example.eventlottery.Organizer;

import android.app.AlertDialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.eventlottery.R;

import java.util.ArrayList;

/**
 * A number picker popup dialog fragment.
 * Please use NumberPickerDialogFragment(Integer minNum, Integer maxNum) instead of the empty constructor.
 */
public class NumberPickerDialogFragment extends DialogFragment {

    private Integer minNum;
    private Integer maxNum;
    private Integer interval;

    /**
     * Required, empty constructor for the dialog fragment
     */
    public NumberPickerDialogFragment() {
        super();
    }

    /**
     * Main constructor for the dialog fragment
     * @param minNum The minimum selectable number
     * @param maxNum The maximum selectable number
     */
    public NumberPickerDialogFragment(Integer minNum, Integer maxNum) {
        super();
        this.minNum = minNum;
        this.maxNum = maxNum;
    }

    /**
     * The interface for retrieving the number selected in the dialog
     */
    // The interface implementation is adapted from CMPUT 301 Lab 3
    public interface NumberPickerDialogFragmentListener {
        void numberPickerResult(Integer numberPicked);
    }

    private NumberPickerDialogFragmentListener listener;

    /**
     * An override of the onAttach function, which ensures that the interface must be implemented to use the dialog fragment
     * @param context The context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NumberPickerDialogFragmentListener) {
            listener = (NumberPickerDialogFragmentListener) context;
        }
        else {
            throw new RuntimeException(context + " must implement NumberPickerDialogFragmentListener");
        }
    }

    /**
     * An override of the onStart function which replaces the background color of the dialog fragment
     */
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }


    /**
     * The onCreateDialog which creates the dialog fragment and defines its functionalities
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     * @return The created Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_number_picker_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        TextView title = rootView.findViewById(R.id.number_picker_title);
        NumberPicker numberPicker = rootView.findViewById(R.id.number_picker);
        Button selectButton = rootView.findViewById(R.id.select_button);

        title.setText("Number of entrants to sample");

        // Implementation of number picker adapted from https://developer.android.com/reference/android/widget/NumberPicker
        numberPicker.setValue(minNum);
        numberPicker.setMinValue(minNum);
        numberPicker.setMaxValue(maxNum);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.numberPickerResult(numberPicker.getValue());
                dismiss();
            }
        });

        builder.setView(rootView);
        return builder.create();
    }
}