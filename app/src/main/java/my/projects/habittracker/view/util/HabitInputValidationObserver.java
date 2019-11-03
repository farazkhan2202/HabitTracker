package my.projects.habittracker.view.util;

import androidx.lifecycle.Observer;

public abstract class HabitInputValidationObserver implements Observer<HabitInputValidationResponse> {

    @Override
    public void onChanged(HabitInputValidationResponse habitInputValidationResponse) {
        switch (habitInputValidationResponse) {
            case OK:
                onOK();
                break;
            case EMPTY_TITLE:
                onEmptyTitle();
                break;
            case TITLE_ALREADY_EXISTS:
                onTitleAlreadyExists();
                break;
            default:
                throw new IllegalArgumentException("Unknown HabitInputValidationResponse");
        }
    }

    public abstract void onOK();
    public abstract void onEmptyTitle();
    public abstract void onTitleAlreadyExists();
}
