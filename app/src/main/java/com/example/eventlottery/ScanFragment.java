package com.example.eventlottery;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.metrics.Event;
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
import java.util.Objects;

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
            checkEvent(eventScanned, curUser.getiD());
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

        barcodeView = rootView.findViewById(R.id.scannerView);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.setStatusText("Scanning QR Code");
        barcodeView.decodeContinuous(callback);

        return rootView;
    }

    /**
     * This function gets the events collection and then loops through to find the event that was scanned.
     * If event is not found it creates a new toast displaying error message.
     * @param event The eventID scanned from the qr code
     */
    private void checkEvent(String event, String userId) {
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
                            // Start the new event fragment and pass in the event id
//                            requireActivity().getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.flFragment, new EventEntrantFragment(db, event))
//                                    .commit();
                            eventFound = true;

                            Intent i = new Intent(getActivity(), EventEntrantActivity.class);
                            i.putExtra("event_id", eventId);
                            i.putExtra("user_id", userId);
                            startActivity(i);
                            break;
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