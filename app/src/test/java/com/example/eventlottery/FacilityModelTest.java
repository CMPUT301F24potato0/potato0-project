package com.example.eventlottery;

import static org.junit.Assert.assertEquals;

import com.example.eventlottery.Models.FacilityModel;

import org.junit.Test;

public class FacilityModelTest {
    private FacilityModel mockFacility;
    private final String name = "Test Facility";
    private final String location = "123 Main St";
    private final String phone = "555-555-5555";
    private final String email = "john.c.breckinridge@altostrat.com";
    private final Integer capacity = 100;
    private final String userID = "12345";

    @Test
    public void setName() {
        mockFacility = new FacilityModel(name, location, phone, email, capacity, userID);
        mockFacility.setName("New Facility Name");
        assertEquals(mockFacility.getName(), "New Facility Name");
    }
    @Test
    public void setLocation() {
        mockFacility = new FacilityModel(name, location, phone, email, capacity, userID);
        mockFacility.setLocation("New Facility Location");
        assertEquals(mockFacility.getLocation(), "New Facility Location");
    }

    @Test
    public void setPhone() {
        mockFacility = new FacilityModel(name, location, phone, email, capacity, userID);
        mockFacility.setPhone("New Facility Phone");
        assertEquals(mockFacility.getPhone(), "New Facility Phone");
    }

    @Test
    public void setEmail() {
        mockFacility = new FacilityModel(name, location, phone, email, capacity, userID);
        mockFacility.setEmail("New Facility Email");
        assertEquals(mockFacility.getEmail(), "New Facility Email");
    }

    @Test
    public void setCapacity() {
        mockFacility = new FacilityModel(name, location, phone, email, capacity, userID);
        mockFacility.setCapacity(200);
        assertEquals(mockFacility.getCapacity(), (Integer) 200);
    }
    @Test
    public void setUserID() {
        mockFacility = new FacilityModel(name, location, phone, email, capacity, userID);
        mockFacility.setUserID("New Facility User ID");
        assertEquals(mockFacility.getUserID(), "New Facility User ID");
    }
}
