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
public class ProfileTests {

    private final Waiter waiter = new Waiter(20, 1);
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(Manifest.permission.CAMERA);
    @Test
    public void NavigateToProfile() {
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.profile), click());
        waiter.check(withId(R.id.fProfile), matches(isDisplayed()));
    }

    @Test
    public void ProfileEditPersistent() {
        NavigateToProfile();
        waiter.perform(withId(R.id.fNameEditText), replaceText("Persistence tester first name"));
        onView(withId(R.id.lNameEditText)).perform(replaceText("Persistence tester last name"));
        onView(withId(R.id.phoneEditText)).perform(replaceText("000000000111"));
        onView(withId(R.id.emailEditText)).perform(replaceText("persist@example.com"));
        // closeSoftKeyboard();
        waiter.perform(withId(R.id.saveProfileBtn), click());
        onView(withId(R.id.facility)).perform(click());
        onView(withId(R.id.saveProfileBtn)).check(doesNotExist());
        onView(withId(R.id.profile)).perform(click());
        waiter.check(withId(R.id.fNameEditText), matches(withText("Persistence tester first name")));
        onView(withId(R.id.lNameEditText)).check(matches(withText("Persistence tester last name")));
        onView(withId(R.id.phoneEditText)).check(matches(withText("000000000111")));
        onView(withId(R.id.emailEditText)).check(matches(withText("persist@example.com")));
        waiter.perform(withId(R.id.fNameEditText), replaceText("Tester first name"));
        onView(withId(R.id.lNameEditText)).perform(replaceText("Tester last name"));
        onView(withId(R.id.phoneEditText)).perform(replaceText("000000000000"));
        onView(withId(R.id.emailEditText)).perform(replaceText("tester@example.com"));
        // closeSoftKeyboard();
        waiter.perform(withId(R.id.saveProfileBtn), click());
    }
    @Test
    public void ProfileEditDiscardUnsaved() {
        ProfileEditPersistent();
        waiter.perform(withId(R.id.fNameEditText), replaceText("Temporary first name"));
        onView(withId(R.id.lNameEditText)).perform(replaceText("Temporary last name"));
        onView(withId(R.id.phoneEditText)).perform(replaceText("111111111111"));
        onView(withId(R.id.emailEditText)).perform(replaceText("temp@example.com"));
        closeSoftKeyboard();
        waiter.perform(withId(R.id.facility), click());
        onView(withId(R.id.saveProfileBtn)).check(doesNotExist());
        onView(withId(R.id.profile)).perform(click());
        waiter.check(withId(R.id.fNameEditText), matches(withText("Tester first name")));
        onView(withId(R.id.lNameEditText)).check(matches(withText("Tester last name")));
        onView(withId(R.id.phoneEditText)).check(matches(withText("000000000000")));
        onView(withId(R.id.emailEditText)).check(matches(withText("tester@example.com")));
    }
}
