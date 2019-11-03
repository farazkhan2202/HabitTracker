package my.projects.habittracker.view.main;

import org.junit.Test;

import androidx.test.espresso.Espresso;
import my.projects.habittracker.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class MainActivityNoHabitsUITest extends BaseMainActivityNoHabitsTest {

    @Test
    public void noHabitsPlaceholderIsDisplayed() {
        onView(withText(R.string.no_habits_placeholder)).check(matches(isDisplayed()));
    }

    @Test
    public void noHabitPlaceholderNotDisplayedAfterAddingHabit() {
        clickOnAddMenuItem();

        typeHabitTitle("title");
        clickOkMenuItem();

        assertNoHabitsPlaceholderNotDisplayed();
    }

    private void clickOnAddMenuItem() {
        onView(withId(R.id.add_menu_item)).perform(click());
    }

    private void typeHabitTitle(String str) {
        onView(withId(R.id.add_title_edit_text)).perform(typeText(str), closeSoftKeyboard());
    }

    private void clickOkMenuItem() {
        onView(withId(R.id.ok_menu_item)).perform(click());
    }

    private void assertNoHabitsPlaceholderNotDisplayed() {
        onView(withText(R.string.no_habits_placeholder)).check(matches(not(isDisplayed())));
    }

}
