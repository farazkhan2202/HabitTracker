package my.projects.habittracker.viewmodel.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.view.util.HabitInputValidationResponse;

public class HabitInputValidator {

    public static LiveData<HabitInputValidationResponse> validateHabitInput(HabitRepository repository, Habit habit) {
        final MutableLiveData<HabitInputValidationResponse> validationResponseMutableLiveData = new MutableLiveData<>();
        if (habit.getTitle().isEmpty()) {
            validationResponseMutableLiveData.setValue(HabitInputValidationResponse.EMPTY_TITLE);
        } else {
            String title = habit.getTitle();
            final LiveData<Boolean> titleAlreadyExistsLiveData = repository.doesHabitTitleAlreadyExist(title, habit.getId());
            titleAlreadyExistsLiveData.observeForever(new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean titleExists) {
                    if(titleExists) {
                        validationResponseMutableLiveData.setValue(HabitInputValidationResponse.TITLE_ALREADY_EXISTS);
                    } else {
                        validationResponseMutableLiveData.setValue(HabitInputValidationResponse.OK);
                    }
                    titleAlreadyExistsLiveData.removeObserver(this);
                }
            });
        }
        return validationResponseMutableLiveData;
    }

}
