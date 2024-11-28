package com.example.eventlottery.Models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

/**
 * Facility Model
 */
public class FacilityModel implements Serializable {
    private String name;
    private String location;
    private String phone;
    private String email;
    private Integer capacity;
    private String userID;

    /**
     * Constructor
     * @param name Facility name
     * @param location Facility location
     * @param phone Facility phone number
     * @param email Facility email
     * @param capacity Facility capacity
     * @param userID Facility user ID
     */
    public FacilityModel(String name, String location, String phone, String email, Integer capacity, String userID) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.capacity = capacity;
        this.userID = userID;
    }

    /**
     * Empty Constructor required for Firebase
     */
    public FacilityModel() {}

    /**
     * Getter for the name of the facility
     * @return the name of the facility
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the facility
     * @param name the name of the facility
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the location of the facility
     * @return the location of the facility
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the location of the facility
     * @param location the location of the facility
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for the phone number of the facility
     * @return the phone number of the facility
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter for the phone number of the facility
     * @param phone the phone number of the facility
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Getter for the email of the facility
     * @return the email of the facility
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email of the facility
     * @param email the email of the facility
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the capacity of the facility
     * @return the capacity of the facility
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Setter for the capacity of the facility
     * @param capacity the capacity of the facility
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * Getter for the user ID of the facility
     * @return the user ID of the facility
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Setter for the user ID of the facility
     * @param userID the user ID of the facility
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Deletes from firebase, also deletes all associated events
     * @param db firestore reference
     */
    public void delete(FirebaseFirestore db) {
        String selfID = getUserID();
        db.collection("facilities").document(selfID).delete();
        db.collection("events").whereEqualTo("facilityID", selfID).get().addOnCompleteListener((Task<QuerySnapshot> task) -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                db.collection("posters").document(document.getId()).delete();
                document.getReference().delete();
            }
        });
        Task<DocumentSnapshot> task = db.collection("users").document(getUserID()).get();
        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModel owner = document.toObject(UserModel.class);
                        if (owner == null) return;
                        owner.setFacilityID("");
                        db.collection("users").document(owner.getiD()).set(owner);
                    }
                }
            }
        });
    }
}
