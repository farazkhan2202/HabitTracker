package my.projects.habittracker.model.repository;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import my.projects.habittracker.LiveDataTestHelper;
import my.projects.habittracker.TestDataProvider;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class HabitRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private HabitsDatabase db;
    private HabitRepository repository;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, HabitsDatabase.class).allowMainThreadQueries().build();
        db.clearAllTables();
        repository = db.repository();
    }

    @After
    public void tearDown() {
        db.clearAllTables();
        db.close();
    }

    @Test
    public void returnsEmptyListOnNoHabits() throws InterruptedException {
        List<Habit> habits = LiveDataTestHelper.getValue(repository.getHabitList());

        assertNotNull(habits);
        assertEquals(0, habits.size());
    }

    private void insertTestHabit() {
        TestDataProvider.insertSimpleHabit(repository);
    }

    @Test
    public void returnsListWithOneEntryAfterOneInsert() throws InterruptedException {
        insertTestHabit();

        List<Habit> habits = LiveDataTestHelper.getValue(repository.getHabitList());

        assertEquals(1, habits.size());
    }

    @Test
    public void storedHabitEqualsInserted() throws InterruptedException {
        insertTestHabit();

        List<Habit> habits = LiveDataTestHelper.getValue(repository.getHabitList());
        Habit stored = habits.get(0);

        assertEquals(TestDataProvider.simpleHabit, stored);
    }

    @Test
    public void returnsEmptyListAfterDeletingOneStoredHabit() throws InterruptedException {
        insertTestHabit();

        repository.delete(TestDataProvider.simpleHabit);
        List<Habit> habits = LiveDataTestHelper.getValue(repository.getHabitList());

        assertEquals(0, habits.size());
    }

    @Test
    public void retrievesAllStoredHabits() throws InterruptedException {
        CountDownLatch latch = TestDataProvider.populateRepository(repository, AppExecutors.diskIO);
        latch.await();

        List<Habit> habits = LiveDataTestHelper.getValue(repository.getHabitList());

        assertEquals(2, habits.size());
    }

    @Test
    public void returnsEmptyChangeListForHabitWithNoChanges() throws InterruptedException {
        CountDownLatch latch = TestDataProvider.populateRepository(repository, AppExecutors.diskIO);
        latch.await();

        List<HabitValueChange> changes = LiveDataTestHelper.getValue(repository.getChanges(TestDataProvider.simpleHabit.getId()));

        assertNotNull(changes);
        assertEquals(0, changes.size());
    }

    @Test
    public void retrievesOrderedStoredChanges() throws InterruptedException {
        CountDownLatch latch = TestDataProvider.populateRepository(repository, AppExecutors.diskIO);
        latch.await();

        List<HabitValueChange> changes = LiveDataTestHelper.getValue(repository.getChanges(TestDataProvider.habitWithChanges.getId()));

        assertEquals(2, changes.size());
        HabitValueChange firstChange = changes.get(1);
        assertEquals(TestDataProvider.changes.get(1), firstChange);
        HabitValueChange secondChange = changes.get(0);
        assertEquals(TestDataProvider.changes.get(0), secondChange);
    }

    @Test
    public void getByIdReturnsCorrectHabit() throws InterruptedException {
        CountDownLatch latch = TestDataProvider.populateRepository(repository, AppExecutors.diskIO);
        latch.await();

        Habit stored = LiveDataTestHelper.getValue(repository.getHabitById(TestDataProvider.simpleHabit.getId()));

        assertEquals(TestDataProvider.simpleHabit, stored);
    }

}
