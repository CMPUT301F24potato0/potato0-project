package com.example.eventlottery;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QR_Handler {
    private QR_Scan_Fragment qrScanFragment;

    public QR_Handler(DecoratedBarcodeView scannerView) {
        qrScanFragment = new QR_Scan_Fragment(scannerView);
    }

    //TODO: Implement QR scanning functionality

    public Bitmap generate_QR_Bitmap(String content) throws WriterException {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        return barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, 400, 400);
    }

}
