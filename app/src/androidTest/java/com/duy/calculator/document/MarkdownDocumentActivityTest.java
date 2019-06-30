package com.duy.calculator.document;

import android.support.test.espresso.contrib.RecyclerViewActions;

import com.duy.calculator.BaseTestCase;
import com.duy.calculator.R;
import com.duy.ncalc.document.model.FunctionDocumentItem;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;

public class MarkdownDocumentActivityTest extends BaseTestCase {

    @Test
    public void testOpenOpenDocument() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());

        onView(withText("All functions")).perform(click());
        onView(withText("Combinatorial")).perform(click());

        onView(withText("Combinatorial")).check(matches(isDisplayed()));
    }

    @Test
    public void testSearch() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());

        onView(withText("All functions")).perform(click());
        onView(withId(R.id.edit_search_view)).perform(typeText("ArcSin"));

        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText("ArcSin")).check(matches(isDisplayed()));

        pressBack();

        onView(withText("Documentation")).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenDocumentationFromIDEActivity() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());

        onView(withText("IDE mode")).perform(click());
        onView(withId(R.id.fab_help)).perform(click());

        onView(withText("Documentation")).check(matches(isDisplayed()));

        pressBack();

        onView(allOf(isDisplayed(), withText("IDE mode"))).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenDocumentationSuggestionPopupActivity() {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());

        onView(withText("IDE mode")).perform(click());
        onView(withId(R.id.edit_input)).perform(clearText(), typeText("ArcS"));

        onData(instanceOf(FunctionDocumentItem.class)).inRoot(isPlatformPopup()).atPosition(0).perform(click()); //ArcSec
        onView(withId(R.id.edit_input)).check(matches(withText("ArcSec")));

    }
}