package my.projects.habittracker.model.data.habit;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class HabitDetails {

    @Embedded
    private Habit habit;

    @Relation(entity = HabitValueChange.class, parentColumn = "id", entityColumn = "habit_id")
    private List<HabitValueChange> habitChanges;

    public HabitDetails(Habit habit, List<HabitValueChange> habitChanges) {
        this.habit = habit;
        this.habitChanges = habitChanges;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public List<HabitValueChange> getHabitChanges() {
        return habitChanges;
    }

    public void setHabitChanges(List<HabitValueChange> habitChanges) {
        this.habitChanges = habitChanges;
    }
}
