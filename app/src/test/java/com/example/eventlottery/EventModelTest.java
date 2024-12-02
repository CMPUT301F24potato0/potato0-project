package com.example.eventlottery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.RemoteUserRef;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is the event model test class
 * This class tests the event model class
 */
public class EventModelTest {
    private EventModel mockEvent;
    private final String eventID = "12345";
    private final String eventTitle = "Test Event";
    private final String facilityID = "67890";
    private final String eventDescription = "This is a test event.";
    private final String eventLocation = "123 Main St";
    private final Boolean geoLocation = true;
    private final Integer waitingListLimit = 10;
    private final Integer capacity = 50;
    private final Date joinDeadline = new Date();
    private final String organizer = "John Doe";
    private final ArrayList<RemoteUserRef> waitingList = new ArrayList<>();
    private final ArrayList<RemoteUserRef> invitedList = new ArrayList<>();
    private final ArrayList<RemoteUserRef> cancelledList = new ArrayList<>();
    private final ArrayList<RemoteUserRef> enrolledList = new ArrayList<>();
    private final ArrayList<RemoteUserRef> chosenList = new ArrayList<>();

    @Test
    public void testGetEventID() {
        mockEvent = new EventModel(facilityID, "TEST", geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertEquals(mockEvent.getEventID(), "TEST");
    }
    @Test
    public void testSetEventID() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventID("54321");
        assertEquals(mockEvent.getEventID(), "54321");
    }
    @Test
    public void testGetGeoLocation() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertEquals(mockEvent.getGeolocationRequired(), geoLocation);  // geoLocation = true
        mockEvent = new EventModel(facilityID, eventID, false, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertEquals(mockEvent.getGeolocationRequired(), false);
    }
    @Test
    public void testSetGeoLocation() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setGeolocationRequired(false);
        assertEquals(mockEvent.getGeolocationRequired(), false);
    }
    @Test
    public void testGetFacilityID() {
        mockEvent = new EventModel("TEST", eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertEquals(mockEvent.getFacilityID(), "TEST");
    }
    @Test
    public void testSetFacilityID() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setFacilityID("09876");
        assertEquals(mockEvent.getFacilityID(), "09876");
    }
    @Test
    public void getEventTitle() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, "Swimming Lessons", eventDescription, organizer);
        assertEquals(mockEvent.getEventTitle(), "Swimming Lessons");
    }
    @Test
    public void setEventTitle() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventTitle("New Event Title");
        assertEquals(mockEvent.getEventTitle(), "New Event Title");
    }
    @Test
    public void getEventDescription() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, "Description", organizer);
        assertEquals(mockEvent.getEventDescription(), "Description");
    }
    @Test
    public void setEventDescription() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventDescription("New Event Description");
        assertEquals(mockEvent.getEventDescription(), "New Event Description");
    }
    @Test
    public void getEventLocation() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, "Canada", eventTitle, eventDescription, organizer);
        assertEquals(mockEvent.getEventStrLocation(), "Canada");
    }
    @Test
    public void setEventLocation() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setEventStrLocation("New Event Location");
        assertEquals(mockEvent.getEventStrLocation(), "New Event Location");
    }
    @Test
    public void getWaitingListLimit() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, 100, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertTrue(mockEvent.getWaitingListLimit().equals(100));
    }
    @Test
    public void setWaitingListLimit() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingListLimit(20);
        assert(mockEvent.getWaitingListLimit().equals(20));
    }
    @Test
    public void getCapacity() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, 1000, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertTrue(mockEvent.getCapacity().equals(1000));
    }
    @Test
    public void setCapacity() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setCapacity(100);
        assert(mockEvent.getCapacity().equals(100));
    }
    @Test
    public void getJoinDeadline() {
        Date date = new Date();
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, date, eventLocation, eventTitle, eventDescription, organizer);
        assertTrue(mockEvent.getJoinDeadline().equals(date));
    }
    @Test
    public void setJoinDeadline() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        Date newDate = new Date();
        mockEvent.setJoinDeadline(newDate);
        assert(mockEvent.getJoinDeadline().equals(newDate));
    }
    @Test
    public void getOrganizer() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, "Jane Doe");
        assertEquals(mockEvent.getOrganizer(), "Jane Doe");
    }
    @Test
    public void setOrganizer() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setOrganizer("Jane Doe");
        assertEquals(mockEvent.getOrganizer(), "Jane Doe");
    }
    @Test
    public void getAnyListOfNewEvent() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        assertEquals(mockEvent.getWaitingList().size(), 0);
        assertEquals(mockEvent.getInvitedList().size(), 0);
        assertEquals(mockEvent.getCancelledList().size(), 0);
        assertEquals(mockEvent.getEnrolledList().size(), 0);
        assertEquals(mockEvent.getChosenList().size(), 0);
        assertEquals(mockEvent.getEntrantIDs().size(), 0);
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
        RemoteUserRef mockUser1 = new RemoteUserRef("12345", "John Doe");
        RemoteUserRef mockUser2 = new RemoteUserRef("12345", "John Doe");
        checkUserInListHelper(mockEvent, mockUser1, mockUser2, mockEvent.getWaitingList());
        checkUserInListHelper(mockEvent, mockUser1, mockUser2, mockEvent.getInvitedList());
        checkUserInListHelper(mockEvent, mockUser1, mockUser2, mockEvent.getCancelledList());
        checkUserInListHelper(mockEvent, mockUser1, mockUser2, mockEvent.getEnrolledList());
        checkUserInListHelper(mockEvent, mockUser1, mockUser2, mockEvent.getChosenList());
    }
    private void checkUserInListHelper(EventModel mockEvent, RemoteUserRef mockUser1, RemoteUserRef mockUser2, ArrayList<RemoteUserRef> arrayList) {
        assertFalse(mockEvent.checkUserInList(mockUser1, arrayList));
        assertFalse(mockEvent.checkUserInList(mockUser2, arrayList));
        arrayList.add(mockUser1);
        assertTrue(mockEvent.checkUserInList(mockUser1, arrayList));
        assertTrue(mockEvent.checkUserInList(mockUser2, arrayList));  // check for different object but identical unique ID
    }
    @Test
    public void queueWaitingList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setWaitingList(waitingList);
        try {
            mockEvent.queueWaitingList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueWaitingList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueWaitingList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueWaitingList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.waitingListIsFull(), true);
    }
    @Test
    public void registerUserID() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, 1, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        RemoteUserRef mockUser1 = new RemoteUserRef("12345", "John Doe");
        RemoteUserRef mockUser2 = new RemoteUserRef("67890", "John Doe");
        RemoteUserRef mockUser3 = new RemoteUserRef("49290", "Jane Doe");
        // test for added unique IDs, even if names of entrants are the same
        mockEvent.registerUserID(mockUser1);
        mockEvent.registerUserID(mockUser2);
        mockEvent.registerUserID(mockUser3);
        assertTrue(mockEvent.getEntrantIDs().contains(mockUser1.getiD()));
        assertTrue(mockEvent.getEntrantIDs().contains(mockUser2.getiD()));
        assertTrue(mockEvent.getEntrantIDs().contains(mockUser3.getiD()));
        assertEquals(mockEvent.getEntrantIDs().size(), 3);
    }
    @Test
    public void deregisterUserID() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, 1, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        RemoteUserRef mockUser1 = new RemoteUserRef("12345", "John Doe");
        RemoteUserRef mockUser2 = new RemoteUserRef("67890", "Jane Doe");
        RemoteUserRef mockUser2Replica = new RemoteUserRef("67890", "Jane Doe");
        mockEvent.registerUserID(mockUser1);
        mockEvent.registerUserID(mockUser2);
        assertTrue(mockEvent.getEntrantIDs().contains(mockUser1.getiD()));
        assertTrue(mockEvent.getEntrantIDs().contains(mockUser2.getiD()));
        assertEquals(mockEvent.getEntrantIDs().size(), 2);
        // test for removal of id of same and different objects (same unique IDs in each)
        mockEvent.deregisterUserID(mockUser1);
        assertFalse(mockEvent.getEntrantIDs().contains(mockUser1));
        assertEquals(mockEvent.getEntrantIDs().size(), 1);
        mockEvent.deregisterUserID(mockUser2Replica);
        assertFalse(mockEvent.getEntrantIDs().contains(mockUser2));
        assertFalse(mockEvent.getEntrantIDs().contains(mockUser2Replica));
        assertEquals(mockEvent.getEntrantIDs().size(), 0);
    }
    @Test
    public void setEntrantIDs() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        ArrayList<String> entrantIDs = new ArrayList<>();
        entrantIDs.add("123");
        entrantIDs.add("456");
        mockEvent.setEntrantIDs(entrantIDs);
        assertEquals(mockEvent.getEntrantIDs().size(), 2);
        assertEquals(mockEvent.getEntrantIDs(), entrantIDs);
    }
    @Test
    public void queueInvitedList() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setInvitedList(invitedList);
        try {
            mockEvent.queueInvitedList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueInvitedList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueInvitedList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueCancelledList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueCancelledList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueCancelledList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueEnrolledList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueEnrolledList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueEnrolledList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueChosenList(new RemoteUserRef("12345", "John Doe"));
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
            mockEvent.queueChosenList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mockEvent.unqueueChosenList(new RemoteUserRef("12345", "John Doe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(mockEvent.getChosenList().size(), 0);
    }
    @Test
    public void randomizeHashQR() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        String oldHashQR = mockEvent.getHashQR();
        String newHashQR1 = mockEvent.randomizeHashQR();
        String newHashQR2 = mockEvent.getHashQR();
        assertEquals(newHashQR1, newHashQR2);
        assertNotEquals(oldHashQR, newHashQR1);
    }
    @Test
    public void getHashQR() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        String hashQR = mockEvent.randomizeHashQR();
        assertEquals(mockEvent.getHashQR(), hashQR);
    }
    @Test
    public void setHashQR() {
        mockEvent = new EventModel(facilityID, eventID, geoLocation, waitingListLimit, capacity, joinDeadline, eventLocation, eventTitle, eventDescription, organizer);
        mockEvent.setHashQR("hash");
        assertEquals(mockEvent.getHashQR(), "hash");
    }

}
