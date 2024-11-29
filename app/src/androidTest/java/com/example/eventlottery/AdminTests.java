package com.example.eventlottery;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.eventlottery.Admin.AdminEventDetailsActivity;
import com.example.eventlottery.Admin.AdminMainActivity;
import com.example.eventlottery.Models.FacilityModel;
import com.example.eventlottery.Models.UserModel;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AdminTests {

    class Deleted extends Exception {
        public Deleted(String message) {
            super(message);
        }
    }

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

    private UserModel curUser;
    private FacilityModel facilityModel;

    private final Waiter waiter = new Waiter(20, 1);
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        waiter.perform(withId(R.id.scanQR), click());
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.facility), click());
        waiter.check(withId(R.id.facilityOrganizerHomePage), matches(isDisplayed()));
        waiter.perform(withId(R.id.scanQR), click());
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        activityRule.getScenario().onActivity(activity -> {
            curUser = activity.getUser();
            facilityModel = activity.getFacility();
        });
    }

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
    );
    String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView)view; //Save, because of check in getConstraints()
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

    @Test
    public void TestAdmin() {
        String fName = "IntentTestAdmin";
        String lName = "IntentTestAdmin";
        String email = "tester@example.com";
        String phone = "000000000000";
        CreateAProfile(fName, lName, email, phone);
        NavigateToFacility();
        try {
            CreatingFacility();
        } catch (Exception e) {
            Log.d("AdminTests","Facility already exist");
        }
        String eventTitle = UUID.randomUUID().toString();
        String eventLocation = "Admin Intent Test Location";
        String geoLocation = "Yes";
        String eventCapacity = "55";
        String eventDescription = "Admin testing event";
        String waitListLimit = "66";
        CreateEvent(eventTitle, eventLocation, geoLocation, eventCapacity, eventDescription, waitListLimit);
        NavigateToProfile();
        Intents.init();
        waiter.perform(withId(R.id.admin_button), click());
        intended(hasComponent(AdminMainActivity.class.getName()));
        Intents.release();
        onView(withId(R.id.adminEventPage)).check(matches(isDisplayed()));
        Intents.init();
        onView(withText(eventTitle)).perform(scrollTo(), click());
        intended(hasComponent(AdminEventDetailsActivity.class.getName()));
        onView(withId(R.id.admin_event_title)).check(matches(withText(eventTitle)));
        onView(withId(R.id.admin_event_organizer_event_description)).check(matches(withText(eventDescription)));
        onView(withId(R.id.organizer_name)).check(matches(withText(curUser.getfName() + " " + curUser.getlName())));
        pressBack();
        Intents.release();
        waiter.perform(withId(R.id.facilitiesAdmin), click());
        onView(withText(facilityModel.getName())).perform(scrollTo(), click());
        waiter.check(withId(R.id.facility_details_text_location), matches(isDisplayed()));

        onView(withId(R.id.facility_details_text_facility_name)).check(matches(withText("Name: " + facilityModel.getName())));
        onView(withId(R.id.facility_details_text_location)).check(matches(withText("Location: " + facilityModel.getLocation())));
        onView(withId(R.id.facility_details_text_phone_number)).check(matches(withText("Phone: " + facilityModel.getPhone())));
        onView(withId(R.id.facility_details_text_email)).check(matches(withText("Email: " + facilityModel.getEmail())));
        onView(withId(R.id.facility_details_text_capacity)).check(matches(withText("Capacity: " + facilityModel.getCapacity().toString())));
        onView(withId(R.id.delete_button)).perform(click());
        try {
            onView(withText(facilityModel.getName())).check(matches(isDisplayed()));
            throw new Deleted("Facility not deleted");
        } catch (NoMatchingViewException e) {
            Log.d("AdminTests","Facility deleted");
        } catch (Deleted e) {
            throw new RuntimeException(e);
        }
        onView(withId(R.id.profilesAdmin)).perform(click());
        onView(withText(curUser.getfName() + " " + curUser.getlName())).perform(scrollTo(), click());
        onView(withId(R.id.admin_name_info)).check(matches(withText("Name: " + curUser.getfName() + " " + curUser.getlName())));
        onView(withId(R.id.admin_email_info)).check(matches(withText("Email: " + curUser.getEmail())));
        onView(withId(R.id.admin_phone_info)).check(matches(withText("Phone: " + curUser.getPhone())));
        onView(withId(R.id.admin_delete_user)).perform(click());
        try{
            onView(withText(curUser.getfName())).check(matches(isDisplayed()));
            throw new Deleted("User not deleted");
        } catch (NoMatchingViewException e) {
            Log.d("AdminTests","User deleted");
        } catch (Deleted e) {
            throw new RuntimeException(e);
        }
        Intents.init();
        onView(withId(R.id.back_main)).perform(click());
        Intents.release();
    }

    private void NavigateToProfile() {
        waiter.perform(withId(R.id.profile), click());
        waiter.check(withId(R.id.fProfile), matches(isDisplayed()));
    }

    private void NavigateToFacility() {
        onView(withId(R.id.scanQR)).perform(click());
        waiter.check(withId(R.id.scannerView), matches(isDisplayed()));
        waiter.perform(withId(R.id.facility), click());
        waiter.check(withId(R.id.facilityOrganizerHomePage), matches(isDisplayed()));
    }

    private void CreateAProfile(
            String firstName,
            String lastName,
            String email,
            String phone
    ) {
        NavigateToProfile();
        waiter.perform(withId(R.id.fNameEditText), replaceText(firstName));
        waiter.perform(withId(R.id.lNameEditText), replaceText(lastName));
        waiter.perform(withId(R.id.phoneEditText), replaceText(phone));
        waiter.perform(withId(R.id.emailEditText), replaceText(email));
        waiter.perform(withId(R.id.saveProfileBtn), click());
    }

    private void CreatingFacility() {
        onView(withId(R.id.create_facility_button)).perform(click());
        waiter.perform(withId(R.id.facility_details_edittext_facility_name), replaceText("Admin Intents Test Facility"));
        onView(withId(R.id.facility_details_edittext_location)).perform(replaceText("Admin Intent Test Location"));
        onView(withId(R.id.facility_details_edittext_phone_number)).perform(replaceText("000000000000"));
        onView(withId(R.id.facility_details_edittext_email)).perform(replaceText("tester@example.com"));
        onView(withId(R.id.facility_details_edittext_capacity)).perform(replaceText("99"));
        onView(withId(R.id.facility_details_confirm_button)).perform(click());
    }

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
}
