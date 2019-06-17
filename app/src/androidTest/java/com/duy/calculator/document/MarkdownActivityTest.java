package com.duy.calculator.document;

import com.duy.calculator.BaseTestCase;
import com.duy.calculator.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class MarkdownActivityTest extends BaseTestCase {

    @Test
    public void openOpenDocument() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());

        onView(withText("All functions")).perform(click());
        onView(withText("AbsArg")).perform(click());

        onView(withText("AbsArg")).check(matches(isDisplayed()));
    }
}