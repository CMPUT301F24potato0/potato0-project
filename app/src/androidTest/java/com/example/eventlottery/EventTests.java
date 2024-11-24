package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.any;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

public class EventTests {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(Manifest.permission.CAMERA);

    private final Waiter waiter = new Waiter(10, 1);

    private void CreateFacility() {
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.facility), click());
        onView(withId(R.id.facilityHomePage)).check(matches(isDisplayed()));
        onView(withId(R.id.create_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Test facility"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText("Test location"));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("000000000000"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("tester@example.com"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("99"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
    }
    private void DestroyFacility() {
        waiter.perform(withId(R.id.facility), click());
        waiter.perform(withId(R.id.facility_page_edit_facility_button), click());
        waiter.perform(withId(R.id.facility_details_delete_button), click());
        waiter.check(withId(R.id.create_facility_button), matches(isDisplayed()));
    }

    @Test
    public void CreateEventValidation() {
        CreateFacility();
        waiter.perform(withId(R.id.facility_page_add_event_button), click());
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText("Validation test event title"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_location), replaceText("Test event location"));
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText("42"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_description), replaceText("Test event description"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        DestroyFacility();
    }

    @Test
    public void CreateEventBackwardNavigation() {
        CreateFacility();
        waiter.perform(withId(R.id.facility_page_add_event_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText("Backwards navigation test event title"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_location), replaceText("Test event location"));
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText("42"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_description), replaceText("Test event description"));
        waiter.perform(withId(R.id.create_event_negative_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_location), matches(withText("Test event location")));
        waiter.check(withId(R.id.create_event_edittext_event_capacity), matches(withText("42")));
        waiter.perform(withId(R.id.create_event_negative_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_title), matches(withText("Backwards navigation test event title")));
        waiter.perform(withId(R.id.create_event_negative_button), click());
        DestroyFacility();
    }

    public void CreateEvent(String title) {
        CreateFacility();
        waiter.perform(withId(R.id.facility_page_add_event_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText(title));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_location), replaceText("Test event location"));
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText("42"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.perform(withId(R.id.create_event_edittext_event_description), replaceText("Test event description"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
    }

    @Test
    public void EditEvent() {
        CreateEvent("Test event title");
        waiter.perform(withText("Test event title"), click());

        waiter.check(withId(R.id.event_organizer_event_description), matches(withText("Test event description")));
        waiter.perform(withId(R.id.event_organizer_edit_event_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_title), matches(withText("Test event title")));
        waiter.perform(withId(R.id.create_event_edittext_event_title), replaceText("Test event title edited"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_location), matches(withText("Test event location")));
        waiter.perform(withId(R.id.create_event_edittext_event_location), replaceText("Test event location edited"));
        waiter.check(withId(R.id.create_event_edittext_event_capacity), matches(withText("42")));
        waiter.perform(withId(R.id.create_event_edittext_event_capacity), replaceText("10"));
        waiter.perform(withId(R.id.create_event_positive_button), click());
        waiter.check(withId(R.id.create_event_edittext_event_description), matches(withText("Test event description")));
        waiter.perform(withId(R.id.create_event_edittext_event_description), replaceText("Test event description edited"));
        waiter.perform(withId(R.id.create_event_positive_button), click());

        waiter.check(withId(R.id.event_organizer_event_description), matches(withText("Test event description edited")));
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
