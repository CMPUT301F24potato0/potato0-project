package com.example.eventlottery;


import static androidx.test.espresso.Espresso.onView;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

/** @noinspection BusyWait*/ /*
    Waits for an element to become visible or for a set amount of time.
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

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval * 1000;
    }

    public void setFrequency(int freq) {
        this.interval = 1000/freq;
    }

    public int getTimeout() {
        return timeout;
    }

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

    public interface ViewInteractionCallback {
        ViewInteraction action(ViewInteraction vi);
    }

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
    public ViewInteraction perform(Matcher<View> matcher, ViewAction action) {
        return waitWithCallback(matcher, (ViewInteraction interaction) -> {
            return interaction.perform(action);
        });
    }
    public ViewInteraction check(Matcher<View> matcher, ViewAssertion assertion) {
        return waitWithCallback(matcher, (ViewInteraction interaction) -> {
            return interaction.check(assertion);
        });
    }
}