package my.projects.habittracker.view.details;

import android.text.InputType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HabitDetailsActivityEditFieldsUITest extends BaseDetailsActivityEditUITest {

    @Test
    public void checkInitialUIState() {
        assertTitleEditTextIsEnabled();
        assertDescriptionEditTextIsEnabled();

        assertEditConfirmMenuItemIsDisplayed();
        assertEditCancelMenuItemIsDisplayed();
    }

    private void assertTitleEditTextIsEnabled() {
        assertViewInputTypeNotNull(R.id.details_habit_title);
    }

    private void assertDescriptionEditTextIsEnabled() {
        assertViewInputTypeNotNull(R.id.details_habit_description);
    }

    private void assertEditConfirmMenuItemIsDisplayed() {
        onView(withId(R.id.details_edit_ok_menu_item)).check(matches(isDisplayed()));
    }

    private void assertEditCancelMenuItemIsDisplayed() {
        onView(withId(R.id.details_edit_cancel_menu_item)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnCancelMenuItemDisablesInput() {
        clickOnCancelMenuItem();

        assertTitleEditTextIsDisabled();
        assertDescriptionEditTextIsDisabled();
    }

    private void clickOnCancelMenuItem() {
        onView(withId(R.id.details_edit_cancel_menu_item)).perform(click());
    }

    private void assertTitleEditTextIsDisabled() {
        onView(withId(R.id.details_habit_title)).check(matches(not(isEnabled())));
    }

    private void assertDescriptionEditTextIsDisabled() {
        onView(withId(R.id.details_habit_description)).check(matches(not(isEnabled())));
    }

    @Test
    public void clickOnCancelMenuItemRevertsTitleChanges() {
        typeInTitleEditText("upd");

        clickOnCancelMenuItem();

        assertTitleMatchesOriginal();
    }

    private void typeInTitleEditText(String str) {
        onView(withId(R.id.details_habit_title)).perform(typeText(str), closeSoftKeyboard());
    }

    private void assertTitleMatchesOriginal() {
        onView(withId(R.id.details_habit_title)).check(matches(withText(TestDataProvider.simpleHabit.getTitle())));
    }

    @Test
    public void clickOnCancelMenuItemRevertsDescriptionChanges() {
        typeInDescriptionEditText("upd");

        clickOnCancelMenuItem();

        assertDescriptionMatchesOriginal();
    }

    private void typeInDescriptionEditText(String str) {
        onView(withId(R.id.details_habit_description)).perform(replaceText(TestDataProvider.simpleHabit.getDescription() + str), closeSoftKeyboard());
    }

    private void assertDescriptionMatchesOriginal() {
        onView(withId(R.id.details_habit_description)).check(matches(withText(TestDataProvider.simpleHabit.getDescription())));
    }

    private void clickOnConfirmMenuItem() {
        onView(withId(R.id.details_edit_ok_menu_item)).perform(click());
    }

    @Test
    public void clickOnConfirmMenuItemSavesTitleChanges() {
        typeInTitleEditText("upd");

        clickOnConfirmMenuItem();
        Espresso.pressBack();

        assertHabitTitleAppendedWith("upd");
    }

    private void clickOnSimpleHabit() {
        onView(withText(TestDataProvider.simpleHabit.getTitle())).perform(click());
    }

    private void assertHabitTitleAppendedWith(String str) {
        onView(withText(TestDataProvider.simpleHabit.getTitle() + str)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnConfirmMenuItemSavesDescriptionChanges() {
        typeInDescriptionEditText("upd");

        clickOnConfirmMenuItem();
        Espresso.pressBack();
        clickOnSimpleHabit();

        assertDescriptionMatchesOriginalWithAppended("upd");
    }

    private void assertDescriptionMatchesOriginalWithAppended(String str) {
        onView(withId(R.id.details_habit_description)).check(matches(withText(TestDataProvider.simpleHabit.getDescription() + str)));
    }

    @Test
    public void clickOnConfirmMenuItemWithEmptyTitleShowsEmptyTitleError() {
        clearTitle();

        clickOnConfirmMenuItem();

        assertEmptyTitleErrorIsDisplayed();
    }

    private void clearTitle() {
        onView(withId(R.id.details_habit_title)).perform(clearText());
    }

    private void assertEmptyTitleErrorIsDisplayed() {
        String error = activityTestRule.getActivity().getString(R.string.empty_title_error);
        onView(withId(R.id.details_habit_title)).check(matches(hasErrorText(error)));
    }

    @Test
    public void clickOnConfirmMenuItemWithExistingTitleShowsExistingTitleError() {
        clearTitle();
        typeExistingTitle();

        clickOnConfirmMenuItem();

        assertExistingTitleErrorIsDisplayed();
    }

    private void typeExistingTitle() {
        String existingTitle = TestDataProvider.habitWithChanges.getTitle();
        typeInTitleEditText(existingTitle);
    }

    private void assertExistingTitleErrorIsDisplayed() {
        String error = activityTestRule.getActivity().getString(R.string.title_already_exists_error);
        onView(withId(R.id.details_habit_title)).check(matches(hasErrorText(error)));
    }
}
