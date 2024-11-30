package com.example.eventlottery;

import static androidx.test.InstrumentationRegistry.getContext;
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
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
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
@LargeTest
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
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
    );

    // https://stackoverflow.com/questions/52818524/delay-test-in-espresso-android-without-freezing-main-thread

    private final Waiter waiter = new Waiter(10, 1);

    private void init() {
        waiter.perform(withId(R.id.scanQR), click());
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        activityRule.getScenario().onActivity(activity -> {
            db = activity.getDb();
            curUser = activity.getUser();
            facility = activity.getFacility();
            fragment = new ScanFragment(db, curUser); // this is used to check an invalid event
        });
    }
    private void ScanInvalidEvent() {
        init();
        // This is basically checking if the user scans an invalid event
        // The way our code is setup is that if the user scans a valid event it automatically opens a new activity
        // This should pop a Toast saying invalid event therefore I am checking if scannerView is being displayed
        fragment.checkEvent("InvalidEvent", "userID");
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
    }

    private void JoinGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_join_button1), matches(isDisplayed()));
        waiter.perform(withId(R.id.event_entrant_page_join_button1), click());
        // checking if the geo requirement dialog fragment is being displayed
        onView(withId(R.id.geo_requirement_dialog_fragment_linear_layout)).check(matches(isDisplayed()));
        waiter.perform(withText("Accept"), click());
    }

    private void UnjoinGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_unjoin_button1), matches(isDisplayed()));
        onView(withId(R.id.event_entrant_page_unjoin_button1)).perform(click());
        waiter.check(withId(R.id.event_entrant_page_join_button1), matches(isDisplayed()));
    }

    private void JoinNoGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_join_button1), matches(isDisplayed()));
        waiter.perform(withId(R.id.event_entrant_page_join_button1), click());
        // The un join button should appear if there is no geo location required.
        waiter.check(withId(R.id.event_entrant_page_unjoin_button1), matches(isDisplayed()));
    }

    private void UnjoinNoGeoEvent() {
        waiter.check(withId(R.id.event_entrant_page_unjoin_button1), matches(isDisplayed()));
        onView(withId(R.id.event_entrant_page_unjoin_button1)).perform(click());
        waiter.check(withId(R.id.event_entrant_page_join_button1), matches(isDisplayed()));
    }

    @Test
    public void EventEntrantTest() {
        init();
        Intents.init();
        ScanInvalidEvent();
        onView(withId(R.id.scannerView)).check(matches(isDisplayed()));
        Intents.release();

        // Testing joining geo event
        Intents.init();
        waiter.perform(withId(R.id.intentTestGeo), click());
        intended(hasComponent(EventEntrantActivity.class.getName()));
        JoinGeoEvent();
        UnjoinGeoEvent();
        pressBack(); // Goes back to the MainActivity
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();

        // Testing joining no geo event
        Intents.init();
        waiter.perform(withId(R.id.intentTestNoGeo), click());
        intended(hasComponent(EventEntrantActivity.class.getName()));
        JoinNoGeoEvent();
        UnjoinNoGeoEvent();
        pressBack();
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }
}