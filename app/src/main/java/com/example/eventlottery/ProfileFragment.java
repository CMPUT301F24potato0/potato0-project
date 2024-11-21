package com.example.eventlottery;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.google.firebase.firestore.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the profile fragment.
 * This class is called when the user clicks the profile button on the bottom.
 */
public class ProfileFragment extends Fragment {

    private CurrentUser curUser;
    private FirebaseFirestore db;
    public CollectionReference userRef;

    private Button editUser;
    private static boolean ismuted;

    FloatingActionButton on_notifications;
    FloatingActionButton off_notifications;

    private Button temp_add_pic;
    private Button temp_load_pic;
    private ImageView profilePicture;


    /**
     * Empty Constructor
     */
    public ProfileFragment(){
        // require a empty public constructor
    }

    /**
     * This constructor is used to pass in the instance of CurrentUser
     * @param db This is the database instance
     * @param curUser This is the information about current user which is passed from MainActivity
     */
    public ProfileFragment(FirebaseFirestore db, CurrentUser curUser) {
        this.db = db;
        this.curUser = curUser;
    }

    /**
     * This method is called when sending a notification.
     * @return It returns the boolean ismuted, returns either true or false, depending whether wants to receive notifications or not.
     */
    public static boolean getIsmute(){
        return ismuted;
    }

    /**
     * This method is called when creating the fragment.
     * This method uses the CurrentUser instance and puts the information if any in the EditText in the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return returns the View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_profile, container, false);
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        EditText f_name = (EditText) rootView.findViewById(R.id.fNameEditText);
        EditText l_name = (EditText) rootView.findViewById(R.id.lNameEditText);
        EditText email = (EditText) rootView.findViewById(R.id.emailEditText);
        EditText phone = (EditText) rootView.findViewById(R.id.phoneEditText);

        f_name.setText(curUser.getfName());
        l_name.setText(curUser.getlName());
        email.setText(curUser.getEmail());
        phone.setText(curUser.getPhone());

        editUser = (Button) rootView.findViewById(R.id.saveProfileBtn);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = curUser.getiD();
                String f_nameStr = f_name.getText().toString();
                String l_nameStr = l_name.getText().toString();
                String emailStr = email.getText().toString();
                String phoneStr = phone.getText().toString();

                curUser.setfName(f_nameStr);
                curUser.setlName(l_nameStr);
                curUser.setEmail(emailStr);
                curUser.setPhone(phoneStr);

                userRef.document(id).set(curUser);
            }
        });


        // Notifications
        on_notifications = (FloatingActionButton) rootView.findViewById(R.id.on_notification);
        off_notifications = (FloatingActionButton) rootView.findViewById(R.id.off_notification);
        ismuted = curUser.isMuted();


        if(getIsmute() == true){
            on_notifications.setVisibility(View.GONE);
            off_notifications.setVisibility(View.VISIBLE);
        } else if (getIsmute() == false) {
            on_notifications.setVisibility(View.VISIBLE);
            off_notifications.setVisibility(View.GONE);
        }
        on_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ismuted = true;
                curUser.setMuted(ismuted);
                db.collection("users").document(curUser.getiD()).set(curUser);
                Toast.makeText(getActivity(),"Notifications: Off",Toast.LENGTH_SHORT).show();
                on_notifications.setVisibility(View.GONE);
                off_notifications.setVisibility(View.VISIBLE);

            }
        });

        off_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ismuted = false;

                curUser.setMuted(ismuted);
                db.collection("users").document(curUser.getiD()).set(curUser);

                Toast.makeText(getActivity(),"Notifications: On",Toast.LENGTH_SHORT).show();
                on_notifications.setVisibility(View.VISIBLE);
                off_notifications.setVisibility(View.GONE);
            }
        });
        // Profile picture
        profilePicture = rootView.findViewById(R.id.profilePicture);
        temp_add_pic = rootView.findViewById(R.id.temp_add_pic_id);
        temp_add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoose();
                Log.e("After choosing image","decoding");

            }
        });
        temp_load_pic = rootView.findViewById(R.id.temp_loade_pic_id);
        temp_load_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decode();
            }
        });


        return rootView;
    }
    public void imageChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageChooser.launch(intent);
    }
    ActivityResultLauncher<Intent> imageChooser = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if(data != null && data.getData() != null){
                        Uri uri = data.getData();
                        try{
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                            // initialize byte stream
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            byte[] bytes = new byte[0];
                            // ****************************************************************************************
                            final int targetSize = 1024 * 1024;
                            // Start with 100% quality, and reduce quality until we get under the target size
                            int quality = 100;
                            int compressedSize = 0;

                            // Compress the image and check the size after each compression
                            while (compressedSize > targetSize || quality >= 10){
                                if (compressedSize < targetSize && compressedSize > 0){
                                    // Stops when the JPEG size is just under 1 mb
                                    break;
                                }
                                stream.reset();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
                                bytes = stream.toByteArray();
                                compressedSize = bytes.length;

                                Log.e("Image compression","Compressed size: " + compressedSize + " bytes");
                                Log.e("Image quality", "Image qualit is: " + quality + "/100");
                                // Reduce the quality by 10% for the next iteration if the image is still too large
                                quality -= 10;
                            }
                            // Save to hashmap and to firebase here
//                            HashMap<String, Object> hashMap = new HashMap<String, Object>();

                            Blob blob = Blob.fromBytes(bytes);

//                            hashMap.put("Blob", blob);


                            //Works -> No
//                            db.collection("photos").document(curUser.getiD()).set(hashMap);
                            // Works?


//                            photo = new PhotosModel(hashMap);


                            db.collection("photos").document(curUser.getiD()).set(
                                    new HashMap<String, Object>(){{
                                        put("Blob",blob);
                                    }});
                            Log.e("Image uploaded","The image uploaded is " + compressedSize + " bytes, and the quality is " + quality + "/100");


                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    public void decode(){
        DocumentReference docref = db.collection("photos").document(curUser.getiD());
        docref.get().addOnCompleteListener( task -> {
           if (task.isSuccessful()) {
               DocumentSnapshot document = task.getResult();
               if (document.exists()){
                   Blob blob = document.getBlob("Blob");
                   byte[] bytes = blob.toBytes();
                   Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                   profilePicture.setImageBitmap(bitmap);
               }
           }
        });
    }

}
