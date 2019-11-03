package my.projects.habittracker.view.main;

import android.content.pm.ActivityInfo;

import org.junit.Before;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class MainActivityFragmentLogicTest extends BaseMainActivityTest {

    private void goLandscape() {
        activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void goPortrait() {
        activityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void placeholderFragmentIsOpenInLandscapeByDefault() {
        goLandscape();

        assertPlaceholderFragmentOpen();
    }

    private void assertPlaceholderFragmentOpen() {
        onView(withId(R.id.empty_placeholder_text)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnHabitClosesPlaceholderFragment() {
        goLandscape();

        clickOnSimpleHabit();

        assertPlaceholderFragmentClosed();
    }

    private void assertPlaceholderFragmentClosed() {
        onView(withId(R.id.empty_placeholder_text)).check(doesNotExist());
    }

    @Test
    public void clickOnAddHabitMenuItemClosesPlaceholderFragment() {
        goLandscape();

        clickOnAddHabitMenuItem();

        assertPlaceholderFragmentClosed();
    }

    @Test
    public void habitsFragmentIsOpenByDefault() {
        assertHabitsFragmentIsOpen();
    }

    private void assertHabitsFragmentIsOpen() {
        onView(withId(R.id.habit_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void habitsFragmentIsOpenAfterPortraitToLandscape() {
        goPortrait();
        goLandscape();

        assertHabitsFragmentIsOpen();
    }

    @Test
    public void habitsFragmentIsOpenAfterPortraitToLandscapeAndBack() {
        goPortrait();
        goLandscape();
        goPortrait();

        assertHabitsFragmentIsOpen();
    }

    @Test
    public void addHabitFragmentOpensInMainFrameInPortrait() {
        goPortrait();
        clickOnAddHabitMenuItem();

        assertHabitsFragmentIsClosed();
    }

    private void assertHabitsFragmentIsClosed() {
        onView(withId(R.id.habit_recycler_view)).check(doesNotExist());
    }

    @Test
    public void addHabitFragmentOpensInSecondFrameInLandscape() {
        goLandscape();
        clickOnAddHabitMenuItem();

        assertHabitsFragmentIsOpen();
        assertAddHabitFragmentIsOpen();
    }

    @Test
    public void addHabitFragmentIsOpenPortraitToLandscape() {
        goPortrait();
        clickOnAddHabitMenuItem();

        goLandscape();

        assertAddHabitFragmentIsOpen();
        assertHabitsFragmentIsOpen();
    }

    private void clickOnAddHabitMenuItem() {
        onView(withId(R.id.add_menu_item)).perform(click());
    }

    private void assertAddHabitFragmentIsOpen() {
        onView(withId(R.id.add_title_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.add_desc_edit_text)).check(matches(isDisplayed()));
    }

    @Test
    public void addHabitFragmentIsOpenAfterLandscapeToPortrait() {
        goLandscape();
        clickOnAddHabitMenuItem();

        goPortrait();

        assertAddHabitFragmentIsOpen();
        assertHabitsFragmentIsClosed();
    }

    @Test
    public void pressBackClosesAddHabitFragmentInPortrait() {
        goPortrait();
        clickOnAddHabitMenuItem();

        Espresso.pressBack();

        assertAddHabitFragmentIsClosed();
    }

    private void assertAddHabitFragmentIsClosed() {
        onView(withId(R.id.add_title_edit_text)).check(doesNotExist());
        onView(withId(R.id.add_desc_edit_text)).check(doesNotExist());
    }

    @Test
    public void pressBackClosesAddHabitFragmentInLandscape() {
        goLandscape();
        clickOnAddHabitMenuItem();

        Espresso.pressBack();

        assertAddHabitFragmentIsClosed();
        assertHabitsFragmentIsOpen();
        assertHabitsFragmentIsOpen();
    }

    @Test
    public void detailsFragmentOpensInMainFrameInPortrait() {
        goPortrait();
        clickOnSimpleHabit();

        assertHabitsFragmentIsClosed();
        assertDetailsFragmentIsOpen();
    }

    private void clickOnSimpleHabit() {
        onView(withText(TestDataProvider.simpleHabit.getTitle())).perform(click());
    }

    private void assertDetailsFragmentIsOpen() {
        onView(withId(R.id.details_habit_title)).check(matches(isDisplayed()));
        onView(withId(R.id.details_habit_description)).check(matches(isDisplayed()));
    }

    @Test
    public void detailsFragmentOpensInSecondFrameInLandscape() {
        goLandscape();
        clickOnSimpleHabit();

        assertHabitsFragmentIsOpen();
        assertDetailsFragmentIsOpen();
    }

    @Test
    public void pressBackClosesDetailsFragmentInPortrait() {
        goPortrait();
        clickOnSimpleHabit();

        Espresso.pressBack();

        assertDetailsFragmentIsClosed();
        assertHabitsFragmentIsOpen();
    }

    private void assertDetailsFragmentIsClosed() {
        onView(withId(R.id.details_habit_title)).check(doesNotExist());
        onView(withId(R.id.details_habit_description)).check(doesNotExist());
    }

    @Test
    public void pressBackClosesDetailsFragmentInLandscape() {
        goLandscape();
        clickOnSimpleHabit();

        Espresso.pressBack();

        assertDetailsFragmentIsClosed();
        assertHabitsFragmentIsOpen();
        assertPlaceholderFragmentOpen();
    }

    @Test
    public void detailsFragmentIsOpenAfterPortraitToLandscape() {
        goPortrait();
        clickOnSimpleHabit();

        goLandscape();

        assertDetailsFragmentIsOpen();
        assertHabitsFragmentIsOpen();
    }

    @Test
    public void detailsFragmentIsOpenAfterLandscapeToPortrait() {
        goLandscape();
        clickOnSimpleHabit();

        goPortrait();

        assertDetailsFragmentIsOpen();
        assertHabitsFragmentIsClosed();
    }

    @Test
    public void historyFragmentOpensInMainFrameInPortrait() {
        goPortrait();
        clickOnSimpleHabit();

        clickOnHistoryMenuItem();

        assertHistoryFragmentIsOpen();
        assertDetailsFragmentIsClosed();
        assertHabitsFragmentIsClosed();
    }

    private void clickOnHistoryMenuItem() {
        onView(withId(R.id.details_history_menu_item)).perform(click());
    }

    private void assertHistoryFragmentIsOpen() {
        onView(withId(R.id.changes_graph)).check(matches(isDisplayed()));
    }

    @Test
    public void historyFragmentOpensInSecondFrameInLandscape() {
        goLandscape();
        clickOnSimpleHabit();

        clickOnHistoryMenuItem();

        assertHistoryFragmentIsOpen();
        assertDetailsFragmentIsClosed();
        assertHabitsFragmentIsOpen();
    }

    @Test
    public void historyFragmentIsOpenAfterPortraitToLandscape() {
        goPortrait();
        clickOnSimpleHabit();
        clickOnHistoryMenuItem();

        goLandscape();

        assertHistoryFragmentIsOpen();
        assertDetailsFragmentIsClosed();
        assertHabitsFragmentIsOpen();
    }

    @Test
    public void historyFragmentIsOpenAfterLandscapeToPortrait() {
        goLandscape();
        clickOnSimpleHabit();
        clickOnHistoryMenuItem();

        goPortrait();

        assertHistoryFragmentIsOpen();
        assertDetailsFragmentIsClosed();
        assertHabitsFragmentIsClosed();
    }

    @Test
    public void pressBackClosesHistoryFragmentInPortrait() {
        goPortrait();
        clickOnSimpleHabit();
        clickOnHistoryMenuItem();

        Espresso.pressBack();

        assertHistoryFragmentIsClosed();
        assertDetailsFragmentIsOpen();
        assertHabitsFragmentIsClosed();
    }

    private void assertHistoryFragmentIsClosed() {
        onView(withId(R.id.changes_graph)).check(doesNotExist());
    }

    @Test
    public void pressBackClosesHistoryFragmentInLandscape() {
        goLandscape();
        clickOnSimpleHabit();
        clickOnHistoryMenuItem();

        Espresso.pressBack();

        assertHistoryFragmentIsClosed();
        assertDetailsFragmentIsOpen();
        assertHabitsFragmentIsOpen();
    }



}
