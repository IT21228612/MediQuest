package com.example.doctor2;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ViewAllRequestsTest {

    @Before
    public void launchActivity() {
        // Launch ViewAllRequests activity before each test
        ActivityScenario.launch(ViewAllRequests.class);
    }

    @Test
    public void testFilterDialog() {
        // Click the filter button to open the filter dialog
        Espresso.onView(ViewMatchers.withId(R.id.filterButton)).perform(ViewActions.click());

        // Check if the filter dialog is displayed
        Espresso.onView(ViewMatchers.withText("Select Filter")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Click one of the filter options, e.g., "Accepted" (modify as needed)
        Espresso.onView(ViewMatchers.withText("Accepted")).perform(ViewActions.click());


    }

    @Test
    public void testItemClick() {
        // Click on an item in the RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(ViewActions.click());

        // Add assertions to validate the behavior, e.g., check if the expected activity is started
        // For example, if clicking on an item should start Activity2
        // You can use IntentsTestRule or ActivityScenario to monitor and validate the expected activity launch.
    }

    // Add more test methods to cover other functionality
}

