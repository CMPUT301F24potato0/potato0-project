package com.example.eventlottery.Entrant;


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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.eventlottery.Admin.AdminMainActivity;
import com.example.eventlottery.Models.UserModel;
import com.example.eventlottery.R;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * This class is the profile fragment.
 * This class is called when the user clicks the profile button on the bottom.
 */
public class ProfileFragment extends Fragment {

    private UserModel curUser;
    private FirebaseFirestore db;
    public CollectionReference userRef;

    private Button editUser;
    private static boolean ismuted;

    FloatingActionButton on_notifications;
    FloatingActionButton off_notifications;

    private Button admin_view;
    private ImageButton add_pic;
    private ImageButton delete_pic;
    private ImageView profilePicture;
    private TextView profile_letter;
    private Boolean fileTooLarge = false;

    /**
     * Empty Constructor
     */
    public ProfileFragment(){
        // require a empty public constructor
    }

    /**
     * This constructor is used to pass in the instance of UserModel
     * @param db This is the database instance
     * @param curUser This is the information about current user which is passed from MainActivity
     */
    public ProfileFragment(FirebaseFirestore db, UserModel curUser) {
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
     * This method uses the UserModel instance and puts the information if any in the EditText in the fragment.
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
                Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_SHORT).show();
                Task<QuerySnapshot> renameOrganizerInEvent = db.collection("events").whereEqualTo("facilityID", curUser.getFacilityID()).get();

                renameOrganizerInEvent.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                document.getReference().update("organizer", curUser.getfName() + " " + curUser.getlName());
                            }
                        }
                    }
                });

                editUser.setBackgroundColor(getResources().getColor(R.color.red1));
                editUser.setEnabled(false);
            }
        });


        // Notifications
        on_notifications = rootView.findViewById(R.id.on_notification);
        off_notifications = rootView.findViewById(R.id.off_notification);
        ismuted = curUser.isMuted();


        if(getIsmute()){
            on_notifications.setVisibility(View.GONE);
            off_notifications.setVisibility(View.VISIBLE);
        } else if (!getIsmute()) {
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
        add_pic = rootView.findViewById(R.id.add_picture);
        delete_pic = rootView.findViewById(R.id.delete_picture);
        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoose();
                Log.e("After choosing image","decoding");

            }
        });
        delete_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_pic.setVisibility(View.VISIBLE);
                delete_pic.setVisibility(View.GONE);
                db.collection("photos").document(curUser.getiD()).delete();

                String initial = "";
                if (!curUser.getfName().isEmpty()){
                    initial = String.valueOf(curUser.getfName().charAt(0)).toUpperCase();
                }
                else if(!curUser.getlName().isEmpty()){
                    initial = String.valueOf(curUser.getlName().charAt(0)).toUpperCase();
                }
                String finalInitial = initial;

                if(!finalInitial.isEmpty()){
                    Log.e("First or Last name","First or last name inital: "+finalInitial);
                    autoGeneratedProfilePicture(finalInitial);
                } else{
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
                            if (task.getResult().exists()) {
                                // document exists
                                Log.e("Document", "exists");
                                Log.e("String", task.getResult().getString("Initial"));
                                // CHECK
                                // **************************************************************************************************************
                                if (Objects.equals(task.getResult().getString("Initial"), "")) {
                                    // if user has an uploaded image
                                    decode();
                                    add_pic.setVisibility(View.GONE);
                                    delete_pic.setVisibility(View.VISIBLE);
                                }
                            }// user has no uploaded pictures
                            else {
                                add_pic.setVisibility(View.VISIBLE);
                                delete_pic.setVisibility(View.GONE);
                                if(finalInitial.isEmpty()) {
                                    // no first or last name
                                    Log.e("Default", "Loading Default Picture");
                                    default_picture();
                                } else {
                                    // generating default picture
                                    autoGeneratedProfilePicture(finalInitial);
                                }
                            }
                        }
                    });
                }
            }
        });

        admin_view = rootView.findViewById(R.id.admin_button);
        if (curUser.isAdmin()) {
            admin_view.setVisibility(View.VISIBLE);
        }
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
                                if (compressedSize > 5000000){ // set size limit to 5mb for a picture
                                    Log.e("fileSize","fileTooLarge");
                                    Log.e("fileSize",""+compressedSize);
                                    Toast.makeText(getContext(), "File too large", Toast.LENGTH_LONG).show();
                                    fileTooLarge = true;
                                    break;
                                }

                                Log.e("Image compression","Compressed size: " + compressedSize + " bytes");
                                Log.e("Image quality", "Image qualit is: " + quality + "/100");
                                // Reduce the quality by 10% for the next iteration if the image is still too large
                                quality -= 10;
                                fileTooLarge = false;
                            }


                            if(fileTooLarge){
                                Log.e("fileSize","The File is too large");
                                add_pic.setVisibility(View.VISIBLE);
                                delete_pic.setVisibility(View.GONE);
                            } else{
                                Log.e("Image uploaded","The image uploaded is " + compressedSize + " bytes, and the quality is " + quality + "/100");
                                Log.e("After choosing image","decoding");
                                Blob blob = Blob.fromBytes(bytes);

                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("Blob",blob);
                                hashMap.put("personal",true);
                                hashMap.put("Initial","");


                                db.collection("photos").document(curUser.getiD()).set(hashMap);
                                decode();
                                add_pic.setVisibility(View.GONE);
                                delete_pic.setVisibility(View.VISIBLE);
                            }


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
                   // Testing
                   Boolean personal = document.getBoolean("personal");
                   Log.e("personal",""+personal);
               }
           }
        });
    }
    public void default_picture(){
        if(getContext() != null){
            Log.e("Context","Not null");
            profilePicture.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.defaultprofilepicture));
        }
//        profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.defaultprofilepicture));
    }
    public void autoGeneratedProfilePicture(String finalInitial){
        profile_letter.setVisibility(View.VISIBLE);
        profile_letter.setText(finalInitial);
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // Get the TextView's text and draw it onto the canvas
        Paint paint = profile_letter.getPaint();
        if (getContext() == null){
            Log.e("Context","Context was null");
            paint.setColor(Color.BLACK);
        } else{
            Log.e("Context","Context is not null");
            paint.setColor(ContextCompat.getColor(requireContext(), R.color.black));
        }


        // Citation: https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
        paint.setTextAlign(Paint.Align.CENTER);
        int x_pos = (canvas.getWidth() / 2);
        int y_pos = (int) (((float) canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        // Add any colours for the profile background
        int[] colors = {
                R.color.gold,
        };
        int randColor = colors[new Random().nextInt(colors.length)];
        if (getContext() == null){
            Log.e("Context","Context was null");
            canvas.drawColor(Color.BLUE);
        } else{
            Log.e("Context","Context is not null");
            canvas.drawColor(ContextCompat.getColor(requireContext(), randColor));
        }
        canvas.drawText(profile_letter.getText().toString(), x_pos, y_pos, paint);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        profilePicture.setImageBitmap(bitmap);

//        byte[] bytes = stream.toByteArray();
        profile_letter.setVisibility(View.GONE);

//        Blob blob = Blob.fromBytes(bytes);
//
//        HashMap<String, Object> hashMap = new HashMap<String, Object>();
//        hashMap.put("Blob",blob);
//        hashMap.put("personal",false);
//        hashMap.put("Initial",finalInitial);
//
//        db.collection("photos").document(curUser.getiD()).set(hashMap);
//        decode();
    }

    public UserModel getCurUser() {
        return curUser;
    }
}
