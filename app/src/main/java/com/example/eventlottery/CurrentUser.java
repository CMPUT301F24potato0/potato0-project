package com.example.eventlottery;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This Class stores the current user's information
 */
public class CurrentUser implements Serializable {
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private boolean isAdmin;
    private String facilityID;
    private String iD;
    private String userPhoto;
    private boolean isMuted = false;
    private ArrayList<String> topics;

    /**
     * Constructor that updates the local variables
     * @param fName First Name
     * @param lName Last Name
     * @param email Email Address
     * @param isAdmin Admin Status
     * @param phone Phone number
     * @param iD Android ID
     */
    public CurrentUser(String fName, String lName, String email, String phone, boolean isAdmin, String facilityID, String iD, boolean isMuted, ArrayList<String> topics) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.facilityID = facilityID;
        this.phone = phone;
        this.iD = iD;
        this.isMuted = isMuted;
        this.topics = topics;
    }

    public CurrentUser() {}

    /**
     * Getting First name
     * @return First name
     */
    public String getfName(){
        return fName;
    }
    /**
     * Getting Last name
     * @return Last name
     */
    public String getlName(){
        return lName;
    }
    /**
     * Getting phone number
     * @return phone number as String
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Getting Email address
     * @return Email address
     */
    public String getEmail(){
        return email;
    }
    /**
     * Getting Admin status
     * @return Admin status
     */
    public boolean getIsAdmin(){
        return isAdmin;
    }
    /**
     * Getting Android ID
     * @return Android's ID
     */
    public String getiD() {
        return iD;
    }
    /**
     * Setting First name
     */
    public void setfName(String fName) {
        this.fName = fName;
    }
    /**
     * Setting Last name
     */
    public void setlName(String lName) {
        this.lName = lName;
    }
    /**
     * Setting email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Setting phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**
     * Setting Admin Status
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Getting Facility ID
     * @return Facility ID
     */
    public String getFacilityID() {
        return facilityID;
    }

    /**
     * Setting Facility ID
     * @param facilityID Facility ID
     */
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    /**
     * Getting admin
     * @return admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Setting admin
     * @param admin admin boolean
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    /**
     * Getting Android ID
     * @param iD Android ID
     */
    public void setiD(String iD) {
        this.iD = iD;
    }

    /**
     * Getting user photo
     * @return user photo
     */
    public String getUserPhoto() {
        return userPhoto;
    }

    /**
     * Setting user photo
     * @param userPhoto user photo
     */
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    /**
     * Getting muted
     * @return muted
     */
    public boolean isMuted() {
        return isMuted;
    }

    /**
     * Setting muted
     * @param muted muted boolean
     */
    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    /**
     * Getting topics
     * @return topics
     */
    public ArrayList<String> getTopics() {
        return topics;
    }

    /**
     * Setting topics
     * @param topics topics
     */
    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    /**
     * Adding topics
     * @param topic topic
     */
    public void addTopics(String topic) {
        for (String t : topics) {
            if (t.equals(topic)) {
                return;
            }
        }
        this.topics.add(topic);
    }

    /**
     * Removing topics
     * @param topic topic
     */
    public void removeTopics(String topic){
        for (String t : topics) {
            if (t.equals(topic)) {
                this.topics.remove(topic);
            }
        }
    }
}