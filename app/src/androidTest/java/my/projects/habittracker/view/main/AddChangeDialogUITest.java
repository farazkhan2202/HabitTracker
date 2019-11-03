package my.projects.habittracker.view.main;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.And;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class AddChangeDialogUITest extends BaseMainActivityTest {

    @Test
    public void baseUIIsDisplayed() {
        clickOnSimpleHabitAddChangeButton();

        assertConfirmButtonIsDisplayed();
        assertCancelButtonIsDisplayed();
        assertValueEditTextIsDisplayed();
        assertUnitSpinnerIsDisplayed();
    }

    private void clickOnSimpleHabitAddChangeButton() {
        onView(allOf(withId(R.id.habit_item_add_change_button), withParent(hasSibling(withText(TestDataProvider.simpleHabit.getTitle()))))).perform(click());
    }

    private void assertConfirmButtonIsDisplayed() {
        onView(withId(R.id.confirm_button)).check(matches(isDisplayed()));
    }

    private void assertCancelButtonIsDisplayed() {
        onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));
    }

    private void assertValueEditTextIsDisplayed() {
        onView(withId(R.id.add_change_value_edit_text)).check(matches(isDisplayed()));
    }

    private void assertUnitSpinnerIsDisplayed() {
        onView(withId(R.id.add_change_unit_spinner)).check(matches(isDisplayed()));
    }

    @Test
    public void showsCorrectUnitSet() {
        clickOnHabitWithChangesAddChangeButton();

        clickOnUnitsSpinner();
        assertTimeUnitsAreDisplayed();
    }

    private void clickOnHabitWithChangesAddChangeButton() {
        onView(allOf(withId(R.id.habit_item_add_change_button), withParent(hasSibling(withText(TestDataProvider.habitWithChanges.getTitle()))))).perform(click());
    }

    private void clickOnUnitsSpinner() {
        onView(withId(R.id.add_change_unit_spinner)).perform(click());
    }

    private void assertTimeUnitsAreDisplayed() {
        onData(withSpinnerText("minutes")).inAdapterView(withId(R.id.add_unit_set_spinner));
        onData(withSpinnerText("minutes")).inAdapterView(withId(R.id.add_unit_set_spinner));
    }

    @Test
    public void clickOnCancelButtonClosesDialog() {
        clickOnSimpleHabitAddChangeButton();

        clickOnCancelButton();

        assertDialogIsClosed();
    }

    private void clickOnCancelButton() {
        onView(withId(R.id.cancel_button)).perform(click());
    }

    private void assertDialogIsClosed() {
        onView(withId(R.id.add_change_value_edit_text)).check(doesNotExist());
        onView(withId(R.id.add_unit_set_spinner)).check(doesNotExist());
    }

    @Test
    public void clickOnCancelButtonDoesNotAddChange() {
        clickOnSimpleHabitAddChangeButton();

        typeValue("6");
        clickOnCancelButton();
        navigateToSimpleHabitHistory();

        assertNoChangeWasAdded();
    }

    private void typeValue(String str) {
        onView(withId(R.id.add_change_value_edit_text)).perform(typeText(str), closeSoftKeyboard());
    }

    private void navigateToSimpleHabitHistory() {
        clickOnSimpleHabit();
        clickOnHistoryMenuItem();
    }

    private void clickOnSimpleHabit() {
        onView(withText(TestDataProvider.simpleHabit.getTitle())).perform(click());
    }

    private void clickOnHistoryMenuItem() {
        onView(withId(R.id.details_history_menu_item)).perform(click());
    }

    private void assertNoChangeWasAdded() {
        onView(withId(R.id.change_item_text)).check(doesNotExist());
        onView(withId(R.id.change_item_delete_button)).check(doesNotExist());
    }

    @Test
    public void clickOnConfirmButtonClosesDialog() {
        clickOnSimpleHabitAddChangeButton();

        typeValue("6");
        clickOnConfirmButton();
        navigateToSimpleHabitHistory();

        assertDialogIsClosed();
    }

    private void clickOnConfirmButton() {
        onView(withId(R.id.confirm_button)).perform(click());
    }

    @Test
    public void clickOnConfirmButtonWithInvalidValueShowsError() {
        clickOnSimpleHabitAddChangeButton();

        typeValue("0");
        clickOnConfirmButton();

        assertInvalidValueErrorIsDisplayed();
    }

    private void assertInvalidValueErrorIsDisplayed() {
        String error = activityTestRule.getActivity().getString(R.string.invalid_value_error);
        onView(withId(R.id.add_change_value_edit_text)).check(matches(hasErrorText(error)));
    }

    @Test
    public void clickOnConfirmButtonAddsChange() {
        clickOnSimpleHabitAddChangeButton();

        typeValue("7");
        clickOnConfirmButton();
        navigateToSimpleHabitHistory();

        assertChangeWasAdded();
    }

    private void assertChangeWasAdded() {
        onView(withId(R.id.change_item_text)).check(matches(isDisplayed()));
        onView(withId(R.id.change_item_delete_button)).check(matches(isDisplayed()));
    }

}
