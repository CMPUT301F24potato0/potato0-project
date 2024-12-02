package com.example.eventlottery;

import com.example.eventlottery.Models.UserModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the user model test class
 * This class tests the user model class
 */
public class UserModelTest {
    private UserModel mockUser;
    private final String fName = "John";
    private final String lName = "Doe";
    private final String email = "john.mclean@examplepetstore.com";
    private final String phone = "1234567890";
    private final boolean isAdmin = true;
    private final String facilityID = "12345";
    private final String iD = "12345";
    private final String userPhoto = "photo.jpg";
    private final boolean isMuted = false;
    private final ArrayList<String> topics = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> notifications = new ArrayList<>();

    @Test
    public void testSetName() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setfName("Jane");
        mockUser.setlName("Doe");
        assert mockUser.getfName().equals("Jane");
        assert mockUser.getlName().equals("Doe");
    }
    @Test
    public void testSetEmail() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setEmail("william.henry.harrison@example-pet-store.com");
        assert mockUser.getEmail().equals("william.henry.harrison@example-pet-store.com");
    }
    @Test
    public void testSetPhone() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setPhone("0987654321");
        assert mockUser.getPhone().equals("0987654321");
    }
    @Test
    public void testSetAdmin() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setIsAdmin(false);
        assert !mockUser.getIsAdmin();
    }
    @Test
    public void testSetFacilityID() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setFacilityID("54321");
        assert mockUser.getFacilityID().equals("54321");
    }
    @Test
    public void testSetID() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setiD("54321");
        assert mockUser.getiD().equals("54321");
    }
    @Test
    public void testSetUserPhoto() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setUserPhoto("newphoto.jpg");
        assert mockUser.getUserPhoto().equals("newphoto.jpg");
    }
    @Test
    public void testSetMuted() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setMuted(true);
        assert mockUser.isMuted();
    }
    @Test
    public void testSetTopics() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setTopics(new ArrayList<>());
        assert mockUser.getTopics().size() == 0;
    }
    @Test
    public void testAddTopics() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.addTopics("topic1");
        mockUser.addTopics("topic2");
        assert mockUser.getTopics().contains("topic1");
        assert mockUser.getTopics().contains("topic2");
    }

    @Test
    public void testRemoveTopics() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.addTopics("topic1");
        mockUser.addTopics("topic2");
        mockUser.removeTopics("topic1");
        assert !mockUser.getTopics().contains("topic1");
        assert mockUser.getTopics().contains("topic2");
    }

    @Test
    public void testSetNotifications() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics, notifications);
        ArrayList<HashMap<String, String>> notifications = new ArrayList<>();
        notifications.add(makeNotification("title1", "body1", "eventID1", "flag1"));
        notifications.add(makeNotification("title2", "body2", "eventID2", "flag2"));
        mockUser.setNotifications(notifications);
        assertEquals(mockUser.getNotifications(), notifications);
        assertEquals(mockUser.getNotifications().size(), 2);
    }

    @Test
    public void testAddNotifications() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics, notifications);
        HashMap<String, String> notification1 = makeNotification("title1", "body1", "eventID1", "flag1");
        HashMap<String, String> notification2 = makeNotification("title2", "body2", "eventID2", "flag2");
        assertEquals(mockUser.getNotifications().size(), 0);
        mockUser.addNotifications(notification1);
        assertEquals(mockUser.getNotifications().size(), 1);
        mockUser.addNotifications(notification2);
        assertEquals(mockUser.getNotifications().size(), 2);
    }

    @Test
    public void testRemoveNotifications() {
        mockUser = new UserModel(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics, notifications);
        HashMap<String, String> notification1 = makeNotification("title1", "body1", "eventID1", "flag1");
        HashMap<String, String> notification2 = makeNotification("title2", "body2", "eventID2", "flag2");
        // testing for each notification added once and deleted once
        mockUser.addNotifications(notification1);
        mockUser.addNotifications(notification2);
        assertEquals(mockUser.getNotifications().size(), 2);
        mockUser.removeNotifications(notification1);
        assertEquals(mockUser.getNotifications().size(), 1);
        mockUser.removeNotifications(notification2);
        assertEquals(mockUser.getNotifications().size(), 0);
        // testing for one notification added twice and deleted once
        mockUser.addNotifications(notification1);
        mockUser.addNotifications(notification1);
        assertEquals(mockUser.getNotifications().size(), 2);
        mockUser.removeNotifications(notification1);
        assertEquals(mockUser.getNotifications().size(), 0);
        // testing for different notification objects but same contents
        HashMap<String, String> notification1Replica = makeNotification("title1", "body1", "eventID1", "flag1");
        mockUser.addNotifications(notification1);
        assertEquals(mockUser.getNotifications().size(), 1);
        mockUser.removeNotifications(notification1Replica);
        assertEquals(mockUser.getNotifications().size(), 0);
    }

    public HashMap<String, String> makeNotification(String title, String body, String eventID, String flag) {
        HashMap<String, String> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
        notification.put("eventID", eventID);
        notification.put("flag", flag);
        return notification;
    }
}
