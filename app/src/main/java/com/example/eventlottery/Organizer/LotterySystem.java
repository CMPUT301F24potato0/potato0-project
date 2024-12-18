package com.example.eventlottery.Organizer;

import com.example.eventlottery.Models.RemoteUserRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class is the LotterySystem
 * This class represent a lottery where some people are select so the organizer can  invited them
 * to enroll in the event, the draw is random and choose and specif number of entrants, the specific
 * number is chose by the organizer
 */
public class LotterySystem {

    /**
     * Draws a random sample from the waitlist based on the event's capacity.
     * If there are fewer Userslist objects (entrants) than sample_amount, returns the entire waitlist.
     * @param waitlist The list of users on the waitlist.
     * @param sample_amount The number of entrants to select.
     * @return A randomly selected subset of the waitlist.
     */
    public static ArrayList<RemoteUserRef> sampleEntrants(ArrayList<RemoteUserRef> waitlist, Integer sample_amount) {
        if (waitlist.size() <= sample_amount) {
            return (ArrayList<RemoteUserRef>) waitlist.clone();
        }
        Collections.shuffle(waitlist, new Random()); // Shuffle for randomness
        return new ArrayList<>(waitlist.subList(0, sample_amount)); // Return subset
    }

}
