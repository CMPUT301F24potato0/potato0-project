package com.example.eventlottery;

import org.junit.Test;

import java.util.ArrayList;

public class CurrentUserTest {
    private CurrentUser mockUser;
    private String fName = "John";
    private String lName = "Doe";
    private String email = "john.mclean@examplepetstore.com";
    private String phone = "1234567890";
    private boolean isAdmin = true;
    private String facilityID = "12345";
    private String iD = "12345";
    private String userPhoto = "photo.jpg";
    private boolean isMuted = false;
    private ArrayList<String> topics = new ArrayList<>();

    @Test
    public void testSetName() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setfName("Jane");
        mockUser.setlName("Doe");
        assert mockUser.getfName().equals("Jane");
        assert mockUser.getlName().equals("Doe");
    }
    @Test
    public void testSetEmail() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setEmail("william.henry.harrison@example-pet-store.com");
        assert mockUser.getEmail().equals("william.henry.harrison@example-pet-store.com");
    }
    @Test
    public void testSetPhone() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setPhone("0987654321");
        assert mockUser.getPhone().equals("0987654321");
    }
    @Test
    public void testSetAdmin() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setIsAdmin(false);
        assert !mockUser.getIsAdmin();
    }
    @Test
    public void testSetFacilityID() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setFacilityID("54321");
        assert mockUser.getFacilityID().equals("54321");
    }
    @Test
    public void testSetID() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setiD("54321");
        assert mockUser.getiD().equals("54321");
    }
    @Test
    public void testSetUserPhoto() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setUserPhoto("newphoto.jpg");
        assert mockUser.getUserPhoto().equals("newphoto.jpg");
    }
    @Test
    public void testSetMuted() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setMuted(true);
        assert mockUser.isMuted();
    }
    @Test
    public void testSetTopics() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.setTopics(new ArrayList<>());
        assert mockUser.getTopics().size() == 0;
    }
    @Test
    public void testAddTopics() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.addTopics("topic1");
        mockUser.addTopics("topic2");
        assert mockUser.getTopics().contains("topic1");
        assert mockUser.getTopics().contains("topic2");
    }

    @Test
    public void testRemoveTopics() {
        mockUser = new CurrentUser(fName, lName, email, phone, isAdmin, facilityID, iD, isMuted, topics);
        mockUser.addTopics("topic1");
        mockUser.addTopics("topic2");
        mockUser.removeTopics("topic1");
        assert !mockUser.getTopics().contains("topic1");
        assert mockUser.getTopics().contains("topic2");
    }
}
