package com.example.eventlottery;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.eventlottery.Models.RemoteUserRef;
import com.example.eventlottery.Organizer.LotterySystem;

import org.junit.Test;

import java.util.ArrayList;

/**
 * This is the lottery system test class
 * This class tests the lottery system class
 */
public class LotterySystemTest {
    @Test
    public void drawSampleTest() {
        ArrayList<RemoteUserRef> userList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            userList.add(new RemoteUserRef("0", "Name"));
        }
        ArrayList<RemoteUserRef> drawnList = LotterySystem.sampleEntrants(userList, 5);
        assertEquals(drawnList.size(), 5);
    }
}
