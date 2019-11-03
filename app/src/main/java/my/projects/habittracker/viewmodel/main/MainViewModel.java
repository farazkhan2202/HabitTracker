package my.projects.habittracker.viewmodel.main;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;

public class MainViewModel extends AndroidViewModel {

    private HabitRepository habitRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        HabitsDatabase db = HabitsDatabase.getInstance(application);
        habitRepository = db.repository();
    }

    public LiveData<List<Habit>> getHabits() {
        return habitRepository.getHabitList();
    }
}
