package com.example.eventlottery;

import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QR_Scan_Fragment {
    private CaptureManager captureManager;
    private DecoratedBarcodeView scannerView;

    //TODO: Setting up BarcodeView (DecoratedBarcodeView) to have helpers similar to ScanOptions and ScanContract (??)
    //TODO: Request Camera permission
    //TODO: Make sure only one Camera instance is active at a time
    //TODO: Handle scan results

    // Look into what CaptureActivity does
    // Learn what happens in https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/TabbedScanning.java

    // Possible Resources:
    //  https://stackoverflow.com/questions/43937292/android-zxing-embedded-barcodeview-not-resuming
    //  https://github.com/journeyapps/zxing-android-embedded/blob/4f311133ee5d7d2389deb6ebe000791a1e083770/sample/src/main/java/example/zxing/ContinuousCaptureActivity.java
    //  https://stackoverflow.com/questions/53087045/how-to-use-zxing-with-decoratedbarcodeview-and-capturemanager-to-scan-only-qr-co
    //  https://medium.com/@peeyush.pathak18/qr-code-barcode-scanning-in-android-app-a84d290fffad
    //  https://medium.com/@mhmdawaddd/change-the-frame-position-and-size-of-the-zxing-barcode-scanning-library-for-android-95747919cfbe

    // Consider using this instead:
    // https://www.geeksforgeeks.org/how-to-read-qr-code-using-camview-library-in-android/
    // https://developers.google.com/ml-kit/migration/android

    public QR_Scan_Fragment(DecoratedBarcodeView scannerView) {
        this.scannerView = scannerView;
    }
}
