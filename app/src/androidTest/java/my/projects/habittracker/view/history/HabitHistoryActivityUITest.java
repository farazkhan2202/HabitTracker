package my.projects.habittracker.view.history;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class HabitHistoryActivityUITest extends BaseHabitHistoryActivityTest {

    @Test
    public void showsChangeText() {
        assertFirstChangeTextIsDisplayed();
        assertSecondChangeTextIsDisplayed();
    }

    private void assertFirstChangeTextIsDisplayed() {
        onView(allOf(withId(R.id.change_item_text), withText(TestDataProvider.changes.get(0).toString()))).check(matches(isDisplayed()));
    }

    private void assertSecondChangeTextIsDisplayed() {
        onView(allOf(withId(R.id.change_item_text), withText(TestDataProvider.changes.get(1).toString()))).check(matches(isDisplayed()));
    }

    @Test
    public void showsDeleteButtons() {
        assertFirstChangeDeleteButtonIsDisplayed();
        assertSecondChangeDeleteButtonIsDisplayed();
    }

    private void assertFirstChangeDeleteButtonIsDisplayed() {
        onView(allOf(withId(R.id.change_item_delete_button), hasSibling(withText(TestDataProvider.changes.get(0).toString())))).check(matches(isDisplayed()));
    }

    private void assertSecondChangeDeleteButtonIsDisplayed() {
        onView(allOf(withId(R.id.change_item_delete_button), hasSibling(withText(TestDataProvider.changes.get(1).toString())))).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnDeleteButtonDeletesChange() {
        clickOnFirstChangeDeleteButton();

        assertFirstChangeIsNotDisplayed();
    }

    private void clickOnFirstChangeDeleteButton() {
        onView(allOf(withId(R.id.change_item_delete_button), hasSibling(withText(TestDataProvider.changes.get(0).toString())))).perform(click());
    }

    private void assertFirstChangeIsNotDisplayed() {
        onView(allOf(withId(R.id.change_item_text), withText(TestDataProvider.changes.get(0).toString()))).check(doesNotExist());
    }

    @Test
    public void deletingAllChangesShowsNoChangesPlaceholder() {
        deleteAllChanges();

        assertNoChangesPlaceholderIsDisplayed();
    }

    private void deleteAllChanges() {
        clickOnFirstChangeDeleteButton();
        clickOnSecondChangeDeleteButton();
    }

    private void clickOnSecondChangeDeleteButton() {
        onView(allOf(withId(R.id.change_item_delete_button), hasSibling(withText(TestDataProvider.changes.get(1).toString())))).perform(click());
    }

    private void assertNoChangesPlaceholderIsDisplayed() {
        onView(withText(R.string.no_changes_placeholder)).check(matches(isDisplayed()));
    }
}
