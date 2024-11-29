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
    private void NavigateToProfile() {
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.profile), click());
        waiter.check(withId(R.id.fProfile), matches(isDisplayed()));
    }

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

//    @Test
//    public void TestProfilePicture() {
//        NavigateToProfile();
//        onView(isRoot()).perform(waitFor(1000));
//        try {
//            onView(withId(R.id.delete_picture)).perform(click());
//        } catch (Exception e) {
//            Log.e("Profile Picture Testing", e.toString());
//        }
//        onView(withId(R.id.add_picture)).perform(click());
//        waitFor(5000);
//        // Checking if imageView is not empty
//        onView(withId(R.id.profilePicture)).check(new ViewAssertion() {
//            @Override
//            public void check(View view, NoMatchingViewException e) {
//                if (view instanceof ImageView) {
//                    ImageView iv = (ImageView) view;
//                    assertNotEquals(iv.getDrawable(), null);
//                } else {
//                    throw e;
//                }
//            }
//        });
//    }
}
