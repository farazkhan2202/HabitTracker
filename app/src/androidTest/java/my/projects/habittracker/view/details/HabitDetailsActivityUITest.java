package my.projects.habittracker.view.details;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import my.projects.habittracker.R;
import my.projects.habittracker.TestDataProvider;
import my.projects.habittracker.model.data.unit.Unit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(AndroidJUnit4.class)
public class HabitDetailsActivityUITest extends BaseDetailsActivityTest {

    @Test
    public void habitTitleIsDisplayed() {
        onView(withId(R.id.details_habit_title)).check(matches(withText(TestDataProvider.simpleHabit.getTitle())));
    }

    @Test
    public void habitDescriptionIsDisplayed() {
        onView(withId(R.id.details_habit_description)).check(matches(withText(TestDataProvider.simpleHabit.getDescription())));
    }

    @Test
    public void habitUnitSetAndUnitsAreDisplayed() {
        assertUnitSetDisplayed();
        assertUnitsDisplayed();
    }

    private void assertUnitSetDisplayed() {
        onView(withId(R.id.details_unit_set_text_view)).check(matches(withText(TestDataProvider.simpleHabit.getUnitSet().getSetName())));
    }

    private void assertUnitsDisplayed() {
        List<Unit> units = TestDataProvider.simpleHabit.getUnitSet().getUnits();
        for (Unit unit : units) {
            onView(withId(R.id.details_units_text_view)).check(matches(withText(containsString(unit.getUnitName()))));
        }
    }

    @Test
    public void deleteMenuItemIsDisplayed() {
        onView(withId(R.id.details_delete_menu_item)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnDeleteMenuItemClosesActivityAndDeletesHabit() {
        clickOnDeleteMenuItem();

        assertDetailsActivityIsNotDisplayed();
        assertSimpleHabitListItemIsNotDisplayed();
    }

    private void clickOnDeleteMenuItem() {
        onView(withId(R.id.details_delete_menu_item)).perform(click());
    }

    private void assertDetailsActivityIsNotDisplayed() {
        onView(withId(R.id.details_habit_title)).check(doesNotExist());
        onView(withId(R.id.details_habit_description)).check(doesNotExist());
    }

    private void assertSimpleHabitListItemIsNotDisplayed() {
        onView(withText(TestDataProvider.simpleHabit.getTitle())).check(doesNotExist());
    }

    @Test
    public void clickOnHistoryMenuItemOpensHistoryFragment() {
        clickOnHistoryMenuItem();

        assertHistoryFragmentOpened();
    }

    private void clickOnHistoryMenuItem() {
        onView(withId(R.id.details_history_menu_item)).perform(click());
    }

    private void assertHistoryFragmentOpened() {
        onView(withId(R.id.changes_graph)).check(matches(isDisplayed()));
    }
}
