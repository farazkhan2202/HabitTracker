package my.projects.habittracker.viewmodel.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;

public class AddChangeViewModel extends AndroidViewModel {

    private HabitRepository habitRepository;

    public AddChangeViewModel(@NonNull Application application) {
        super(application);
        HabitsDatabase db = HabitsDatabase.getInstance(application.getApplicationContext());
        habitRepository = db.repository();
    }

    public void addChange(final HabitValueChange change) {
        AppExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                habitRepository.insertChangeAndUpdateHabit(change);
            }
        });
    }
}
