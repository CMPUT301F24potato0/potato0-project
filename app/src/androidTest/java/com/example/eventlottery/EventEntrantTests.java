package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

import android.Manifest;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.eventlottery.Entrant.EventEntrantActivity;
import com.example.eventlottery.Entrant.ScanFragment;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Models.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;

public class EventEntrantTests {

    private FirebaseFirestore db;
    private UserModel curUser;
    private FacilityModel facility;
    private ScanFragment fragment;
    private EventModel event;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(Manifest.permission.CAMERA);

    private final Waiter waiter = new Waiter(10, 1);

    private void init() {
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        activityRule.getScenario().onActivity(activity -> {
            db = activity.getDb();
            curUser = activity.getUser();
            facility = activity.getFacility();
            fragment = new ScanFragment(db, curUser);
        });
    }


    private void ScanInvalidEvent() {
        // This is basically checking if the user scans an invalid event
        // The way our code is setup is that if the user scans a valid event it automatically opens a new activity
        // This should pop a Toast saying invalid event therefore I am checking if scannerView is being displayed
        fragment.checkEvent("InvalidEvent", "userID");
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
    }

    private void ScanValidEvent() {
        // Event that requires geo location
        // This is a hard coded event in the firebase for testing purposes only
        Intents.init();
        fragment.checkEvent("ggpNuxXJVwrk1ABYXYfs", curUser.getiD());
        // Checking if new activity is displayed
        // https://stackoverflow.com/questions/25998659/espresso-how-can-i-check-if-an-activity-is-launched-after-performing-a-certain
        intended(hasComponent(EventEntrantActivity.class.getName()));
        // Waiting for the event to load
        waiter.check(withId(R.id.event_entrant_page_event_title1), matches(isDisplayed()));
        // Getting the event
        // https://stackoverflow.com/questions/34294745/calling-a-method-of-the-tested-activity-from-a-test-using-espresso-and-see-its-r
        ActivityScenarioRule<EventEntrantActivity> eventActivityRule =
                new ActivityScenarioRule<>(EventEntrantActivity.class);
        eventActivityRule.getScenario().onActivity(activity -> {
            event = activity.getEvent();
        });
        onView(withId(R.id.event_entrant_page_event_title1)).check(matches(withText(event.getEventTitle())));
        JoinGeoEvent();
        onView(withId(R.id.event_entrant_page_unjoin_button1)).check(matches(isDisplayed()));
        UnjoinGeoEvent();
        onView(withId(R.id.event_entrant_page_join_button1)).check(matches(isDisplayed()));
        pressBack();
        Intents.release();

        // Event that does not require geo location
        Intents.init();
        fragment.checkEvent("cTXJ0BLp6QHr5E29cYIC", curUser.getiD());
        intended(hasComponent(EventEntrantActivity.class.getName()));
        waiter.check(withId(R.id.event_entrant_page_event_title1), matches(isDisplayed()));
        ActivityScenarioRule<EventEntrantActivity> eventActivityRuleNew =
                new ActivityScenarioRule<>(EventEntrantActivity.class);
        eventActivityRuleNew.getScenario().onActivity(activity -> {
            event = activity.getEvent();
        });
        onView(withId(R.id.event_entrant_page_event_title1)).check(matches(withText(event.getEventTitle())));
        JoinNoGeoEvent();
        onView(withId(R.id.event_entrant_page_unjoin_button1)).check(matches(isDisplayed()));
        UnjoinNoGeoEvent();
        onView(withId(R.id.event_entrant_page_join_button1)).check(matches(isDisplayed()));
        pressBack();
        Intents.release();
    }

    private void JoinGeoEvent() {
        onView(withId(R.id.event_entrant_page_join_button1)).perform(click());
        // checking if the geo requirement dialog fragment is being displayed
        waiter.check(withId(R.id.geo_requirement_dialog_fragment), matches(isDisplayed()));
        onView(withText("Accept"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
    }

    private void JoinNoGeoEvent() {
        onView(withId(R.id.event_entrant_page_join_button1)).perform(click());
        // checking if the geo requirement dialog fragment is not being displayed
        waiter.check(withId(R.id.geo_requirement_dialog_fragment), matches(not(isDisplayed())));
    }

    private void UnjoinGeoEvent() {
        onView(withId(R.id.event_entrant_page_unjoin_button1)).perform(click());
    }

    private void UnjoinNoGeoEvent() {
        onView(withId(R.id.event_entrant_page_unjoin_button1)).perform(click());
    }

    @Test
    public void EventTest() {
        init();
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
        ScanInvalidEvent();
        ScanValidEvent();
    }
}