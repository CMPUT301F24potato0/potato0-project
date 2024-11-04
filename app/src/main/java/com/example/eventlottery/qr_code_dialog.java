package com.example.eventlottery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

// QR code generation stuff
// https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
import com.google.firebase.firestore.FieldValue;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class qr_code_dialog extends DialogFragment {
    private String eventID;
    private ImageView qrCodeIV;

    public qr_code_dialog(String eventID) {
        this.eventID = eventID;
    }

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
}
