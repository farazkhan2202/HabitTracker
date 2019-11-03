package my.projects.habittracker.model.repository.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.repository.HabitRepository;

@Database(entities = {Habit.class, HabitValueChange.class}, version = 1)
public abstract class HabitsDatabase extends RoomDatabase {
    public static final String DB_NAME = "habits_db";

    public abstract HabitRepository repository();

    private static HabitsDatabase instance;

    public static synchronized HabitsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), HabitsDatabase.class, DB_NAME)
                    .build();
        }
        return instance;
    }

    public static synchronized void invalidateInstance() {
        instance = null;
    }
}
