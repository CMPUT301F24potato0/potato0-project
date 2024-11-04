package com.example.eventlottery;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class EventModel {
    private String facilityID;
    private String eventID;
    // each of the following list contains user ID's formatted as Strings
    private ArrayList<String> waitingList;
    private ArrayList<String> invitedList;
    private ArrayList<String> cancelledList;
    private ArrayList<String> enrolledList;
    private Boolean geolocationRequired;
    private Integer waitingListLimit;
    private Integer capacity;
    private Date joinDeadline;
    private String eventStrLocation;
    private String eventTitle;
    private String hashQR; // TODO: part 4
    // TODO: part 4 - poster image

    public EventModel(String facilityID,
                      Boolean geolocationRequired,
                      Integer capacity,
                      Date joinDeadline,
                      String eventStrLocation,
                      String eventTitle) {
        this.facilityID = facilityID;
        this.eventID = eventID;
        this.geolocationRequired = geolocationRequired;
        this.capacity = capacity;
        this.joinDeadline = joinDeadline;
        this.eventStrLocation = eventStrLocation;
        this.eventTitle = eventTitle;

        this.waitingList = new ArrayList<String>();
        this.invitedList = new ArrayList<String>();
        this.cancelledList = new ArrayList<String>();
        this.enrolledList = new ArrayList<String>();
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
    public void queueWaitingList(String userID) throws Exception {
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
    public void unqueueWaitingList(String userID) throws Exception {
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
    public void queueInvitedList(String userID) {
        return;
    }

    // TODO: this function
    public void unqueueInvitedList(String userID){
        return;
    }

    // TODO: this function
    public void queueCancelledList(String userID) {
        return;
    }

    // TODO: this function
    public void queueEnrolledList(String userID) {
        return;
    }

    // TODO: customize to generate a unique ID to store as the event's document name in Firestore
    private String generateID() {
        Random rand = new Random();
        return Integer.toString(rand.nextInt(100000));
    }
}
