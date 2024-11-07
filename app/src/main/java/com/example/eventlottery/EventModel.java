package com.example.eventlottery;

import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Event Model
 */
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

    /**
     * Default constructor
     */
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

    /**
     * Constructor with parameters
     * @param facilityID The facility ID of the event
     * @param geolocationRequired Whether geolocation is required for the event
     * @param waitingListLimit The waiting list limit of the event
     * @param capacity The capacity of the event
     * @param joinDeadline The deadline for joining the event
     * @param eventStrLocation The location of the event
     * @param eventTitle The title of the event
     * @param eventDescription The description of the event
     * @param organizer The organizer of the event
     */
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

    /**
     * Constructor with parameters
     * @param facilityID The facility ID of the event
     * @param eventID The event ID of the event. Calling the above constructor and setting event ID in this constructor
     * @param geolocationRequired Whether geolocation is required for the event
     * @param waitingListLimit The waiting list limit of the event
     * @param capacity The capacity of the event
     * @param joinDeadline The deadline for joining the event
     * @param eventStrLocation The location of the event
     * @param eventTitle The title of the event
     * @param eventDescription The description of the event
     * @param organizer The organizer of the event
     */
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

    /**
     * This function adds another user to the invited list.
     * @param user The entrant's unique user
     */
    public void queueInvitedList(UsersList user) {
        return;
    }

    // TODO: this function

    /**
     * This function removes the provided user from the invited list.
     * @param user The entrant's unique user
     */
    public void unqueueInvitedList(UsersList user){
        return;
    }

    // TODO: this function

    /**
     * This function adds another user to the cancelled list.
     * @param user The entrant's unique user
     */
    public void queueCancelledList(UsersList user) {
        return;
    }

    // TODO: this function

    /**
     * This function removes the provided user from the cancelled list.
     * @param user The entrant's unique user
     */
    public void queueEnrolledList(UsersList user) {
        return;
    }

    // TODO: this function

    /**
     * This function removes the provided user from the enrolled list.
     * @param user The entrant's unique user
     */
    public void queueChosenList(UsersList user) {
        return;
    }

    // TODO: this function
    /**
     * This function removes the provided user from the chosen list.
     * @param user The entrant's unique user
     */
    public void unqueueChosenList(UsersList user) {
        return;
    }

    /**
     * Getter for facility ID
     * @return The facility ID
     */
    public String getFacilityID() {
        return facilityID;
    }

    /**
     * Setter for facility ID
     * @param facilityID The facility ID
     */
    public void setFacilityID(String facilityID) {
        this.facilityID = facilityID;
    }

    /**
     * Getter for event ID
     * @return The event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Setter for event ID
     * @param eventID The event ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Getter for geolocation required
     * @return Whether geolocation is required for the event
     */
    public Boolean getGeolocationRequired() {
        return geolocationRequired;
    }

    /**
     * Setter for geolocation required
     * @param geolocationRequired Whether geolocation is required for the event
     */
    public void setGeolocationRequired(Boolean geolocationRequired) {
        this.geolocationRequired = geolocationRequired;
    }


    /**
     * Getter for waiting list limit
     * @return The waiting list limit
     */
    public Integer getWaitingListLimit() {
        return waitingListLimit;
    }

    /**
     * Setter for waiting list limit
     * @param waitingListLimit The waiting list limit
     */
    public void setWaitingListLimit(Integer waitingListLimit) {
        this.waitingListLimit = waitingListLimit;
    }

    /**
     * Getter for capacity
     * @return The capacity
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Setter for capacity
     * @param capacity The capacity
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * Getter for join deadline
     * @return The join deadline
     */
    public Date getJoinDeadline() {
        return joinDeadline;
    }

    /**
     * Setter for join deadline
     * @param joinDeadline The join deadline
     */
    public void setJoinDeadline(Date joinDeadline) {
        this.joinDeadline = joinDeadline;
    }

    /**
     * Getter for event location
     * @return The event location
     */
    public String getEventStrLocation() {
        return eventStrLocation;
    }

    /**
     * Setter for event location
     * @param eventStrLocation The event location
     */
    public void setEventStrLocation(String eventStrLocation) {
        this.eventStrLocation = eventStrLocation;
    }

    /**
     * Getter for event title
     * @return The event title
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Setter for event title
     * @param eventTitle The event title
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Getter for hash QR
     * @return The hash QR
     */
    public String getHashQR() {
        return hashQR;
    }

    /**
     * Setter for hash QR
     * @param hashQR The hash QR
     */
    public void setHashQR(String hashQR) {
        this.hashQR = hashQR;
    }

    /**
     * Getter for event description
     * @return The event description
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Setter for event description
     * @param eventDescription The event description
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Getter for organizer
     * @return The organizer
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * Setter for organizer
     * @param organizer The organizer
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    /**
     * Getter for waiting list
     * @return The waiting list
     */
    public ArrayList<UsersList> getWaitingList() {
        return waitingList;
    }

    /**
     * Setter for waiting list
     * @param waitingList The waiting list
     */
    public void setWaitingList(ArrayList<UsersList> waitingList) {
        this.waitingList = waitingList;
    }

    /**
     * Getter for invited list
     * @return The invited list
     */
    public ArrayList<UsersList> getInvitedList() {
        return invitedList;
    }

    /**
     * Setter for invited list
     * @param invitedList The invited list
     */
    public void setInvitedList(ArrayList<UsersList> invitedList) {
        this.invitedList = invitedList;
    }

    /**
     * Getter for cancelled list
     * @return The cancelled list
     */

    public ArrayList<UsersList> getCancelledList() {
        return cancelledList;
    }

    /**
     * Setter for cancelled list
     * @param cancelledList The cancelled list
     */
    public void setCancelledList(ArrayList<UsersList> cancelledList) {
        this.cancelledList = cancelledList;
    }

    /**
     * Getter for enrolled list
     * @return The enrolled list
     */
    public ArrayList<UsersList> getEnrolledList() {
        return enrolledList;
    }

    /**
     * Setter for enrolled list
     * @param enrolledList The enrolled list
     */
    public void setEnrolledList(ArrayList<UsersList> enrolledList) {
        this.enrolledList = enrolledList;
    }

    /**
     * Getter for chosen list
     * @return The chosen list
     */
    public ArrayList<UsersList> getChosenList() {
        return chosenList;
    }

    /**
     * Setter for chosen list
     * @param chosenList The chosen list
     */
    public void setChosenList(ArrayList<UsersList> chosenList) {
        this.chosenList = chosenList;
    }

    /**
     * Check if user is in list
     * @param user The user to check
     * @param list The list to check
     * @return Whether the user is in the list
     */
    public boolean checkUserInList(UsersList user, ArrayList<UsersList> list) {
        for (UsersList u : list) {
            if (u.getiD().equals(user.getiD())) {
                return true;
            }
        }
        return false;
    }
}
