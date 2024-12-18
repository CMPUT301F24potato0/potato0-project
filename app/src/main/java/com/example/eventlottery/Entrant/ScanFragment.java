package com.example.eventlottery.Entrant;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class is the Scan Fragment
 * This is the "home" page for the app
 * This class inflates fragment_scan
 * The purposes of this fragment is to scan QR codes of events in the app, that send the user
 * to the event page so the usar can join/unjoin the event
 */
public class ScanFragment extends Fragment {

    private DecoratedBarcodeView barcodeView;
    View rootView;
    private String eventScanned;
    private FirebaseFirestore db;
    private UserModel curUser;

    /**
     * This is the empty constructor
     */
    public ScanFragment(){
        // require a empty public constructor
    }

    /**
     * This is the constructor that takes in the database and the current user
     * @param db The database
     * @param curUser The current user
     */
    public ScanFragment(FirebaseFirestore db, UserModel curUser) {
        this.db = db;
        this.curUser = curUser;
    }

    /**
     * This is the callback for the barcode scanner
     * This method gave the ability to the user to going back to the past activity by touching the
     * left corner of the screen and swipe right
     */
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            Log.e("QR", "Scanned " + result.getText());
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
     * This function inflates the fragment_scan layout
     * It generates the view of the scanner so the user can scan a QR code
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
        Collection<BarcodeFormat> formats = Collections.singletonList(BarcodeFormat.QR_CODE);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.setStatusText("Scanning QR Code");
        barcodeView.decodeContinuous(callback);

        Button intentTestGeo = rootView.findViewById(R.id.intentTestGeo);

        intentTestGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEvent("INTENT-TEST-GEO", curUser.getiD());
            }
        });

        Button intentTestNoGeo = rootView.findViewById(R.id.intentTestNoGeo);

        intentTestNoGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEvent("INTENT-TEST-NO-GEO", curUser.getiD());
            }
        });

        return rootView;
    }

    /**
     * This function gets the events collection and then loops through to find the event that was scanned
     * when the event is found, the program takes the user to the event page activity so the user
     * can see the event details and join/unjoin the event
     * If event is not found it creates a new toast displaying error message
     * @param hashQR   The QR code that is in a hash
     * @param userId    The userID to be able to join the event
     */
    public void checkEvent(String hashQR, String userId) { // Making it public to use it for test cases
        CollectionReference eventRef = db.collection("events");
        Task<QuerySnapshot> eventTask = eventRef.whereEqualTo("hashQR", hashQR).get();
        eventTask.addOnCompleteListener((@NonNull Task<QuerySnapshot> task)  -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (!documents.isEmpty()) {
                        DocumentSnapshot document = documents.get(0);
                        EventModel eve = document.toObject(EventModel.class);
                        Intent i = new Intent(getActivity(), EventEntrantActivity.class);
                        RemoteUserRef userList = new RemoteUserRef(userId, curUser.getfName() + " " + curUser.getlName());
                        i.putExtra("userList", userList);
                        i.putExtra("eventModel", eve);
                        startActivity(i);
                    } else {
                        Log.e("CheckEventScanFragment", "Event doesn't exist");
//                        Toast.makeText(getActivity(), "Event doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("CheckEventScanFragment", "Task Failed");
//                    Toast.makeText(getActivity(), "Task Failed", Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    /**
     * This function pauses the barcode scanner when the fragment is paused
     */
    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pauseAndWait();
    }

    /**
     * This function resumes the barcode scanner when the fragment is resumed
     */
    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    /**
     * getter to get the event
     * @return   return the event
     */
    public EventModel getEvent() {
        Task<DocumentSnapshot> t = db.collection("events")
                .document(eventScanned)
                .get();
        return t.getResult().toObject(EventModel.class);
    }
}