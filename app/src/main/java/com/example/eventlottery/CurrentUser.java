package com.example.eventlottery;

/**
 * This Class stores the current user's information
 */
public class CurrentUser {
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private boolean isAdmin;
    private String facilityID;
    private String iD;
    private String userPhoto;

    /**
     * Constructor that updates the local variables
     * @param fName First Name
     * @param lName Last Name
     * @param email Email Address
     * @param isAdmin Admin Status
     * @param phone Phone number
     * @param iD Android ID
     */
    public CurrentUser(String fName, String lName, String email, String phone, boolean isAdmin, String facilityID, String iD) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.facilityID = facilityID;
        this.phone = phone;
        this.iD = iD;
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

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }
}
