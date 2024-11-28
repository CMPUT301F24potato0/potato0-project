package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;
import android.util.Log;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.eventlottery.Organizer.CancelledListActivity;
import com.example.eventlottery.Organizer.EnrolledListActivity;
import com.example.eventlottery.Organizer.EventOrganizerActivity;
import com.example.eventlottery.Organizer.EventWaitlistActivity;
import com.example.eventlottery.Organizer.InvitedListActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;
@LargeTest
public class EventTests {
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

    private void NavigateToFacility() {
        onView(withId(R.id.scanQR)).perform(click());
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.facility), click());
        onView(withId(R.id.facilityOrganizerHomePage)).check(matches(isDisplayed()));
    }

    private void CreatingFacility() {
        onView(withId(R.id.create_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Test Facility"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText("Test location"));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("000000000000"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("tester@example.com"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("99"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
    }

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

    private final Waiter waiter = new Waiter(10, 1);

    private void CreateEvent(
            String eventTitle,
            String eventLocation,
            String geoLocation,
            String eventCapacity,
            String eventDescription,
            String waitListLimit) {
        onView(withId(R.id.facility_page_add_event_button)).perform(click());
        onView(withId(R.id.add_poster)).perform(click());
        onView(withId(R.id.create_event_positive_button)).perform(click()); // This should not allow user since the title hasn't been added yet
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText(eventTitle));
        waitFor(5000); // Waiting 5 seconds for use to input the image
        onView(withId(R.id.create_event_positive_button)).perform(click());
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText(eventCapacity));
        onView(withId(R.id.create_event_positive_button)).perform(click()); // This should not allow user since location hasn't been added
        onView(withId(R.id.create_event_edittext_event_location)).perform(replaceText(eventLocation));
        onView(withId(R.id.create_event_edittext_event_waitlist_limit)).perform(replaceText(waitListLimit));
        if (geoLocation.equals("Yes")) {
            onView(withId(R.id.create_event_switch_geolocation_required)).perform(click());
        }
        onView(withId(R.id.create_event_positive_button)).perform(click());
        onView(withId(R.id.create_event_positive_button)).perform(click()); // This should not allow user since description hasn't been added
        waiter.perform(withId(R.id.create_event_edittext_event_description), replaceText(eventDescription));
        onView(withId(R.id.create_event_positive_button)).perform(click());
        waitFor(1000);
    }

    private void TestEvent(
            String eventTitle,
            String geoLocation,
            String eventCapacity,
            String eventDescription,
            String waitListLimit) {
        waitFor(2000);
        onView(withText(eventTitle)).perform(click());
        intended(hasComponent(EventOrganizerActivity.class.getName()));
        waiter.check(withId(R.id.event_organizer_event_title), matches(withText(eventTitle)));
        onView(withId(R.id.event_organizer_event_description)).check(matches(withText(eventDescription)));
        if (geoLocation.equals("Yes")) {
            onView(withId(R.id.event_organizer_geolocation_required)).check(matches(withText("Yes")));
        } else {
            onView(withId(R.id.event_organizer_geolocation_required)).check(matches(withText("No")));
        }
        onView(withId(R.id.event_organizer_event_capacity)).check(matches(withText(eventCapacity)));
        onView(withId(R.id.event_organizer_waiting_list_limit)).check(matches(withText(waitListLimit)));
        TestLists();
    }

    private void TestLists() {
        waiter.perform(withId(R.id.event_organizer_invited_button), click());
        intended(hasComponent(InvitedListActivity.class.getName()));
        pressBack();
        intended(hasComponent(EventOrganizerActivity.class.getName()));

        waiter.perform(withId(R.id.event_organizer_cancelled_button), click());
        intended(hasComponent(CancelledListActivity.class.getName()));
        pressBack();
        intended(hasComponent(EventOrganizerActivity.class.getName()));

        waiter.perform(withId(R.id.event_organizer_waitlist_button), click());
        intended(hasComponent(EventWaitlistActivity.class.getName()));
        pressBack();
        intended(hasComponent(EventOrganizerActivity.class.getName()));

        waiter.perform(withId(R.id.event_organizer_enrolled_button), click());
        intended(hasComponent(EnrolledListActivity.class.getName()));
        pressBack();
        intended(hasComponent(EventOrganizerActivity.class.getName()));
        pressBack();
        intended(hasComponent(EventOrganizerActivity.class.getName()));
    }

    @Test
    public void CreatingEventsTest() {
        NavigateToFacility();
        try {
            CreatingFacility();
        } catch (Exception e) {
            Log.d("FacilityTests", "Facility already exist");
        }
        String eventTitle = "Test Event";
        String eventLocation = "Test location";
        String geoLocation = "Yes";
        String eventCapacity = "55";
        String eventDescription = "Test description";
        String waitListLimit = "66";
        Intents.init();
        CreateEvent(eventTitle, eventLocation, geoLocation, eventCapacity, eventDescription, waitListLimit);
        String eventTitle2 = UUID.randomUUID().toString();
        String geoLocation2 = "No";
        String eventCapacity2 = "77";
        String eventDescription2 = UUID.randomUUID().toString();
        String waitListLimit2 = "88";
        CreateEvent(eventTitle2, eventLocation, geoLocation2, eventCapacity2, eventDescription2, waitListLimit2);
        TestEvent(eventTitle2, geoLocation2, eventCapacity2, eventDescription2, waitListLimit2);
        Intents.release();
    }
}
