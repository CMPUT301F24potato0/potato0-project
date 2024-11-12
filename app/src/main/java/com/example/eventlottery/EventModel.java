package com.example.eventlottery;

import com.google.firebase.firestore.auth.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Event Model
 */
public class EventModel implements Serializable {
    private String facilityID;
    private String eventID;
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

    /**
     * Default constructor
     */
    public EventModel() {
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
        waitingList = new ArrayList<>();
        invitedList = new ArrayList<>();
        cancelledList = new ArrayList<>();
        enrolledList = new ArrayList<>();
        chosenList = new ArrayList<>();
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
    public EventModel(String facilityID, Boolean geolocationRequired, Integer waitingListLimit,
                      Integer capacity, Date joinDeadline, String eventStrLocation,
                      String eventTitle, String eventDescription, String organizer) {
        this();
        this.facilityID = facilityID;
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
     * Constructor with additional event ID parameter
     * @param facilityID The facility ID of the event
     * @param eventID The event ID of the event
     * @param geolocationRequired Whether geolocation is required for the event
     * @param waitingListLimit The waiting list limit of the event
     * @param capacity The capacity of the event
     * @param joinDeadline The deadline for joining the event
     * @param eventStrLocation The location of the event
     * @param eventTitle The title of the event
     * @param eventDescription The description of the event
     * @param organizer The organizer of the event
     */
    public EventModel(String facilityID, String eventID, Boolean geolocationRequired,
                      Integer waitingListLimit, Integer capacity, Date joinDeadline,
                      String eventStrLocation, String eventTitle, String eventDescription,
                      String organizer) {
        this(facilityID, geolocationRequired, waitingListLimit, capacity, joinDeadline,
                eventStrLocation, eventTitle, eventDescription, organizer);
        this.eventID = eventID;
    }

    /**
     * Adds the user to the provided list if they are not already in it
     * @param user The entrant's unique user
     * @param list The list to add the user into
     * @param listStr The list name for error messages
     * @throws Exception if the list already contains the user
     */
    private void queueList(UsersList user, ArrayList<UsersList> list, String listStr) throws Exception {
        if (checkUserInList(user, list)) {
            throw new Exception("The user is already inside the event's " + listStr + "!");
        }
        list.add(user);
    }

    /**
     * Removes the user from the provided list if they are in the list
     * @param user The entrant's unique user
     * @param list The list to remove the user from
     * @param listStr The list name for error messages
     * @throws Exception if the list does not contain the user
     */
    private void unqueueList(UsersList user, ArrayList<UsersList> list, String listStr) throws Exception {
        if (!checkUserInList(user, list)) {
            throw new Exception("The user is not inside the event's " + listStr + "!");
        }
        list.removeIf(u -> u.getiD().equals(user.getiD()));
    }

    /**
     * Adds a user to the waiting list if it's not full
     * @param user The entrant's unique user
     * @throws Exception if the waiting list is full or the user is already in it
     */
    public void queueWaitingList(UsersList user) throws Exception {
        if (waitingListIsFull()) {
            throw new Exception("The waiting list for this event is full!");
        }
        queueList(user, waitingList, "waiting list");
    }

    /**
     * Removes a user from the waiting list
     * @param user The entrant's unique user
     * @throws Exception if the user is not in the waiting list
     */
    public void unqueueWaitingList(UsersList user) throws Exception {
        unqueueList(user, waitingList, "waiting list");
    }

    /**
     * Checks if the waiting list is full
     * @return True if the waiting list has reached its limit; False otherwise
     */
    public Boolean waitingListIsFull() {
        if (waitingListLimit == -1) {
            return Boolean.FALSE;
        }
        return waitingList.size() >= waitingListLimit;
    }

    // Methods for managing invited list
    public void queueInvitedList(UsersList user) throws Exception {
        queueList(user, invitedList, "invited list");
    }
    public void unqueueInvitedList(UsersList user) throws Exception {
        unqueueList(user, invitedList, "invited list");
    }

    // Methods for managing cancelled list
    public void queueCancelledList(UsersList user) throws Exception {
        queueList(user, cancelledList, "cancelled list");
    }
    public void unqueueCancelledList(UsersList user) throws Exception {
        unqueueList(user, cancelledList, "cancelled list");
    }

    // Methods for managing enrolled list
    public void queueEnrolledList(UsersList user) throws Exception {
        queueList(user, enrolledList, "enrolled list");
    }
    public void unqueueEnrolledList(UsersList user) throws Exception {
        unqueueList(user, enrolledList, "enrolled list");
    }

    // Methods for managing chosen list
    public void queueChosenList(UsersList user) throws Exception {
        queueList(user, chosenList, "chosen list");
    }
    public void unqueueChosenList(UsersList user) throws Exception {
        unqueueList(user, chosenList, "chosen list");
    }

    // Getters and setters for all attributes (with JavaDocs retained)
    public String getFacilityID() { return facilityID; }
    public void setFacilityID(String facilityID) { this.facilityID = facilityID; }
    public String getEventID() { return eventID; }
    public void setEventID(String eventID) { this.eventID = eventID; }
    public Boolean getGeolocationRequired() { return geolocationRequired; }
    public void setGeolocationRequired(Boolean geolocationRequired) { this.geolocationRequired = geolocationRequired; }
    public Integer getWaitingListLimit() { return waitingListLimit; }
    public void setWaitingListLimit(Integer waitingListLimit) { this.waitingListLimit = waitingListLimit; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Date getJoinDeadline() { return joinDeadline; }
    public void setJoinDeadline(Date joinDeadline) { this.joinDeadline = joinDeadline; }
    public String getEventStrLocation() { return eventStrLocation; }
    public void setEventStrLocation(String eventStrLocation) { this.eventStrLocation = eventStrLocation; }
    public String getEventTitle() { return eventTitle; }
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public String getHashQR() { return hashQR; }
    public void setHashQR(String hashQR) { this.hashQR = hashQR; }
    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public ArrayList<UsersList> getWaitingList() { return waitingList; }
    public void setWaitingList(ArrayList<UsersList> waitingList) { this.waitingList = waitingList; }
    public ArrayList<UsersList> getInvitedList() { return invitedList; }
    public void setInvitedList(ArrayList<UsersList> invitedList) { this.invitedList = invitedList; }
    public ArrayList<UsersList> getCancelledList() { return cancelledList; }
    public void setCancelledList(ArrayList<UsersList> cancelledList) { this.cancelledList = cancelledList; }
    public ArrayList<UsersList> getEnrolledList() { return enrolledList; }
    public void setEnrolledList(ArrayList<UsersList> enrolledList) { this.enrolledList = enrolledList; }
    public ArrayList<UsersList> getChosenList() { return chosenList; }
    public void setChosenList(ArrayList<UsersList> chosenList) { this.chosenList = chosenList; }

    /**
     * Checks if a user is in a specified list
     * @param user The user to check
     * @param list The list to check
     * @return Whether the user is in the list
     */
    public boolean checkUserInList(UsersList user, ArrayList<UsersList> list) {
        return list.stream().anyMatch(u -> u.getiD().equals(user.getiD()));
    }
}
