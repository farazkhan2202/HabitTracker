package my.projects.habittracker.viewmodel.add;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;
import my.projects.habittracker.view.util.HabitInputValidationResponse;
import my.projects.habittracker.viewmodel.util.HabitInputValidator;

public class AddHabitViewModel extends AndroidViewModel {

    private HabitRepository habitRepository;

    public AddHabitViewModel(@NonNull Application application) {
        super(application);
        HabitsDatabase db = HabitsDatabase.getInstance(application);
        habitRepository = db.repository();
    }

    public void addHabit(final Habit habit) {
        AppExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                habitRepository.insert(habit);
            }
        });
    }

    public LiveData<HabitInputValidationResponse> validateInput(Habit habit) {
        return HabitInputValidator.validateHabitInput(habitRepository, habit);
    }

}
