package com.vibbix.ballroom;

import android.os.SystemClock;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralClickAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Tap;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.WindowManager;
import android.widget.Checkable;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests the main activity view
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityInstrumentationTests {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(activity_main.class);

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
                //or ((SeekBar) view).setProgress(progress);
            }

            @Override
            public String getDescription() {
                return "Set a progress";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return new Matcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {
                    }

                    @Override
                    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }
    @Before
    public void setUp() {
        final activity_main activity = (activity_main) mActivityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @Test
    public void changeToMetric(){
        onView(withId(R.id.switchMetric)).perform(setChecked(true));
        onView(withId(R.id.switchEasy)).perform(setChecked(false));
        onView(withId(R.id.txtunitSquared)).check(matches(withText(R.string.MeterSquared)));
        onView(withId(R.id.txtunitsmall)).check(matches(withText(R.string.centimeter)));
        onView(withId(R.id.txtunitstd)).check(matches(withText(R.string.Meter)));

    }

    @Test
    public void changeToImperial() {
        onView(withId(R.id.switchMetric)).perform(setChecked(false));
        onView(withId(R.id.switchEasy)).perform(setChecked(false));
        onView(withId(R.id.txtunitSquared)).check(matches(withText(R.string.FeetSquared)));
        onView(withId(R.id.txtunitsmall)).check(matches(withText(R.string.Inchs)));
        onView(withId(R.id.txtunitstd)).check(matches(withText(R.string.Feet)));
    }

    @Test
    public void switchOffEasyMode() {
        onView(withId(R.id.switchEasy)).perform(setChecked(false));
        onView(withId(R.id.seekEfficiency)).check(matches(isDisplayed()));
        onView(withId(R.id.txtEfficiencyPercent)).check(matches(isDisplayed()));
        onView(withId(R.id.txtEfficiency)).check(matches(isDisplayed()));
        onView(withId(R.id.decimalRadius)).check(matches(isDisplayed()));
        onView(withId(R.id.txtconstRadius)).check(matches(isDisplayed()));
        onView(withId(R.id.txtunitsmall)).check(matches(isDisplayed()));
    }

    @Test
    public void switchToEasyMode() {
        //onView(withId(R.id.switchEasy)).perform(setChecked(true));
        //SystemClock.sleep(500); //wait for animation to finish
        //onView(withId(R.id.llEfficiency)).check(matches(not(isDisplayed())));
        //onView(withId(R.id.llRadius)).check(matches(not(isDisplayed())));
    }

    @Test
    public void verifyMetricCalculations() {
        onView(withId(R.id.switchMetric)).perform(setChecked(true));
        onView(withId(R.id.switchEasy)).perform(setChecked(false));
        onView(withId(R.id.seekEfficiency)).perform(setProgress(70));
        onView(withId(R.id.decimalArea)).perform(ViewActions.replaceText("10.0"));
        onView(withId(R.id.decimalDepth)).perform(ViewActions.replaceText("2"));
        onView(withId(R.id.decimalRadius)).perform(ViewActions.replaceText("7.6"));
        onView(withId(R.id.decimalMoney)).perform(ViewActions.replaceText(".19"));
        onView(withId(R.id.txtResult)).check(matches(ViewMatchers.withText("It would take 7613 " +
                "balls and cost $1446.47 to fill the area with playpen balls")));

    }

    @Test
    public void verifyMetricCalculationsEasy() {
        onView(withId(R.id.switchMetric)).perform(setChecked(true));
        onView(withId(R.id.switchEasy)).perform(setChecked(true));
        onView(withId(R.id.decimalArea)).perform(ViewActions.replaceText("10.0"));
        onView(withId(R.id.decimalDepth)).perform(ViewActions.replaceText("2"));
        onView(withId(R.id.decimalMoney)).perform(ViewActions.replaceText(".19"));
        onView(withId(R.id.txtResult)).check(matches(ViewMatchers.withText("It would take 6961 " +
                "balls and cost $1322.59 to fill the area with playpen balls")));
    }

    @Test
    public void verifyImperialCalculations() {
        onView(withId(R.id.switchMetric)).perform(setChecked(false));
        onView(withId(R.id.switchEasy)).perform(setChecked(false));
        onView(withId(R.id.seekEfficiency)).perform(setProgress(74));
        onView(withId(R.id.decimalArea)).perform(ViewActions.replaceText("10.0"));
        onView(withId(R.id.decimalDepth)).perform(ViewActions.replaceText("2"));
        onView(withId(R.id.decimalMoney)).perform(ViewActions.replaceText(".19"));
        onView(withId(R.id.decimalRadius)).perform(ViewActions.replaceText("3"));
        onView(withId(R.id.txtResult)).check(matches(ViewMatchers.withText("It would take 226 " +
                "balls and cost $42.94 to fill the area with playpen balls")));
    }

    @Test
    public void verifyImperialCalculationsEasy() {
        onView(withId(R.id.switchMetric)).perform(setChecked(false));
        onView(withId(R.id.switchEasy)).perform(setChecked(true));
        onView(withId(R.id.decimalArea)).perform(ViewActions.replaceText("10.0"));
        onView(withId(R.id.decimalDepth)).perform(ViewActions.replaceText("2"));
        onView(withId(R.id.decimalMoney)).perform(ViewActions.replaceText(".19"));
        onView(withId(R.id.txtResult)).check(matches(ViewMatchers.withText("It would take 195 " +
                "balls and cost $37.05 to fill the area with playpen balls")));
    }

    @Test
    public void noTextDisplayedWhenNoBalls() {
        onView(withId(R.id.seekEfficiency)).perform(setProgress(0));
        onView(withId(R.id.decimalArea)).perform(ViewActions.replaceText("0"));
        onView(withId(R.id.decimalDepth)).perform(ViewActions.replaceText("0"));
        onView(withId(R.id.decimalMoney)).perform(ViewActions.replaceText("0"));
        SystemClock.sleep(500); //wait for animation to finish
        onView(withId(R.id.txtResult)).check(matches(not(isCompletelyDisplayed())));
    }

    @Test
    public void noBackspaceCreatingCharacters() {
        onView(withId(R.id.decimalArea)).perform(ViewActions.replaceText(""));
        onView(withId(R.id.decimalArea)).check(matches(not(withText("0.00"))));
    }

    @Test
    public void maxBoundsOfSeekbar() {
        onView(withId(R.id.seekEfficiency)).perform(new GeneralClickAction(Tap.SINGLE,
                GeneralLocation.CENTER_RIGHT, Press.FINGER));
        onView(withId(R.id.txtEfficiencyPercent)).check(matches(withText(
                ObservableBallRoomCalculator.MAX_DENSITY.toBigInteger().toString() + "%")));
    }
}
