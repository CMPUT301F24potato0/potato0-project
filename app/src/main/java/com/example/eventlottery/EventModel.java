package com.example.eventlottery;

import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class EventModel implements Serializable {
    private String facilityID;
    private String eventID;
    // each of the following list contains user ID's formatted as Strings
    private ArrayList<UsersList> waitingList;
    private ArrayList<UsersList> invitedList;
    private ArrayList<UsersList> cancelledList;
    private ArrayList<UsersList> enrolledList;
    private ArrayList<UsersList> chosenList;
    private Boolean geolocationRequired;
    private Integer waitingListLimit;
    private Integer capacity;
    private Date joinDeadline;
    private String eventStrLocation;
    private String eventTitle;
    private String eventDescription;
    private String hashQR; // TODO: part 4
    private String organizer;

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
        eventDescription = "";
        hashQR = "";
        organizer = "";
        waitingList = new ArrayList<UsersList>();
        invitedList = new ArrayList<UsersList>();
        cancelledList = new ArrayList<UsersList>();
        enrolledList = new ArrayList<UsersList>();
        chosenList = new ArrayList<UsersList>();
    }

    public EventModel(String facilityID,
                      Boolean geolocationRequired,
                      Integer waitingListLimit,
                      Integer capacity,
                      Date joinDeadline,
                      String eventStrLocation,
                      String eventTitle,
                      String eventDescription,
                      String organizer) {

        this();

        this.facilityID = facilityID;
        this.eventID = eventID;
        this.geolocationRequired = geolocationRequired;
        this.waitingListLimit = waitingListLimit;
        this.capacity = capacity;
        this.joinDeadline = joinDeadline;
        this.eventStrLocation = eventStrLocation;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.organizer = organizer;
    }

    public EventModel(String facilityID,
                      String eventID,
                      Boolean geolocationRequired,
                      Integer waitingListLimit,
                      Integer capacity,
                      Date joinDeadline,
                      String eventStrLocation,
                      String eventTitle,
                      String eventDescription,
                      String organizer) {
        this(facilityID, geolocationRequired, waitingListLimit, capacity, joinDeadline, eventStrLocation, eventTitle, eventDescription, organizer);
        this.eventID = eventID;
    }

    /**
     * This function adds another user to the waiting list only if the waiting list
     * has not reached its limit, and if the user has not been added yet.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the waiting list is full
     */
    public void queueWaitingList(UsersList user) throws Exception {
        if (waitingListIsFull()) {
            throw new Exception("The waiting list for this event is full!");
        }
        waitingList.add(user);
//        if (this.checkUserInList(userID, waitingList)) {
//            waitingList.add(userID);
//        }
    }

    /**
     * This function removes the provided user from the waiting list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is not in the waiting list
     */
    public void unqueueWaitingList(UsersList user){
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getiD().equals(user.getiD())) {
                waitingList.remove(i);
            }
        }
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
    public void queueInvitedList(UsersList user) {
        return;
    }

    // TODO: this function
    public void unqueueInvitedList(UsersList user){
        return;
    }

    // TODO: this function
    public void queueCancelledList(UsersList user) {
        return;
    }

    // TODO: this function
    public void queueEnrolledList(UsersList user) {
        return;
    }

    // TODO: this function
    public void queueChosenList(UsersList user) {
        return;
    }

    // TODO: this function
    public void unqueueChosenList(UsersList user) {
        return;
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

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
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

    public ArrayList<UsersList> getChosenList() {
        return chosenList;
    }

    public void setChosenList(ArrayList<UsersList> chosenList) {
        this.chosenList = chosenList;
    }

    public boolean checkUserInList(UsersList user, ArrayList<UsersList> list) {
        for (UsersList u : list) {
            if (u.getiD().equals(user.getiD())) {
                return true;
            }
        }
        return false;
    }
}
