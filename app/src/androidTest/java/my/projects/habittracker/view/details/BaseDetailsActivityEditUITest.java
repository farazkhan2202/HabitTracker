package my.projects.habittracker.view.details;

import android.text.InputType;

import org.junit.Before;

import androidx.test.espresso.matcher.ViewMatchers;
import my.projects.habittracker.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static org.hamcrest.CoreMatchers.not;

public class BaseDetailsActivityEditUITest extends BaseDetailsActivityTest {

    @Before
    public void clickOnEditMenuItem() {
        onView(withId(R.id.details_edit_menu_item)).perform(click());
    }

    protected void assertViewInputTypeIsNull(int id) {
        onView(withId(id)).check(matches(withInputType(InputType.TYPE_NULL)));
    }

    protected void assertViewInputTypeNotNull(int id) {
        onView(withId(id)).check(matches(not(withInputType(InputType.TYPE_NULL))));
    }

}
