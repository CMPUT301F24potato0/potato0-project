package com.example.eventlottery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eventlottery.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.os.FileUtils;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseFirestore db;
    private CollectionReference userRef;
    private CollectionReference photosRef;
    protected String androidID;


    private ArrayList usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        androidID = getAndroidID();

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        photosRef = db.collection("photos");


        // This was taken from https://firebase.google.com/docs/firestore/manage-data/add-data#data_types October 18 2024
        HashMap<String, Object> user_data = new HashMap<>();
        user_data.put("android_id", androidID);
        user_data.put("f_name", "Chirayu");
        user_data.put("l_name", "Shah");
        user_data.put("email", "cshah1@ualberta.ca");
        user_data.put("isAdmin", true);

        userRef.document("admin_1").set(user_data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "Document successfully written");
            }
        });
    }
    // The following function is from:
    private String getAndroidID(){
        // https://www.geeksforgeeks.org/how-to-fetch-device-id-in-android-programmatically/
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}