package my.projects.habittracker.view.main;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import my.projects.habittracker.R;
import my.projects.habittracker.view.add.AddHabitFragment;
import my.projects.habittracker.view.broadcast.AlarmReceiver;
import my.projects.habittracker.view.details.HabitDetailsFragment;
import my.projects.habittracker.view.history.HabitHistoryFragment;

public class MainActivity extends AppCompatActivity implements
        AddHabitFragment.AddHabitFragmentListener,
        HabitDetailsFragment.HabitDetailsFragmentListener,
        HabitsFragment.HabitsFragmentListener {

    private static final String HABITS_FRAGMENT_TAG = "habits_fragment";
    private static final String HABIT_HISTORY_FRAGMENT_TAG = "habit_history_fragment";
    private static final String HABIT_DETAILS_FRAGMENT_TAG = "habit_details_fragment";
    private static final String ADD_HABIT_FRAGMENT_TAG = "add_habit_fragment";
    private static final String EMPTY_PLACEHOLDER_FRAGMENT_TAG = "empty_placeholder_fragment";

    private static final String ROOT_SECOND_FRAME_BACKSTACK_TAG = "root_second_frame";
    private static final String ROOT_MAIN_FRAME_BACKSTACK_TAG = "root_main_frame";

    private HabitHistoryFragment habitHistoryFragment;
    private HabitDetailsFragment habitDetailsFragment;
    private AddHabitFragment addHabitFragment;

    private boolean isTwoPaneLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locateFragments();
        determineLayout();

        if (habitsFragmentNotPresent()) {
            setupHabitsFragment();
        }
        if (isTwoPaneLayout && placeholderFragmentNotPresent()) {
            setupPlaceholderFragment();
        }

        if (savedInstanceState != null) {
            reorganizeFragmentHierarchy();
        }

        setupNotification();
    }

    private void locateFragments() {
        FragmentManager fm = getSupportFragmentManager();
        habitHistoryFragment = (HabitHistoryFragment) fm.findFragmentByTag(HABIT_HISTORY_FRAGMENT_TAG);
        habitDetailsFragment = (HabitDetailsFragment) fm.findFragmentByTag(HABIT_DETAILS_FRAGMENT_TAG);
        addHabitFragment = (AddHabitFragment) fm.findFragmentByTag(ADD_HABIT_FRAGMENT_TAG);
    }

    private void determineLayout() {
        isTwoPaneLayout = (findViewById(R.id.second_frame) != null);
    }

    private boolean habitsFragmentNotPresent() {
        return (getSupportFragmentManager().findFragmentByTag(HABITS_FRAGMENT_TAG) == null);
    }

    private void setupHabitsFragment() {
        HabitsFragment habitsFragment = new HabitsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, habitsFragment, HABITS_FRAGMENT_TAG).commit();
    }

    private boolean placeholderFragmentNotPresent() {
        return (getSupportFragmentManager().findFragmentByTag(EMPTY_PLACEHOLDER_FRAGMENT_TAG) == null);
    }

    private void setupPlaceholderFragment() {
        EmptyPlaceholderFragment emptyPlaceholderFragment = new EmptyPlaceholderFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.second_frame, emptyPlaceholderFragment, EMPTY_PLACEHOLDER_FRAGMENT_TAG).commit();
    }

    private void reorganizeFragmentHierarchy() {
        detachFragments();
        placeExistingFragments();
    }

    private void detachFragments() {
        removeFragmentsWithRoot(ROOT_MAIN_FRAME_BACKSTACK_TAG, false);
        removeFragmentsWithRoot(ROOT_SECOND_FRAME_BACKSTACK_TAG, false);
    }

    private void removeFragmentsWithRoot(String rootTag, boolean destructive) {
        getSupportFragmentManager().popBackStackImmediate(rootTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (destructive) {
            clearFragmentReferences();
        }
    }

    private void clearFragmentReferences() {
        addHabitFragment = null;
        habitDetailsFragment = null;
        habitHistoryFragment = null;
    }

    private void placeExistingFragments() {
        if (addHabitFragment != null) {
            placeFragment(addHabitFragment, ADD_HABIT_FRAGMENT_TAG, true);
        } else if (habitDetailsFragment != null) {
            placeFragment(habitDetailsFragment, HABIT_DETAILS_FRAGMENT_TAG, true);
            if (habitHistoryFragment != null) {
                placeFragment(habitHistoryFragment, HABIT_HISTORY_FRAGMENT_TAG, false);
            }
        }
    }

    private void placeFragment(Fragment fragment, String tag, boolean root) {
        int frameId = isTwoPaneLayout ? R.id.second_frame : R.id.main_frame;
        String rootTag = isTwoPaneLayout ? ROOT_SECOND_FRAME_BACKSTACK_TAG : ROOT_MAIN_FRAME_BACKSTACK_TAG;
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, tag)
                .addToBackStack(rootTag)
                .commit();
    }

    private void setupNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        PendingIntent pendingIntent = buildAlarmIntent();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private PendingIntent buildAlarmIntent() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onSignalCloseAddFragment() {
        removeLastAddedFragment();
        addHabitFragment = null;
    }

    @Override
    public void onSignalCloseDetailsFragment() {
        removeLastAddedFragment();
        habitDetailsFragment = null;
    }

    private void removeLastAddedFragment() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onSignalOpenHabitHistoryFragment(int habitId) {
        habitHistoryFragment = HabitHistoryFragment.newInstance(habitId);
        placeFragment(habitHistoryFragment, HABIT_HISTORY_FRAGMENT_TAG, false);
    }

    @Override
    public void onSignalOpenHabitDetailsFragment(int habitId) {
        removeCurrentlyOpenedFragments();

        habitDetailsFragment = HabitDetailsFragment.newInstance(habitId);
        placeFragment(habitDetailsFragment, HABIT_DETAILS_FRAGMENT_TAG, true);
    }

    @Override
    public void onSignalOpenAddHabitFragment() {
        removeCurrentlyOpenedFragments();

        addHabitFragment = new AddHabitFragment();
        placeFragment(addHabitFragment, ADD_HABIT_FRAGMENT_TAG, true);
    }

    private void removeCurrentlyOpenedFragments() {
        String rootTag = isTwoPaneLayout ? ROOT_SECOND_FRAME_BACKSTACK_TAG : ROOT_MAIN_FRAME_BACKSTACK_TAG;
        removeFragmentsWithRoot(rootTag, true);
    }

    public boolean hasAddHabitFragmentOpenInSecondFrame() {
        return addHabitFragment != null && addHabitFragment.getId() == R.id.second_frame;
    }
}
