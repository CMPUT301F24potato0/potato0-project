package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.eventlottery.Waiter;

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
        waiter.perform(withId(R.id.profile), click());
        onView(withId(R.id.fProfile)).check(matches(isDisplayed()));
    }
}
