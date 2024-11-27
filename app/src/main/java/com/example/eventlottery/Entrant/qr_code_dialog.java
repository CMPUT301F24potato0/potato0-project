package com.example.eventlottery.Entrant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// QR code generation stuff
// https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
import com.example.eventlottery.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * This class is the QR code dialog.
 * Taken from https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
 */
public class qr_code_dialog extends DialogFragment {
    private final String eventID;
    private ImageView qrCodeIV;

    /**
     * This constructor is used to pass in the eventID.
     * @param eventID This is the eventID
     */
    public qr_code_dialog(String eventID) {
        this.eventID = eventID;
    }

    /**
     * onCreateDialog override
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return returns the Dialog
     */
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.qr_code_dialog, null);

        qrCodeIV = view.findViewById(R.id.idIVQrcode);

        generateQRCode(eventID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("QR code")
                .setPositiveButton("Ok", null)
                .create();
    }

    /**
     * This method is used to generate the QR code.
     * @param text The text to be encoded in the QR code.
     */
    private void generateQRCode(String text)
    {
        BarcodeEncoder barcodeEncoder
                = new BarcodeEncoder();
        try {
            // This method returns a Bitmap image of the
            // encoded text with a height and width of 400
            // pixels.
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeIV.setImageBitmap(bitmap); // Sets the Bitmap to ImageView
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.drawable.gradient_background_dialog);
    }
}
