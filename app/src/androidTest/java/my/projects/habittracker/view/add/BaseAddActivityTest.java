package my.projects.habittracker.view.add;

import org.junit.Before;
import org.junit.Rule;

import androidx.test.rule.ActivityTestRule;
import my.projects.habittracker.R;
import my.projects.habittracker.view.main.BaseMainActivityTest;
import my.projects.habittracker.view.main.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class BaseAddActivityTest extends BaseMainActivityTest {

    @Before
    public void setUp() {
        onView(withId(R.id.add_menu_item)).perform(click());
    }
}
