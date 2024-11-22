package com.example.eventlottery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.eventlottery.models.EventModel;
import com.example.eventlottery.models.UsersList;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class EventModelTest {
    private EventModel mockEvent;
    private String eventID = "12345";
    private String eventTitle = "Test Event";
    private String facilityID = "67890";
    private String eventDescription = "This is a test event.";
    private String eventLocation = "123 Main St";
    private Boolean geoLocation = true;
    private Integer waitingListLimit = 10;
    private Integer capacity = 50;
    private Date joinDeadline = new Date();
    private String organizer = "John Doe";
    private ArrayList<UsersList> waitingList = new ArrayList<>();
    private ArrayList<UsersList> invitedList = new ArrayList<>();
    private ArrayList<UsersList> cancelledList = new ArrayList<>();
    private ArrayList<UsersList> enrolledList = new ArrayList<>();
    private ArrayList<UsersList> chosenList = new ArrayList<>();

    @Test
    public void testSetID() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventID("54321");
        assertEquals(mockEvent.getEventID(), "54321");
    }
    @Test
    public void testSetGeoLocation() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setGeolocationRequired(false);
        assertEquals(mockEvent.getGeolocationRequired(), false);
    }
    @Test
    public void testSetFacilityID() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setFacilityID("09876");
        assertEquals(mockEvent.getFacilityID(), "09876");
    }
    @Test
    public void setEventTitle() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventTitle("New Event Title");
        assertEquals(mockEvent.getEventTitle(), "New Event Title");
    }
    @Test
    public void setEventDescription() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventDescription("New Event Description");
        assertEquals(mockEvent.getEventDescription(), "New Event Description");
    }
    @Test
    public void setEventLocation() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventStrLocation("New Event Location");
        assertEquals(mockEvent.getEventStrLocation(), "New Event Location");
    }
    @Test
    public void setWaitingListLimit() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingListLimit(20);
        assert(mockEvent.getWaitingListLimit().equals(20));
    }
    @Test
    public void setCapacity() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setCapacity(100);
        assert(mockEvent.getCapacity().equals(100));
    }
    @Test
    public void setJoinDeadline() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        Date newDate = new Date();
        mockEvent.setJoinDeadline(newDate);
        assert(mockEvent.getJoinDeadline().equals(newDate));
    }
    @Test
    public void setOrganizer() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setOrganizer("Jane Doe");
        assertEquals(mockEvent.getOrganizer(), "Jane Doe");
    }
    @Test
    public void setWaitingList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingList(waitingList);
        assertEquals(mockEvent.getWaitingList(), waitingList);
    }
    @Test
    public void setInvitedList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setInvitedList(invitedList);
        assertEquals(mockEvent.getInvitedList(), invitedList);
    }
    @Test
    public void setCancelledList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setCancelledList(cancelledList);
        assertEquals(mockEvent.getCancelledList(), cancelledList);
    }
    @Test
    public void setEnrolledList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEnrolledList(enrolledList);
        assertEquals(mockEvent.getEnrolledList(), enrolledList);
    }
    @Test
    public void setChosenList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setChosenList(chosenList);
        assertEquals(mockEvent.getChosenList(), chosenList);
    }
    @Test
    public void checkUserInList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingList(waitingList);
        assertFalse(mockEvent.checkUserInList(new UsersList("12345", "John Doe"), waitingList));
    }
    @Test
    public void queueWaitingList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingList(waitingList);
        try {
            mockEvent.queueWaitingList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getWaitingList().size(), 1);
    }
    @Test
    public void unqueueWaitingList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingList(waitingList);
        try {
            mockEvent.queueWaitingList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueWaitingList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getWaitingList().size(), 0);
    }
    @Test
    public void waitingListIsFull() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, 1, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingList(waitingList);
        try {
            mockEvent.queueWaitingList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.waitingListIsFull(), true);
    }
    @Test
    public void queueInvitedList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setInvitedList(invitedList);
        try {
            mockEvent.queueInvitedList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getInvitedList().size(), 1);
    }
    @Test
    public void unqueueInvitedList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setInvitedList(invitedList);
        try {
            mockEvent.queueInvitedList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueInvitedList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getInvitedList().size(), 0);
    }
    @Test
    public void queueCancelledList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setCancelledList(cancelledList);
        try {
            mockEvent.queueCancelledList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getCancelledList().size(), 1);
    }
    @Test
    public void unqueueCancelledList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setCancelledList(cancelledList);
        try {
            mockEvent.queueCancelledList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueCancelledList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getCancelledList().size(), 0);
    }
    @Test
    public void queueEnrolledList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEnrolledList(enrolledList);
        try {
            mockEvent.queueEnrolledList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getEnrolledList().size(), 1);
    }
    @Test
    public void unqueueEnrolledList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEnrolledList(enrolledList);
        try {
            mockEvent.queueEnrolledList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueEnrolledList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getEnrolledList().size(), 0);
    }
    @Test
    public void queueChosenList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setChosenList(chosenList);
        try {
            mockEvent.queueChosenList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getChosenList().size(), 1);
    }
    @Test
    public void unqueueChosenList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setChosenList(chosenList);
        try {
            mockEvent.queueChosenList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueChosenList(new UsersList("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getChosenList().size(), 0);
    }
}
