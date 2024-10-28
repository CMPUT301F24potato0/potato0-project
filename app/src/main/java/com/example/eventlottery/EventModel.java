package com.example.eventlottery;

import java.util.ArrayList;

public class EventModel {
    private String eventID;
    // each of the following list contains user ID's formatted as Strings
    private ArrayList<String> waitingList;
    private ArrayList<String> invitedList;
    private ArrayList<String> cancelledList;
    private ArrayList<String> enrolledList;
    private Boolean geolocationRequired;
    private Integer capacity;
    private String hashQR; // TODO: part 4
    // TODO: part 4 - poster image

    public EventModel(String eventID,
                      ArrayList<String> waitingList,
                      ArrayList<String> invitedList,
                      ArrayList<String> cancelledList,
                      ArrayList<String> enrolledList,
                      Boolean geolocationRequired,
                      Integer capacity,
                      String hashQR) {
        this.eventID = eventID;
        this.waitingList = waitingList;
        this.invitedList = invitedList;
        this.cancelledList = cancelledList;
        this.enrolledList = enrolledList;
        this.geolocationRequired = geolocationRequired;
        this.capacity = capacity;
        this.hashQR = hashQR;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public ArrayList<String> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(ArrayList<String> waitingList) {
        this.waitingList = waitingList;
    }

    public ArrayList<String> getInvitedList() {
        return invitedList;
    }

    public void setInvitedList(ArrayList<String> invitedList) {
        this.invitedList = invitedList;
    }

    public ArrayList<String> getCancelledList() {
        return cancelledList;
    }

    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public ArrayList<String> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(ArrayList<String> enrolledList) {
        this.enrolledList = enrolledList;
    }

    public Boolean getGeolocationRequired() {
        return geolocationRequired;
    }

    public void setGeolocationRequired(Boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getHashQR() {
        return hashQR;
    }

    public void setHashQR(String hashQR) {
        this.hashQR = hashQR;
    }
}
