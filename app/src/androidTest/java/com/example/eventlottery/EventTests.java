package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertNotEquals;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Models.UserModel;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EventTests {
    @Before
    public void setup() {
        activityRule.getScenario().onActivity(activity -> {
            facilityModel = activity.getFacility();
            curUser = activity.getUser();
        });
        CreateFacility();
    }

    @After
    public void teardown() {
        DestroyFacility();
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


    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(Manifest.permission.CAMERA);

    private final Waiter waiter = new Waiter(10, 1);
    private FacilityModel facilityModel;
    private UserModel curUser;
    private ImageView old_image; // this is used for checking edit event

    private void NavigateToFacility() {
        waiter.perform(withId(R.id.facility), click());
        onView(withId(R.id.facilityHomePage)).check(matches(isDisplayed()));
    }

    private void DestroyFacility() {
        NavigateToFacility();
        waiter.perform(withId(R.id.facility_page_edit_facility_button), click());
        waiter.perform(withId(R.id.facility_details_delete_button), click());
        waiter.check(withId(R.id.create_facility_button), matches(isDisplayed()));
    }

    private void CreateFacility() {
        try {
            DestroyFacility();
        } catch (Exception e) {
            Log.d("FacilityTests", "Facility does not exist");
        }
        onView(withId(R.id.create_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Test facility"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText("Test location"));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("000000000000"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("tester@example.com"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("99"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
    }

    @Test
    public void CreateValidEventTest() {
        waiter.perform(withId(R.id.facility_page_add_event_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText("Valid Event Test"));
        onView(withId(R.id.add_poster)).perform(click());
        waitFor(5000); // Waiting for user to enter the poster
        onView(withId(R.id.create_event_positive_button)).perform(click());
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText("42"));
        onView(withId(R.id.create_event_edittext_event_location)).perform(replaceText("Test event location"));
        onView(withId(R.id.create_event_switch_geolocation_required)).perform(click());
        onView(withId(R.id.create_event_join_deadline_button)).perform(click());
        waiter.perform(withText("OK"), click());
        onView(withId(R.id.create_event_positive_button)).perform(click());
        onView(withId(R.id.create_event_edittext_event_description)).perform(replaceText("Test event description"));
        onView(withId(R.id.create_event_positive_button)).perform(click());
    }

    @Test
    public void CreatingInvalidEvent() {
        waiter.perform(withId(R.id.facility_page_add_event_button), click());
        waiter.perform(withId(R.id.create_event_positive_button), click());// This should not allow the user to create an event without a title
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText("Invalid Event Test"));
        waiter.perform(withId(R.id.create_event_positive_button), click());// This should not allow the user to create an event without a poster
        onView(withId(R.id.add_poster)).perform(click());
        waitFor(5000);
        onView(withId(R.id.create_event_positive_button)).perform(click());
        onView(withId(R.id.create_event_edittext_event_location)).perform(replaceText("Test event location"));
        waiter.perform(withId(R.id.create_event_positive_button), click());// This should not allow the user to create an event without a capacity
        onView(withId(R.id.create_event_edittext_event_capacity)).perform(replaceText("42"));
        waiter.perform(withId(R.id.create_event_positive_button), click());// This should not allow the user to create an event
    }

    public void CreateEvent(String title) {
        CreateFacility();
        waiter.perform(withId(R.id.facility_page_add_event_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText(title));
        waiter.perform(withId(R.id.add_poster), click());
        waitFor(5000);
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_location), replaceText("Test event location"));
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText("42"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_description), replaceText("Test event description"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
    }

    @Test
    public void EditEvent() {
        CreateEvent("Test event title 00001");
        onView(withText("Test event title 00001")).perform(click());
        onView(withId(R.id.event_organizer_edit_event_button)).perform(click());
        waiter.check(withId(R.id.create_event_edittext_event_title), matches(withText("Test event title 00001")));
        onView(withId(R.id.create_event_edittext_event_title)).perform(replaceText("Test event title edited 0001"));
        onView(withId(R.id.add_poster)).perform(click());
        waitFor(5000); // waiting for user to add the image
        onView(withId(R.id.create_event_positive_button)).perform(click());
        waiter.check(withId(R.id.create_event_edittext_event_location), matches(withText("Test event location")));
        onView(withId(R.id.create_event_edittext_event_capacity)).check(matches(withText("42")));
        onView(withId(R.id.create_event_edittext_event_location)).perform(replaceText("Test event location edited"));
        onView(withId(R.id.create_event_edittext_event_capacity)).perform(replaceText("10"));
        onView(withId(R.id.create_event_positive_button)).perform(click());
        waiter.check(withId(R.id.create_event_edittext_event_description), matches(withText("Test event description")));
        onView(withId(R.id.create_event_edittext_event_description)).perform(replaceText("Test event description edited"));
        onView(withId(R.id.create_event_positive_button)).perform(click());

        waiter.check(withId(R.id.event_organizer_event_description), matches(withText("Test event title edited 0001")));
        waiter.perform(withId(R.id.event_organizer_edit_event_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_title), matches(withText("Test event title edited")));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_location), matches(withText("Test event location edited")));
        waiter.check(withId(R.id.create_event_edittext_event_capacity), matches(withText("10")));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_description), matches(withText("Test event description edited")));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        pressBack();
        DestroyFacility();
    }

    @Test
    public void EventActivityListsNavigation() {
        CreateEvent("Navigation test event");
        waiter.perform(withText("Navigation test event"), click());

        waiter.perform(withId(R.id.event_organizer_invited_button), click());
        waiter.check(withId(R.id.invited_list), matches(isDisplayed()));
        pressBack();

        waiter.perform(withId(R.id.event_organizer_cancelled_button), click());
        waiter.check(withId(R.id.cancelled_list), matches(isDisplayed()));
        pressBack();

        waiter.perform(withId(R.id.event_organizer_waitlist_button), click());
        waiter.check(withId(R.id.waitList_listview), matches(isDisplayed()));
        pressBack();

        waiter.perform(withId(R.id.event_organizer_enrolled_button), click());
        waiter.check(withId(R.id.enroll_list), matches(isDisplayed()));
        pressBack();

        waiter.perform(withId(R.id.event_organizer_edit_event_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText("Navigation test event DONE"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_positive_button), click());
        pressBack();
        DestroyFacility();
    }
}
