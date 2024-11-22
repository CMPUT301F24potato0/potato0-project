package com.example.eventlottery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.eventlottery.Models.UsersList;
import com.example.eventlottery.Organizer.LotterySystem;

import org.junit.Test;

import java.util.ArrayList;

public class LotterySystemTest {
    @Test
    public void drawSampleTest() {
        ArrayList<UsersList> userList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            userList.add(new UsersList("0", "Name"));
        }
        ArrayList<UsersList> drawnList = LotterySystem.sampleEntrants(userList, 5);
        assertEquals(drawnList.size(), 5);
    }
}
