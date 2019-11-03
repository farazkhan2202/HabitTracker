package my.projects.habittracker.view.add;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddHabitActivityUITest extends BaseAddActivityTest {

    @Test
    public void showsBaseUI() {
        assertBaseUIIsDisplayed();
    }

    private void assertBaseUIIsDisplayed() {
        onView(withId(R.id.add_title_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.add_desc_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.ok_menu_item)).check(matches(isDisplayed()));
    }

    @Test
    public void confirmingHabitWithEmptyTitleDisplaysEmptyTitleError() {
        clickOkMenuItem();

        assertBaseUIIsDisplayed();
        assertEmptyTitleErrorIsDisplayed();
    }

    private void clickOkMenuItem() {
        onView(withId(R.id.ok_menu_item)).perform(click());
    }

    private void assertEmptyTitleErrorIsDisplayed() {
        onView(withId(R.id.add_title_edit_text)).check(matches(hasErrorText(getStringRes(R.string.empty_title_error))));
    }

    private String getStringRes(int id) {
        return activityTestRule.getActivity().getString(id);
    }

    @Test
    public void confirmingHabitWithExistingTitleShowsTitleExistsError() {
        typeExistingTitle();

        clickOkMenuItem();

        assertBaseUIIsDisplayed();
        assertTitleAlreadyExistsErrorIsDisplayed();
    }

    private void typeExistingTitle() {
        onView(withId(R.id.add_title_edit_text)).perform(typeText(TestDataProvider.simpleHabit.getTitle()), ViewActions.closeSoftKeyboard());
    }

    private void assertTitleAlreadyExistsErrorIsDisplayed() {
        onView(withId(R.id.add_title_edit_text)).check(matches(hasErrorText(getStringRes(R.string.title_already_exists_error))));
    }

    @Test
    public void clickOnConfirmItemClosesAddHabitFragment() {
        typeTitle("title");
        clickOkMenuItem();

        assertAddHabitFragmentClosed();
    }

    private void typeTitle(String str) {
        onView(withId(R.id.add_title_edit_text)).perform(typeText(str), closeSoftKeyboard());
    }

    private void assertAddHabitFragmentClosed() {
        onView(withId(R.id.add_title_edit_text)).check(doesNotExist());
        onView(withId(R.id.add_desc_edit_text)).check(doesNotExist());
    }

    @Test
    public void clickOnConfirmMenuItemAddsHabit() {
        typeTitle("title");
        clickOkMenuItem();

        assertHabitWithTitleAdded("title");
    }

    private void assertHabitWithTitleAdded(String str) {
        onView(withText(str)).check(matches(isDisplayed()));
    }

}
