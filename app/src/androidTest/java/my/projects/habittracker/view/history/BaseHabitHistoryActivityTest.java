package my.projects.habittracker.view.history;

import org.junit.Before;

import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;
import my.projects.habittracker.view.main.BaseMainActivityTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class BaseHabitHistoryActivityTest extends BaseMainActivityTest {

    @Before
    public void navigateToHistoryActivity() {
        onView(withText(TestDataProvider.habitWithChanges.getTitle())).perform(click());
        onView(withId(R.id.details_history_menu_item)).perform(click());
    }

}
