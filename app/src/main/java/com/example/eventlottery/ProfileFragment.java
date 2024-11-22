package com.example.eventlottery;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventlottery.Admin.AdminMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Objects;

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

    private Button admin_view;
    private Button temp_add_pic;
    private Button temp_delete_pic;
    private ImageView profilePicture;
    private TextView profile_letter;

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

        EditText f_name = rootView.findViewById(R.id.fNameEditText);
        EditText l_name = rootView.findViewById(R.id.lNameEditText);
        EditText email = rootView.findViewById(R.id.emailEditText);
        EditText phone = rootView.findViewById(R.id.phoneEditText);

        f_name.setText(curUser.getfName());
        l_name.setText(curUser.getlName());
        email.setText(curUser.getEmail());
        phone.setText(curUser.getPhone());

        editUser = rootView.findViewById(R.id.saveProfileBtn);
        editUser.setEnabled(false);
        editUser.setBackgroundColor(getResources().getColor(R.color.red1));
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editUser.setEnabled(true);
                editUser.setBackgroundColor(getResources().getColor(R.color.indigo));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        f_name.addTextChangedListener(textWatcher);
        l_name.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        phone.addTextChangedListener(textWatcher);

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
                editUser.setBackgroundColor(getResources().getColor(R.color.red1));
                editUser.setEnabled(false);
            }
        });


        // Notifications
        on_notifications = rootView.findViewById(R.id.on_notification);
        off_notifications = rootView.findViewById(R.id.off_notification);
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
        temp_delete_pic = rootView.findViewById(R.id.temp_delete_pic_id);

        temp_delete_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("photos").document(curUser.getiD()).delete();




                String initial = "";
                if (!curUser.getfName().isEmpty()){
                    initial = String.valueOf(curUser.getfName().charAt(0)).toUpperCase();
                }
                else if(!curUser.getlName().isEmpty()){
                    initial = String.valueOf(curUser.getlName().charAt(0)).toUpperCase();
                }
                DocumentReference docRef = db.collection("photos").document(curUser.getiD());
                String finalInitial = initial;

                if(!finalInitial.isEmpty()){
                    Log.e("First or Last name","First or last name inital: "+finalInitial);
                    profile_letter.setVisibility(View.VISIBLE);
                    profile_letter.setText(finalInitial);
                    Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    // Get the TextView's text and draw it onto the canvas
                    Paint paint = profile_letter.getPaint();
//                                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.gray1));
                    canvas.drawColor(Color.GRAY);
                    canvas.drawText(profile_letter.getText().toString(), 20, 80, paint);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    byte[] bytes = stream.toByteArray();
                    profile_letter.setVisibility(View.GONE);

                    Blob blob = Blob.fromBytes(bytes);

                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("Blob",blob);
                    hashMap.put("type",false);
                    hashMap.put("Initial",finalInitial);

                    db.collection("photos").document(curUser.getiD()).set(hashMap);
                    decode();
                }
                else{
                    // document doesn't exist
                    Log.e("Document","Does not exist");
                    default_picture();
                }
            }
        });



        profile_letter = rootView.findViewById(R.id.profile_letter_picture);
        DocumentReference userRef = db.collection("users").document(curUser.getiD());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Log.d("Error",""+error);
                } else {
                    Log.e("Updated","You updated your profile");
                    String initial = "";
                    if (!curUser.getfName().isEmpty()){
                        initial = String.valueOf(curUser.getfName().charAt(0)).toUpperCase();
                    }
                    else if(!curUser.getlName().isEmpty()){
                        initial = String.valueOf(curUser.getlName().charAt(0)).toUpperCase();
                    }


                    // Check if user already has a profile picture
                    DocumentReference docRef = db.collection("photos").document(curUser.getiD());
                    String finalInitial = initial;
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.e("Document","checking document existence");
                            if (task.getResult().exists()){
                                // document exists
                                Log.e("Document","exists");
                                Log.e("String",""+task.getResult().getString("Initial"));
//                                decode();
                            }
                            if(Objects.equals(task.getResult().getString("Initial"), "")){
                                decode();
                            }
                            else if(finalInitial.isEmpty()){
                                Log.e("deleting","deleting");
                                docRef.delete();
                                default_picture();
                            }
                            else if(finalInitial.equals(task.getResult().getString("Initial"))){
                                decode();
                            }
                            else if (!finalInitial.equals(task.getResult().getString("Initial")) && !finalInitial.isEmpty()){
                                Log.e("First or Last name","First or last name inital: "+finalInitial);
                                profile_letter.setVisibility(View.VISIBLE);
                                profile_letter.setText(finalInitial);
                                Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                // Get the TextView's text and draw it onto the canvas
                                Paint paint = profile_letter.getPaint();
//                                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.gray1));
                                canvas.drawColor(Color.GRAY);
                                canvas.drawText(profile_letter.getText().toString(), 20, 80, paint);

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                                byte[] bytes = stream.toByteArray();
                                profile_letter.setVisibility(View.GONE);

                                Blob blob = Blob.fromBytes(bytes);

                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("Blob",blob);
                                hashMap.put("type",false);
                                hashMap.put("Initial",finalInitial);

                                db.collection("photos").document(curUser.getiD()).set(hashMap);
                                decode();
                            }
                            else{
                                // document doesn't exist
                                Log.e("Document","Does not exist");
                                default_picture();
                            }
                        }
                    });




                }
            }
        });




        // check if document exists in firebase (if user has ever uploaded an image for his profile)
//        DocumentReference docRef = db.collection("photos").document(curUser.getiD());
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                Log.e("Document","checking document existence");
//                if (task.getResult().exists()){
//                    // document exists
//                    Log.e("Document","exists");
//                    decode();;
//                } else{
//                    // document doesn't exist
//                    Log.e("Document","Does not exist");
//                    default_picture();
//                }
//            }
//        });


        admin_view = (Button) rootView.findViewById(R.id.admin_button);
        admin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AdminMainActivity.class);
                startActivity(i);
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
                            // ****************************************************************************************

                            Blob blob = Blob.fromBytes(bytes);


//                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
//                            hashMap.put("Blob",blob);
//                            PhotosModel photosModel = new PhotosModel("personal",hashMap);


                            HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            hashMap.put("Blob",blob);
                            hashMap.put("type",true);
                            hashMap.put("Initial","");

                            // ****************************************************************************************
//                            db.collection("photos").document(curUser.getiD()).set(
//                                    new HashMap<String, Object>(){{
//                                        put("Blob",blob);
//                                        put("type",false);
//                                    }});
                            db.collection("photos").document(curUser.getiD()).set(hashMap);
                            // ****************************************************************************************

//                            db.collection("photos").document(curUser.getiD()).set(photosModel);

                            Log.e("Image uploaded","The image uploaded is " + compressedSize + " bytes, and the quality is " + quality + "/100");
                            Log.e("After choosing image","decoding");
                            decode();

                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    public void decode(){
        // ****************************************************************************************

        DocumentReference docref = db.collection("photos").document(curUser.getiD());
        docref.get().addOnCompleteListener( task -> {
           if (task.isSuccessful()) {
               DocumentSnapshot document = task.getResult();
               if (document.exists()){
                   Blob blob = document.getBlob("Blob");
                   byte[] bytes = blob.toBytes();
                   Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                   profilePicture.setImageBitmap(bitmap);
                   // Testing
                   Boolean type = document.getBoolean("type");
                   Log.e("type",""+type);
               }
           }
        });
    }
    public void default_picture(){
        DocumentReference docref = db.collection("photos").document("default");
        Log.e("here","here");
        docref.get().addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    Blob blob = document.getBlob("Blob");
                    byte[] bytes = blob.toBytes();
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    profilePicture.setImageBitmap(bitmap);
                    // Testing
                    Boolean type = document.getBoolean("type");
                    Log.e("type",""+type);
                }
            }
        });
    }

}
