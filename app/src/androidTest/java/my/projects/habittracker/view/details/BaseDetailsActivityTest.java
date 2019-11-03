package my.projects.habittracker.view.details;

import android.content.Intent;

import org.junit.Before;

import my.projects.habittracker.TestDataProvider;
import my.projects.habittracker.view.main.BaseMainActivityTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class BaseDetailsActivityTest extends BaseMainActivityTest {

    @Before
    public void openSimpleHabitDetails() {
        clickOnSimpleHabit();
    }

    private void clickOnSimpleHabit() {
        onView(withText(TestDataProvider.simpleHabit.getTitle())).perform(click());
    }

}
