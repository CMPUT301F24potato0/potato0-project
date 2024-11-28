package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.eventlottery.Entrant.EventEntrantActivity;
import com.example.eventlottery.Entrant.ScanFragment;
import com.example.eventlottery.Models.EventModel;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Models.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setup(){
        Intents.init();
        init();
    }

    @After
    public void teardown(){
        Intents.release();
    }

    // https://stackoverflow.com/questions/52818524/delay-test-in-espresso-android-without-freezing-main-thread
    private ViewAction waitFor(long delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + delay + " milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }

    private final Waiter waiter = new Waiter(10, 1);

    private void init() {
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        activityRule.getScenario().onActivity(activity -> {
            db = activity.getDb();
            curUser = activity.getUser();
            facility = activity.getFacility();
            fragment = new ScanFragment(db, curUser); // this is used to check an invalid event
        });
    }

    @Test
    public void ScanInvalidEvent() {
        // This is basically checking if the user scans an invalid event
        // The way our code is setup is that if the user scans a valid event it automatically opens a new activity
        // This should pop a Toast saying invalid event therefore I am checking if scannerView is being displayed
        fragment.checkEvent("InvalidEvent", "userID");
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
    }
    @Test
    public void OpenGeoEvent() {
        // Event that requires geo location
        // This is a hard coded event in the firebase for testing purposes only
        // Testing if a hardcoded geo location event is being opened
        waiter.perform(withId(R.id.intentTestGeo), click());
        intended(hasComponent(EventEntrantActivity.class.getName()));
        JoinGeoEvent();
        onView(withId(R.id.event_entrant_page_unjoin_button1)).check(matches(isDisplayed()));
        UnjoinGeoEvent();
        onView(withId(R.id.event_entrant_page_join_button1)).check(matches(isDisplayed()));
        pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void OpenNoGeoEvent() {
        // Event that requires geo location
        // This is a hard coded event in the firebase for testing purposes only
        // Testing if a hardcoded no geo location event is being opened
        waiter.perform(withId(R.id.intentTestNoGeo), click());
        intended(hasComponent(EventEntrantActivity.class.getName()));
        JoinNoGeoEvent();
        onView(withId(R.id.event_entrant_page_unjoin_button1)).check(matches(isDisplayed()));
        UnjoinNoGeoEvent();
        onView(withId(R.id.event_entrant_page_join_button1)).check(matches(isDisplayed()));
        pressBack();
        intended(hasComponent(MainActivity.class.getName()));
    }

    private void JoinGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_join_button1), matches(isDisplayed()));
        onView(withId(R.id.event_entrant_page_join_button1)).perform(click());
        // Waiting for use to accept the notification permissions
        waitFor(4000);
        // checking if the geo requirement dialog fragment is being displayed
        onView(withId(R.id.geo_requirement_dialog_fragment_linear_layout)).check(matches(isDisplayed()));
        waitFor(500);
        onView(withText("Accept")).perform(click());
        // Waiting for user to accept the geo location permission
        waitFor(4000);
        // Have to click accept again
        onView(withText("Accept")).perform(click());
    }

    private void UnjoinGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_unjoin_button1), matches(isDisplayed()));
        onView(withId(R.id.event_entrant_page_unjoin_button1)).perform(click());
    }

    private void JoinNoGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_join_button1), matches(isDisplayed()));
        waiter.perform(withId(R.id.event_entrant_page_join_button1), click());
//        onView(withId(R.id.event_entrant_page_join_button1)).perform(click());
        // The un join button should appear if there is no geo location required.
//        onView(withId(R.id.event_entrant_page_unjoin_button1)).check(matches(isDisplayed()));
        waiter.check(withId(R.id.event_entrant_page_unjoin_button1), matches(isDisplayed()));
    }

    private void UnjoinNoGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_unjoin_button1), matches(isDisplayed()));
        onView(withId(R.id.event_entrant_page_unjoin_button1)).perform(click());
    }
}