package com.example.eventlottery;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This class is the Scan Fragment
 * This is the "home" page for the app
 * This class inflates fragment_scan
 */
public class ScanFragment extends Fragment {

    private DecoratedBarcodeView barcodeView;
    View rootView;
    private String eventScanned;
    private FirebaseFirestore db;
    private CurrentUser curUser;

    public ScanFragment(){
        // require a empty public constructor
    }


    public ScanFragment(FirebaseFirestore db, CurrentUser curUser) {
        this.db = db;
        this.curUser = curUser;
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(eventScanned)) {
                return;
            }

            eventScanned = result.getText();

            checkEvent(eventScanned);
//            Toast.makeText(getContext(), eventScanned, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        // https://stackoverflow.com/questions/38552144/how-get-permission-for-camera-in-android-specifically-marshmallow
        // https://stackoverflow.com/questions/43937292/android-zxing-embedded-barcodeview-not-resuming
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 100);
        }
        else {
            barcodeView = rootView.findViewById(R.id.scannerView);
            Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE);
            barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
            barcodeView.setStatusText("Scanning QR Code");
            barcodeView.decodeContinuous(callback);
        }
        return rootView;
    }

    private void checkEvent(String event) {
        CollectionReference eventRef = db.collection("events");
        eventRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    boolean eventFound = false;
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String eventId = doc.getId();
                        if (eventId.equals(event)) {
                            // Start the new event activity and pass in the event id

                            Toast.makeText(getContext(), "Event found: " + event, Toast.LENGTH_SHORT).show();
                            eventFound = true;
                        }
                    }
                    if (!eventFound) {
                        Toast.makeText(getContext(), "Event not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pauseAndWait();
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

}