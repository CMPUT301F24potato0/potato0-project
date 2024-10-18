package com.example.eventlottery;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eventlottery.databinding.ActivityMainBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FirebaseFirestore db;
    private CollectionReference userRef;
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
        try {
            androidID = createTransactionID();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        HashMap<String, String> data = new HashMap<>();
        data.put("android_id", androidID);
        userRef.document(androidID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Firestore", "Document successfully written");
            }
        });

    }
    // The following function is from:
    // https://stackoverflow.com/questions/40237169/generating-unique-id-in-android-uuid October 17 2024
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}