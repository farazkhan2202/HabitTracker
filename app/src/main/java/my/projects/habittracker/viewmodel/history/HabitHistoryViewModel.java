package my.projects.habittracker.viewmodel.history;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;

public class HabitHistoryViewModel extends AndroidViewModel {

    private HabitRepository habitRepository;

    public HabitHistoryViewModel(@NonNull Application application) {
        super(application);
        HabitsDatabase db = HabitsDatabase.getInstance(application.getApplicationContext());
        habitRepository = db.repository();
    }

    public LiveData<List<HabitValueChange>> getChanges(int habitId) {
        return habitRepository.getChanges(habitId);
    }

    public void deleteHabitChange(final HabitValueChange change) {
        AppExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                habitRepository.deleteChange(change);
            }
        });
    }
}
