package my.projects.habittracker.model.repository;

import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitDetails;
import my.projects.habittracker.model.data.habit.HabitValueChange;

@Dao
public abstract class HabitRepository {
    @Update
    public abstract void update(Habit habit);

    @Insert
    public abstract void insert(Habit habit);

    @Delete
    public abstract void delete(Habit habit);

    @Query("SELECT * FROM changes WHERE habit_id = :habitId ORDER BY changes.time DESC")
    public abstract LiveData<List<HabitValueChange>> getChanges(int habitId);

    @Query("SELECT * FROM habits")
    public abstract LiveData<List<Habit>> getHabitList();

    @Query("SELECT * FROM habits WHERE id = :habitId")
    public abstract LiveData<Habit> getHabitById(int habitId);

    @Query("SELECT * FROM habits WHERE id = :habitId")
    public abstract LiveData<HabitDetails> getHabitDetails(int habitId);

    @Query("SELECT (:habitTitle IN (SELECT title FROM habits WHERE id <> :baseHabitId))")
    public abstract LiveData<Boolean> doesHabitTitleAlreadyExist(String habitTitle, int baseHabitId);

    @Transaction
    public void insertChangeAndUpdateHabit(HabitValueChange change) {
        insertChange(change);
        long currentTime = new Date().getTime();
        updateHabitWithTime((int) change.getHabitId(), currentTime);
    }

    @Insert
    public abstract void insertChange(HabitValueChange change);

    @Query("UPDATE habits SET last_updated=:time WHERE id=:habitId")
    public abstract void updateHabitWithTime(int habitId, long time);

    @Delete
    public abstract void deleteChange(HabitValueChange change);
}
