package com.example.eventlottery;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FacilityTests {

    private final Waiter waiter = new Waiter(20, 1);
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(Manifest.permission.CAMERA);
    @Test
    public void NavigateToFacility() {
        waiter.perform(withId(R.id.facility), click());
        onView(withId(R.id.facilityHomePage)).check(matches(isDisplayed()));
    }

    private void CreateFacility() {
        NavigateToFacility();
        onView(withId(R.id.create_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Test facility"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText("Test location"));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("000000000000"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("tester@example.com"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("99"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
    }

    @Test
    public void CreateEditDeleteFacilityTest() {
        CreateFacility();
        waiter.check(withId(R.id.facility_page_facility_name), matches(withText("Test facility")));
        onView(withId(R.id.facility_page_edit_facility_button)).perform(click());
        waiter.check(withId(R.id.facility_details_edittext_facility_name), matches(withText("Test facility")));
        onView(withId(R.id.facility_details_edittext_location)).check(matches(withText("Test location")));
        onView(withId(R.id.facility_details_edittext_phone_number)).check(matches(withText("000000000000")));
        onView(withId(R.id.facility_details_edittext_email)).check(matches(withText("tester@example.com")));
        onView(withId(R.id.facility_details_edittext_capacity)).check(matches(withText("99")));
        waiter.perform(withId(R.id.facility_details_delete_button), click());
        waiter.check(withId(R.id.create_facility_button), matches(isDisplayed()));
    }

    @Test
    public void InvalidFacilityCreation() {
        NavigateToFacility();
        onView(withId(R.id.create_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Test facility"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText(""));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("abcdefgh"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("1343512345"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("99"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
        waiter.check(withId(R.id.facility_details_cancel_button), matches(isDisplayed()));
    }
}
