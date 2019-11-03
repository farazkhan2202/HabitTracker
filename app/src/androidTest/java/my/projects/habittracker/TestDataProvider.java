package my.projects.habittracker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.UnitSet;
import my.projects.habittracker.model.repository.HabitRepository;

public class TestDataProvider {

    public static Habit simpleHabit;
    public static Habit habitWithChanges;
    public static List<HabitValueChange> changes;

    static {
        simpleHabit = new Habit(1, "simple habit", "this is a simple habit", UnitSet.ABSTRACT, 1541142113402L);
        habitWithChanges = new Habit(2, "habit with changes", "this is a habit with changes list", UnitSet.TIME, 1541142113402L);

        changes = new ArrayList<>();

        HabitValueChange firstChange = new HabitValueChange();
        firstChange.setId(1L);
        firstChange.setHabitId(habitWithChanges.getId());
        firstChange.setTimeInMillis(1548162693402L);
        firstChange.setUnit(Unit.MINUTES);
        firstChange.setDelta(10);

        HabitValueChange secondChange = new HabitValueChange();
        secondChange.setId(2L);
        secondChange.setHabitId(habitWithChanges.getId());
        secondChange.setTimeInMillis(1541162113402L);
        secondChange.setUnit(Unit.MINUTES);
        secondChange.setDelta(5);

        changes.add(firstChange);
        changes.add(secondChange);
    }

    public static CountDownLatch populateRepository(final HabitRepository repository, Executor executor) {
        final CountDownLatch latch = new CountDownLatch(1);
        executor.execute(new Runnable() {
            @Override
            public void run() { repository.insert(simpleHabit);
                repository.insert(habitWithChanges);
                repository.insertChange(changes.get(0));
                repository.insertChange(changes.get(1));
                latch.countDown();
            }
        });
        return latch;
    }

    public static void insertSimpleHabit(HabitRepository repository) {
        repository.insert(simpleHabit);
    }

}
