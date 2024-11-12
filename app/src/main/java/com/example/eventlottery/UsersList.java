package com.example.eventlottery;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

/**
 * This class is the UsersList
 */
public class UsersList implements Serializable {
    private String iD;
    private String name;
    private GeoPoint geoPoint;

    /**
     * This is the empty constructor
     */
    public UsersList() {
        iD = null;
        name = null;
        geoPoint = null;
    }

    /**
     * Get name
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * This is the constructor for UsersList
     * @param name The name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is the constructor for UsersList
     * @param iD The ID of the user
     * @param name The name of the user
     */
    public UsersList(String iD, String name) {
        this.iD = iD;
        this.name = name;
        this.geoPoint = null;
    }

    /**
     * Returns the ID of the user
     * @return The ID of the user
     */
    public String getiD() {
        return iD;
    }

    /**
     * This is the constructor for UsersList
     * @param iD The ID of the user
     */
    public void setiD(String iD) {
        this.iD = iD;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
