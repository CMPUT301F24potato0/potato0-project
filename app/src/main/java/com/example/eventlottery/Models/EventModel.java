package com.example.eventlottery.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;
import java.util.Random;

/**
 * Event Model
 */
public class EventModel implements Serializable {
    private String facilityID;
    private String eventID;
    // each of the following list contains user ID's formatted as Strings
    private ArrayList<RemoteUserRef> waitingList;
    private ArrayList<RemoteUserRef> invitedList;
    private ArrayList<RemoteUserRef> cancelledList;
    private ArrayList<RemoteUserRef> enrolledList;
    private ArrayList<RemoteUserRef> chosenList;
    private ArrayList<String> entrantIDs;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        waitingList = new ArrayList<RemoteUserRef>();
        invitedList = new ArrayList<RemoteUserRef>();
        cancelledList = new ArrayList<RemoteUserRef>();
        enrolledList = new ArrayList<RemoteUserRef>();
        chosenList = new ArrayList<RemoteUserRef>();
        entrantIDs = new ArrayList<String>();
        randomizeHashQR();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void randomizeHashQR() {
        byte[] rand = new byte[32];
        new Random().nextBytes(rand);
        hashQR = Base64.getEncoder().encodeToString(rand);

      

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
     * This function adds the user to the provided list if they are not already in it
     * @param user The entrant's unique user
     * @param list The list to add the user into
     * @param listStr The list to put into the exception if the user is already inside the list
     * @throws Exception Throws an exception if the list already contains the user
     */
    private void queueList(RemoteUserRef user, ArrayList<RemoteUserRef> list, String listStr) throws Exception {
        // check if user is already inside the list
        if (checkUserInList(user, list)) {
            throw new Exception("The user is already inside the event's " + listStr + "!");
        }
        // adds the user to the list
        list.add(user);
    }

    /**
     * This function removes the user from the provided list if they are in the list
     * @param user The entrant's unique user
     * @param list The list to remove the user from
     * @param listStr The list to put into the exception if the user is not inside the list
     * @throws Exception Throws an exception if the list does not contain the user
     */
    private void unqueueList(RemoteUserRef user, ArrayList<RemoteUserRef> list, String listStr) throws Exception {
        // checks if user is not inside the list
        if (!checkUserInList(user, list)) {
            throw new Exception("The user is not inside the event's " + listStr + "!");
        }
        // removes the user from the list
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getiD().equals(user.getiD())) {
                list.remove(i);
            }
        }
    }

    /**
     * This function adds another user to the waiting list only if the waiting list
     * has not reached its limit, and if the user has not been added yet.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the waiting list is full or if the user is already inside the waiting list
     */
    public void queueWaitingList(RemoteUserRef user) throws Exception {
        if (waitingListIsFull()) {
            throw new Exception("The waiting list for this event is full!");
        }
        queueList(user, waitingList, "waiting list");
    }

    /**
     * This function removes the provided user from the waiting list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is not in the waiting list
     */
    public void unqueueWaitingList(RemoteUserRef user) throws Exception {
        unqueueList(user, waitingList, "waiting list");
    }

    /**
     * This function checks if the waiting list is full (if the waiting list has a limit).
     * @return True if the waiting list has reached its limit; False otherwise
     */
    public Boolean waitingListIsFull() {
        if (waitingListLimit == -1) {
            return Boolean.FALSE;
        }
        return waitingList.size() >= waitingListLimit;
    }

    /**
     * Registers the user's ID into the event
     * @param user The RemoteUserRef representing the entrant
     */
    public void registerUserID(RemoteUserRef user) {
        entrantIDs.add(user.getiD());
    }

    /**
     * Removes the registration of the user's ID from the event
     * @param user The RemoteUserRef representing the entrant
     */
    public void deregisterUserID(RemoteUserRef user) {
        entrantIDs.remove(user.getiD());
    }

    /**
     * This function adds another user to the invited list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is already in the invited list
     */
    public void queueInvitedList(RemoteUserRef user) throws Exception {
        queueList(user, invitedList, "invited list");
    }

    /**
     * This function removes the provided user from the invited list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is not in the invited list
     */
    public void unqueueInvitedList(RemoteUserRef user) throws Exception {
        unqueueList(user, invitedList, "invited list");
    }

    /**
     * This function adds another user to the cancelled list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is already in the cancelled list
     */
    public void queueCancelledList(RemoteUserRef user) throws Exception {
        queueList(user, cancelledList, "cancelled list");
    }

    /**
     * This function removes the provided user from the cancelled list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is not in the cancelled list
     */
    public void unqueueCancelledList(RemoteUserRef user) throws Exception {
        unqueueList(user, cancelledList, "cancelled list");
    }

    /**
     * This function adds another user to the enrolled list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is already in the enrolled list
     */
    public void queueEnrolledList(RemoteUserRef user) throws Exception {
        queueList(user, enrolledList, "enrolled list");
    }

    /**
     * This function removes the provided user from the enrolled list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is not in the enrolled list
     */
    public void unqueueEnrolledList(RemoteUserRef user) throws Exception {
        unqueueList(user, enrolledList, "enrolled list");
    }

    /**
     * This function removes the provided user from the chosen list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is already in the chosen list
     */
    public void queueChosenList(RemoteUserRef user) throws Exception {
        queueList(user, chosenList, "chosen list");
    }

    /**
     * This function removes the provided user from the chosen list.
     * @param user The entrant's unique user
     * @throws Exception Throws an exception if the user is not in the chosen list
     */
    public void unqueueChosenList(RemoteUserRef user) throws Exception {
        unqueueList(user, chosenList, "chosen list");
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
    public ArrayList<RemoteUserRef> getWaitingList() {
        return waitingList;
    }

    /**
     * Setter for waiting list
     * @param waitingList The waiting list
     */
    public void setWaitingList(ArrayList<RemoteUserRef> waitingList) {
        this.waitingList = waitingList;
    }

    /**
     * Getter for invited list
     * @return The invited list
     */
    public ArrayList<RemoteUserRef> getInvitedList() {
        return invitedList;
    }

    /**
     * Setter for invited list
     * @param invitedList The invited list
     */
    public void setInvitedList(ArrayList<RemoteUserRef> invitedList) {
        this.invitedList = invitedList;
    }

    /**
     * Getter for cancelled list
     * @return The cancelled list
     */

    public ArrayList<RemoteUserRef> getCancelledList() {
        return cancelledList;
    }

    /**
     * Setter for cancelled list
     * @param cancelledList The cancelled list
     */
    public void setCancelledList(ArrayList<RemoteUserRef> cancelledList) {
        this.cancelledList = cancelledList;
    }

    /**
     * Getter for enrolled list
     * @return The enrolled list
     */
    public ArrayList<RemoteUserRef> getEnrolledList() {
        return enrolledList;
    }

    /**
     * Setter for enrolled list
     * @param enrolledList The enrolled list
     */
    public void setEnrolledList(ArrayList<RemoteUserRef> enrolledList) {
        this.enrolledList = enrolledList;
    }

    /**
     * Getter for chosen list
     * @return The chosen list
     */
    public ArrayList<RemoteUserRef> getChosenList() {
        return chosenList;
    }

    /**
     * Setter for chosen list
     * @param chosenList The chosen list
     */
    public void setChosenList(ArrayList<RemoteUserRef> chosenList) {
        this.chosenList = chosenList;
    }

    /**
     * Getter for entrant IDs
     * @return An array of Strings representing entrant IDs
     */
    public ArrayList<String> getEntrantIDs() {
        return entrantIDs;
    }

    /**
     * Setter for entrant IDs
     * @param entrantIDs An array of Strings representing entrant IDs
     */
    public void setEntrantIDs(ArrayList<String> entrantIDs) {
        this.entrantIDs = entrantIDs;
    }

    /**
     * Check if user is in list
     * @param user The user to check
     * @param list The list to check
     * @return Whether the user is in the list
     */
    public boolean checkUserInList(RemoteUserRef user, ArrayList<RemoteUserRef> list) {
        for (RemoteUserRef u : list) {
            if (u.getiD().equals(user.getiD())) {
                return true;
            }
        }
        return false;
    }
}
