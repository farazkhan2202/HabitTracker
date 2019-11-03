package my.projects.habittracker.view.history;

import org.junit.Test;

import my.projects.habittracker.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class HabitHistoryActivityNoChangesUITest extends BaseHabitHistoryActivityNoChangesTest {

    @Test
    public void noChangesPlaceholderIsDisplayed() {
        onView(withText(R.string.no_changes_placeholder)).check(matches(isDisplayed()));
    }

}
