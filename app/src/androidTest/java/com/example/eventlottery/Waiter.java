package com.example.eventlottery;


import static androidx.test.espresso.Espresso.onView;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

/**
 * This is the waiter class
 * This class waits for an element to become visible or for a set amount of time.
*/
public class Waiter {
    private int interval; // in milliseconds
    private int timeout;  // in milliseconds

    /**
     * Constructor for waiter
     * @param freq      Polling frequency in hertz.
     * @param timeout   Timeout in seconds
     */
    public Waiter(int freq, int timeout) {
        this.interval = 1000/freq;      // convert to waiting period in milliseconds
        this.timeout = timeout * 1000;  // convert seconds to milliseconds
    }

    /**
     * Getter for interval
     * @return interval in milliseconds
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Setter for interval
     * @param interval in milliseconds
     */
    public void setInterval(int interval) {
        this.interval = interval * 1000;
    }

    /**
     * Setter for frequency
     * @param freq frequency
     */
    public void setFrequency(int freq) {
        this.interval = 1000/freq;
    }

    /**
     * Getter for timeout
     * @return timeout in milliseconds
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Setter for timeout
     * @param timeout timeout in milliseconds
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static void wait_ms(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is the ViewInteractionCallback interface
     * This interface is used to call a method on a ViewInteraction object
     */
    public interface ViewInteractionCallback {
        ViewInteraction action(ViewInteraction vi);
    }

    /**
     * This is the waitWithCallback method
     * This method is waits for the view to match and the performs the action
     * @param matcher the view to match
     * @param action the action to perform
     * @return the view interaction
     */
    public ViewInteraction waitWithCallback(Matcher<View> matcher, ViewInteractionCallback action) {
        try {
            int time = 0;
            while (true) {
                Thread.sleep(this.interval);
                time += this.interval;
                try {
                    return action.action(onView(matcher));
                } catch (NoMatchingViewException | PerformException e) {
                    if (time > this.timeout) {
                        throw e;
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is the perform method
     * This method performs the action on the view using the waitWithCallBack method
     * @param matcher the view to match
     * @param action the action to perform
     * @return the view interaction
     */
    public ViewInteraction perform(Matcher<View> matcher, ViewAction action) {
        return waitWithCallback(matcher, (ViewInteraction interaction) -> {
            return interaction.perform(action);
        });
    }
    /**
     * This is the check method
     * This method checks if the view is matching the ViewAssertion using the waitWithCallBack method
     * @param matcher the view to match
     * @param assertion the assertion to perform
     * @return the view interaction
     */
    public ViewInteraction check(Matcher<View> matcher, ViewAssertion assertion) {
        return waitWithCallback(matcher, (ViewInteraction interaction) -> {
            return interaction.check(assertion);
        });
    }
}