package my.projects.habittracker.view.main;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest extends BaseMainActivityTest {

    @Test
    public void buildTest() {

    }

    @Test
    public void addMenuItemIsDisplayed() {
        onView(withId(R.id.add_menu_item)).check(matches(isDisplayed()));
    }

    @Test
    public void habitsAreDisplayed() {
        assertSimpleHabitIsDisplayed();
        assertHabitWithChangesIsDisplayed();
    }

    private void assertSimpleHabitIsDisplayed() {
        onView(allOf(withId(R.id.habit_item_title), withText(TestDataProvider.simpleHabit.getTitle()))).check(matches(isDisplayed()));
    }

    private void assertHabitWithChangesIsDisplayed() {
        onView(allOf(withId(R.id.habit_item_title), withText(TestDataProvider.habitWithChanges.getTitle()))).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnHabitOpensDetailsFragment() {
        clickOnSimpleHabit();

        assertDetailsFragmentOpened();
    }

    private void clickOnSimpleHabit() {
        onView(allOf(withId(R.id.habit_item_title), withText(TestDataProvider.simpleHabit.getTitle()))).perform(click());
    }

    private void assertDetailsFragmentOpened() {
        onView(withId(R.id.details_habit_title)).check(matches(isDisplayed()));
        onView(withId(R.id.details_habit_description)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnAddHabitMenuItemOpensAddHabitFragment() {
        clickOnAddMenuItem();

        assertAddHabitFragmentOpened();
    }

    private void assertAddHabitFragmentOpened() {
        onView(withId(R.id.add_title_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.add_desc_edit_text)).check(matches(isDisplayed()));
    }

    private void clickOnAddMenuItem() {
        onView(withId(R.id.add_menu_item)).perform(click());
    }

    @Test
    public void clickOnAddChangeButtonOnSimpleHabitShowsAddChangeDialog() {
        clickOnAddChangeButtonForSimpleHabit();

        assertAddChangeDialogIsDisplayed();
    }

    private void clickOnAddChangeButtonForSimpleHabit() {
        onView(allOf(withId(R.id.habit_item_add_change_button), withParent(hasSibling(withText(TestDataProvider.simpleHabit.getTitle()))))).perform(click());
    }

    private void assertAddChangeDialogIsDisplayed() {
        onView(withId(R.id.add_change_value_edit_text)).check(matches(isDisplayed()));
    }
}
