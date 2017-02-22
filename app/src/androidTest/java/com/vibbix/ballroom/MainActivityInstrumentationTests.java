package com.vibbix.ballroom;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests the main activity view
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTests {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(activity_main.class);

    @Test
    public void changeToMetric(){
        onView(withId(R.id.switchMetric)).perform(ViewActions.click());
        onView(withId(R.id.txtunitSquared)).check(matches(withText(R.string.MeterSquared)));
    }

    @Test
    public void switchToEasyMode(){
        onView(withId(R.id.seekEfficiency)).check(matches(isDisplayed()));
        onView(withId(R.id.txtEfficiencyPercent)).check(matches(isDisplayed()));
        onView(withId(R.id.txtEfficiency)).check(matches(isDisplayed()));
        onView(withId(R.id.decimalRadius)).check(matches(isDisplayed()));
        onView(withId(R.id.txtconstRadius)).check(matches(isDisplayed()));
        onView(withId(R.id.txtunitsmall)).check(matches(isDisplayed()));
        onView(withId(R.id.switchEasy)).perform(ViewActions.click());
        onView(withId(R.id.seekEfficiency)).check(matches(not(isDisplayed())));
        onView(withId(R.id.txtEfficiencyPercent)).check(matches(not(isDisplayed())));
        onView(withId(R.id.txtEfficiency)).check(matches(not(isDisplayed())));
        onView(withId(R.id.decimalRadius)).check(matches(not(isDisplayed())));
        onView(withId(R.id.txtconstRadius)).check(matches(not(isDisplayed())));
        onView(withId(R.id.txtunitsmall)).check(matches(not(isDisplayed())));
    }
}
