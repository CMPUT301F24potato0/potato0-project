package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.Manifest;
import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Models.UserModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the facility test class
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FacilityTests {
    private FacilityModel facilityModel;
    private UserModel curUser;

    /**
     *  This is the setup method
     *  Called before every test
     *  This method gets the current user and facility model from MainActivity
     */
    @Before
    public void setup() {
        activityRule.getScenario().onActivity(activity -> {
            facilityModel = activity.getFacility();
            curUser = activity.getUser();
        });
    }
    private final Waiter waiter = new Waiter(20, 1);
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

    /**
     * This is the NavigateToFacility method
     * This method clicks scanQR and then checks if the scannerView is displayed
     * Then clicks facility and checks if the facilityOrganizerHomePage is displayed
     */
    private void NavigateToFacility() {
        waiter.perform(withId(R.id.scanQR), click());
//        onView(withId(R.id.scanQR)).perform(click());
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        onView(withId(R.id.facility)).perform(click());
        onView(withId(R.id.facilityOrganizerHomePage)).check(matches(isDisplayed()));
    }

    /**
     * This method tests if editing a facility is working
     * It navigates to facility and clicks the edit button
     * Then updates the facility information and checks if it is updated
     */
    private void TestEditFacility() {
        NavigateToFacility();
        waiter.check(withId(R.id.facility_page_facility_name), matches(withText("Test facility")));
        onView(withId(R.id.facility_page_edit_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Test facility 2"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText("Test location 2"));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("000000000001"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("tester2@example.com"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("100"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
        waiter.check(withId(R.id.facility_page_facility_name), matches(withText("Test facility 2")));
    }

    /**
     * This method checks if facility is being deleted
     * Navigates to facility then clicks the delete button
     */
    private void TestDeleteFacility() {
        NavigateToFacility();
        waiter.check(withId(R.id.facility_page_facility_name), matches(withText("Test facility 2")));
        onView(withId(R.id.facility_page_edit_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_delete_button), click());
        waiter.check(withId(R.id.create_facility_button), matches(isDisplayed()));
        assertEquals("", curUser.getFacilityID());
    }

    /**
     * This method tests if data validation on facility creation is working
     */
    private void TestInvalidFacilityCreation() {
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

    /**
     * This is the main test
     * This method tests all the methods above
     */
    @Test
    public void TestFacility() {
        NavigateToFacility();
        try {
            onView(withId(R.id.facility_page_edit_facility_button)).perform(click());
            waiter.perform(withId(R.id.facility_details_delete_button), click());
            waiter.check(withId(R.id.create_facility_button), matches(isDisplayed()));
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

        TestEditFacility();
        TestDeleteFacility();
        TestInvalidFacilityCreation();
    }
}
