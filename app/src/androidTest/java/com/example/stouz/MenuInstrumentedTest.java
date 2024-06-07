package com.example.stouz;

import android.Manifest;
import android.app.UiAutomation;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.AdditionalMatchers.not;
import static java.lang.Thread.sleep;

import com.example.stouz.MainActivity;
import com.example.stouz.R;
import com.google.android.gms.ads.AdView;

@RunWith(AndroidJUnit4.class)
public class MenuInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @BeforeClass
    public static void disableAnimations() {
        UiAutomation uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        uiAutomation.executeShellCommand("settings put global window_animation_scale 0");
        uiAutomation.executeShellCommand("settings put global transition_animation_scale 0");
        uiAutomation.executeShellCommand("settings put global animator_duration_scale 0");
    }

    @AfterClass
    public static void enableAnimations() {
        UiAutomation uiAutomation = InstrumentationRegistry.getInstrumentation().getUiAutomation();
        uiAutomation.executeShellCommand("settings put global window_animation_scale 1");
        uiAutomation.executeShellCommand("settings put global transition_animation_scale 1");
        uiAutomation.executeShellCommand("settings put global animator_duration_scale 1");
    }


    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testImageExists() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void testNameExists() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.textViewName)).check(matches(isDisplayed()));
    }
    @Test
    public void testRecyclerView() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void testMapExists() {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.map_container)).check(matches(isDisplayed()));
    }
}
