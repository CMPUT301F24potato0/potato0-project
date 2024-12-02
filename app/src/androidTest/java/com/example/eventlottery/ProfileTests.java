package com.example.eventlottery;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
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
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the profiles test
 * This tests the profile page of the app
 * Permissions are auto granted to the app
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileTests {
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
     * This method navigates to the profile page
     */
    private void NavigateToProfile() {
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.profile), click());
        waiter.check(withId(R.id.fProfile), matches(isDisplayed()));
    }

    /**
     * This test checks if the profile page is displayed
     * This method calls the NavigateToProfile method
     * then checks if the save button is disabled
     * Then replaces the first name, last name, phone number, and email
     * then clicks the save button
     * Then it goes to scanner view and then navigates back to the profile to see if the profile is being saved
     */
    @Test
    public void ProfileEditPersistent() {
        NavigateToProfile();
        onView(withId(R.id.saveProfileBtn)).check(matches(isNotEnabled()));
        onView(withId(R.id.fNameEditText)).perform(replaceText("Persistence tester first name"));
        onView(withId(R.id.lNameEditText)).perform(replaceText("Persistence tester last name"));
        onView(withId(R.id.phoneEditText)).perform(replaceText("000000000111"));
        onView(withId(R.id.emailEditText)).perform(replaceText("persist@example.com"));
        onView(withId(R.id.saveProfileBtn)).check(matches(isEnabled()));
        onView(withId(R.id.saveProfileBtn)).perform(click());
        // Navigating to scanner and navigating back to profile
        onView(withId(R.id.scanQR)).perform(click());
        NavigateToProfile();

        onView(withId(R.id.fNameEditText)).check(matches(withText("Persistence tester first name")));
        onView(withId(R.id.lNameEditText)).check(matches(withText("Persistence tester last name")));
        onView(withId(R.id.phoneEditText)).check(matches(withText("000000000111")));
        onView(withId(R.id.emailEditText)).check(matches(withText("persist@example.com")));
    }

    /**
     * This test checks if the profile page is discarding unsaved changes
     * It calls ProfileEditPersistent method
     * Then it replaces the first name, last name, phone number, and email
     * Then goes to the facility and goes back to profile page and checks if the new edits have been discarded
     */
    @Test
    public void ProfileEditDiscardUnsaved() {
        ProfileEditPersistent();
        waiter.perform(withId(R.id.fNameEditText), replaceText("Temporary first name"));
        onView(withId(R.id.lNameEditText)).perform(replaceText("Temporary last name"));
        onView(withId(R.id.phoneEditText)).perform(replaceText("111111111111"));
        onView(withId(R.id.emailEditText)).perform(replaceText("temp@example.com"));
        closeSoftKeyboard();
        waiter.perform(withId(R.id.facility), click());
        onView(withId(R.id.profile)).perform(click());
        waiter.check(withId(R.id.fNameEditText), matches(withText("Persistence tester first name")));
        onView(withId(R.id.lNameEditText)).check(matches(withText("Persistence tester last name")));
        onView(withId(R.id.phoneEditText)).check(matches(withText("000000000111")));
        onView(withId(R.id.emailEditText)).check(matches(withText("persist@example.com")));
    }
}
