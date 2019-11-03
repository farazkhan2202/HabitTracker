package my.projects.habittracker.viewmodel.details;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.data.habit.HabitDetails;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;
import my.projects.habittracker.view.util.HabitInputValidationResponse;
import my.projects.habittracker.viewmodel.util.HabitInputValidator;

public class HabitDetailsViewModel extends AndroidViewModel {

    private HabitRepository habitRepository;

    public HabitDetailsViewModel(@NonNull Application application) {
        super(application);
        HabitsDatabase db = HabitsDatabase.getInstance(application);
        habitRepository = db.repository();
    }

    public LiveData<HabitDetails> getDetails(int habitId) {
        return habitRepository.getHabitDetails(habitId);
    }

    public void updateHabit(final Habit habit) {
        AppExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                habitRepository.update(habit);
            }
        });
    }

    public LiveData<HabitInputValidationResponse> validateHabitInput(Habit habit) {
        return HabitInputValidator.validateHabitInput(habitRepository, habit);
    }

    public void deleteHabit(final Habit habit) {
        AppExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                habitRepository.delete(habit);
            }
        });
    }

}
