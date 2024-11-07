package com.example.eventlottery;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class is the LotterySystem
 */
public class LotterySystem {

    /**
     * Draws a random sample from the waitlist based on the event's capacity.
     * If there are fewer Userslist objects (entrants) than sample_amount, returns the entire waitlist.
     * @param waitlist The list of users on the waitlist.
     * @param sample_amount The number of entrants to select.
     * @return A randomly selected subset of the waitlist.
     */
    public static ArrayList<UsersList> sampleEntrants(ArrayList<UsersList> waitlist, Integer sample_amount) {
        if (waitlist.size() < sample_amount) {
            return waitlist;
        }
        Collections.shuffle(waitlist, new Random()); // Shuffle for randomness
        return new ArrayList<>(waitlist.subList(0, sample_amount)); // Return subset
    }

}
