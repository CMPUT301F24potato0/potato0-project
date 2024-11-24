package com.example.eventlottery.Models;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is the RemoteUserRef
 */
public class RemoteUserRef implements Serializable {
    private String iD;
    private String name;
    private Double latitude;
    private Double longitude;

    /**
     * This is the empty constructor
     */
    public RemoteUserRef() {
        iD = null;
        name = null;
        latitude = null;
        longitude = null;
    }

    /**
     * Get name
     * @return The name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * This is the constructor for RemoteUserRef
     * @param name The name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is the constructor for RemoteUserRef
     * @param iD The ID of the user
     * @param name The name of the user
     */
    public RemoteUserRef(String iD, String name) {
        this.iD = iD;
        this.name = name;
        this.latitude = null;
        this.longitude = null;
    }

    /**
     * Returns the ID of the user
     * @return The ID of the user
     */
    public String getiD() {
        return iD;
    }

    /**
     * This is the constructor for RemoteUserRef
     * @param iD The ID of the user
     */
    public void setiD(String iD) {
        this.iD = iD;
    }

    /**
     * Returns the latitude
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Returns the longitude
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Function for differentiating between RemoteUserRef objects based on the unique iD
     * @param o The RemoteUserRef object to compare to
     * @return True if the objects are the same; False otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteUserRef that = (RemoteUserRef) o;
        return Objects.equals(iD, that.iD);
    }

    /**
     * A function that returns the hash code of the RemoteUserRef object
     * @return The hash code of the RemoteUserRef object
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(iD);
    }
}
