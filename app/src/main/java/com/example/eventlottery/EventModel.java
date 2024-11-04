package com.example.eventlottery;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class EventModel {
    private String facilityID;
    private String eventID;
    // each of the following list contains user ID's formatted as Strings
    private ArrayList<UsersList> waitingList;
    private ArrayList<UsersList> invitedList;
    private ArrayList<UsersList> cancelledList;
    private ArrayList<UsersList> enrolledList;
    private Boolean geolocationRequired;
    private Integer waitingListLimit;
    private Integer capacity;
    private Date joinDeadline;
    private String eventStrLocation;
    private String eventTitle;
    private String hashQR; // TODO: part 4

    // TODO: part 4 - poster image
  
    public EventModel() {
        // default values
        facilityID = "";
        eventID = "";
        geolocationRequired = Boolean.FALSE;
        waitingListLimit = -1;
        capacity = 0;
        joinDeadline = new Date();
        eventStrLocation = "";
        eventTitle = "";
        hashQR = "";
        waitingList = new ArrayList<>();
        invitedList = new ArrayList<>();
        cancelledList = new ArrayList<>();
        enrolledList = new ArrayList<>();
    }
    public EventModel(String facilityID,
                      Boolean geolocationRequired,
                      Integer capacity,
                      Date joinDeadline,
                      String eventStrLocation,
                      String eventTitle) {

        this();

        this.facilityID = facilityID;
        this.eventID = eventID;
        this.geolocationRequired = geolocationRequired;
        this.capacity = capacity;
        this.joinDeadline = joinDeadline;
        this.eventStrLocation = eventStrLocation;
        this.eventTitle = eventTitle;
        this.waitingList = new ArrayList<UsersList>();
        this.invitedList = new ArrayList<UsersList>();
        this.cancelledList = new ArrayList<UsersList>();
        this.enrolledList = new ArrayList<UsersList>();

    }

    public EventModel(String facilityID,
                      String eventID,
                      Boolean geolocationRequired,
                      Integer capacity,
                      Date joinDeadline,
                      String eventStrLocation,
                      String eventTitle) {
        this(facilityID, geolocationRequired, capacity, joinDeadline, eventStrLocation, eventTitle);
        this.eventID = eventID;
    }

    /**
     * This function adds another user to the waiting list only if the waiting list
     * has not reached its limit, and if the user has not been added yet.
     * @param userID The entrant's unique user ID
     * @throws Exception Throws an exception if the waiting list is full
     */
    public void queueWaitingList(UsersList userID) throws Exception {
        if (waitingListIsFull()) {
            throw new Exception("The waiting list for this event is full!");
        }
        if (!waitingList.contains(userID)) {
            waitingList.add(userID);
        }
    }

    /**
     * This function removes the provided user from the waiting list.
     * @param userID The entrant's unique user ID
     * @throws Exception Throws an exception if the user is not in the waiting list
     */
    public void unqueueWaitingList(UsersList userID) throws Exception {
        if (!waitingList.contains(userID)) {
            throw new Exception("User has not joined the waiting list for this event yet!");
        }
        waitingList.remove(userID);
    }

    /**
     * This function checks if the waiting list is full (if the waiting list has a limit).
     * @return True if the waiting list has reached its limit; False otherwise
     */
    public Boolean waitingListIsFull() {
        if (waitingListLimit == -1) {
            return Boolean.FALSE;
        }
        return waitingList.size() == waitingListLimit;
    }

    // TODO: this function
    public void queueInvitedList(UsersList userID) {
        return;
    }

    // TODO: this function
    public void unqueueInvitedList(UsersList userID){
        return;
    }

    // TODO: this function
    public void queueCancelledList(UsersList userID) {
        return;
    }

    // TODO: this function
    public void queueEnrolledList(UsersList userID) {
        return;
    }

    // TODO: customize to generate a unique ID to store as the event's document name in Firestore
    private String generateID() {
        Random rand = new Random();
        return Integer.toString(rand.nextInt(100000));
    }

    public String getFacilityID() {
        return facilityID;
    }

    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public Boolean getGeolocationRequired() {
        return geolocationRequired;
    }

    public void setGeolocationRequired(Boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }

    public Integer getWaitingListLimit() {
        return waitingListLimit;
    }

    public void setWaitingListLimit(Integer waitingListLimit) {
        this.waitingListLimit = waitingListLimit;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Date getJoinDeadline() {
        return joinDeadline;
    }

    public void setJoinDeadline(Date joinDeadline) {
        this.joinDeadline = joinDeadline;
    }

    public String getEventStrLocation() {
        return eventStrLocation;
    }

    public void setEventStrLocation(String eventStrLocation) {
        this.eventStrLocation = eventStrLocation;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getHashQR() {
        return hashQR;
    }

    public void setHashQR(String hashQR) {
        this.hashQR = hashQR;
    }

    public ArrayList<UsersList> getWaitingList() {
        return waitingList;
    }

    public void setWaitingList(ArrayList<UsersList> waitingList) {
        this.waitingList = waitingList;
    }

    public ArrayList<UsersList> getInvitedList() {
        return invitedList;
    }

    public void setInvitedList(ArrayList<UsersList> invitedList) {
        this.invitedList = invitedList;
    }

    public ArrayList<UsersList> getCancelledList() {
        return cancelledList;
    }

    public void setCancelledList(ArrayList<UsersList> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public ArrayList<UsersList> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(ArrayList<UsersList> enrolledList) {
        this.enrolledList = enrolledList;
    }
}
