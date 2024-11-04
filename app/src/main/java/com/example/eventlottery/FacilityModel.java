package com.example.eventlottery;

public class FacilityModel {
    private String name;
    private String location;
    private String phone;
    private String email;
    private Integer capacity;
    private String userID;

    public FacilityModel(String name, String location, String phone, String email, Integer capacity, String userID) {
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.email = email;
        this.capacity = capacity;
        this.userID = userID;
    }

    public FacilityModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
