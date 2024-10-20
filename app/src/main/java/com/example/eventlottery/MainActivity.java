package com.example.eventlottery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        // https://github.com/journeyapps/zxing-android-embedded/tree/master
//        ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
//                result -> {
//                    if (result.getContents() == null) {
//                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                    }
//                });

//        Button scan_button = findViewById(R.id.scan_button);
//        scan_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ScanOptions options = new ScanOptions();
//                options.setOrientationLocked(false);
//                options.setBeepEnabled(false);
//                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
//                barcodeLauncher.launch(options);
//            }
//        });
        new ScanContract();
        new ScanOptions();

        DecoratedBarcodeView barcodeView = findViewById(R.id.scanner);
        QR_Handler qrHandler = new QR_Handler(barcodeView);

        Button scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement this after implementing QR_Scan_Fragment and QR_Handler
                Toast.makeText(MainActivity.this, "Implement this button", Toast.LENGTH_SHORT).show();
            }
        });

        Button generate_button = findViewById(R.id.generate_button);
        generate_button.setOnClickListener(( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Bitmap bitmap = qrHandler.generate_QR_Bitmap("Hello World!");
                    ImageView qrCode = findViewById(R.id.qrCode);
                    qrCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }
}