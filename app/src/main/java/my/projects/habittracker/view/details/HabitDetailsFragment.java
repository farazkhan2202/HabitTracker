package my.projects.habittracker.view.details;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitDetails;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.UnitSet;
import my.projects.habittracker.view.util.HabitInputValidationObserver;
import my.projects.habittracker.view.util.HabitInputValidationResponse;
import my.projects.habittracker.viewmodel.details.HabitDetailsViewModel;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HabitDetailsFragment extends Fragment {

    public interface HabitDetailsFragmentListener {
        void onSignalCloseDetailsFragment();
        void onSignalOpenHabitHistoryFragment(int habitId);
    }

    private HabitDetailsFragmentListener listener;

    private static final String EDIT_MODE_KEY = "edit_mode";
    private static final String HABIT_ID_ARGUMENT_KEY = "habit_extra";

    private EditText titleEditText;
    private EditText descEditText;

    private boolean editModeEnabled;

    private HabitDetailsViewModel habitDetailsViewModel;
    private HabitDetails habitDetails;

    public static HabitDetailsFragment newInstance(int habitId) {
        Bundle args = new Bundle();
        args.putInt(HABIT_ID_ARGUMENT_KEY, habitId);

        HabitDetailsFragment fragment = new HabitDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (HabitDetailsFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            editModeEnabled = savedInstanceState.getBoolean(EDIT_MODE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EDIT_MODE_KEY, editModeEnabled);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_habit_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindUIVariables();
        configureInitialEditTextStates();
        requestDetails();
    }

    private void bindUIVariables() {
        titleEditText = getView().findViewById(R.id.details_habit_title);
        descEditText = getView().findViewById(R.id.details_habit_description);
    }

    private void configureInitialEditTextStates() {
        if (!editModeEnabled) {
            disableEditTextForInput(titleEditText);
            disableEditTextForInput(descEditText);
        }
    }

    private void requestDetails() {
        Bundle args = getArguments();
        int passedHabitId = args.getInt(HABIT_ID_ARGUMENT_KEY);

        habitDetailsViewModel = ViewModelProviders.of(this).get(HabitDetailsViewModel.class);
        habitDetailsViewModel.getDetails(passedHabitId).observe(this, new Observer<HabitDetails>() {
            @Override
            public void onChanged(@Nullable HabitDetails habitDetails) {
                HabitDetailsFragment.this.habitDetails = habitDetails;
                showDetails();
                showUnitsInfo();
            }
        });
    }

    private void showDetails() {
        Habit habit = habitDetails.getHabit();
        titleEditText.setText(habit.getTitle());
        descEditText.setText(habit.getDescription());
    }

    private void showUnitsInfo() {
        UnitSet unitSet = habitDetails.getHabit().getUnitSet();

        TextView unitSetTextView = getView().findViewById(R.id.details_unit_set_text_view);
        unitSetTextView.setText(unitSet.getSetName());

        TextView unitsTextView = getView().findViewById(R.id.details_units_text_view);
        unitsTextView.setText(unitSet.getUnitsExamplesString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int menu_res_id;
        if (editModeEnabled) {
            menu_res_id = R.menu.menu_details_edit;
        } else {
            menu_res_id = R.menu.menu_details;
        }
        inflater.inflate(menu_res_id, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_history_menu_item:
                signalOpenHabitHistoryFragment();
                break;
            case R.id.details_delete_menu_item:
                habitDetailsViewModel.deleteHabit(habitDetails.getHabit());
                signalCloseFragment();
                break;
            case R.id.details_edit_menu_item:
                enableEditMode();
                reconfigureMenu();
                break;
            case R.id.details_edit_ok_menu_item:
                final Habit habit = buildHabitFromUI();
                LiveData<HabitInputValidationResponse> validationResponseLiveData = habitDetailsViewModel.validateHabitInput(habit);
                validationResponseLiveData.observe(this, new HabitInputValidationObserver() {
                    @Override
                    public void onOK() {
                        habitDetailsViewModel.updateHabit(habit);
                        disableEditMode();
                        reconfigureMenu();
                    }

                    @Override
                    public void onEmptyTitle() {
                        showEmptyTitleError();
                    }

                    @Override
                    public void onTitleAlreadyExists() {
                        showTitleAlreadyExistsError();
                    }
                });
                break;
            case R.id.details_edit_cancel_menu_item:
                revertUIChanges();
                disableEditMode();
                reconfigureMenu();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void signalOpenHabitHistoryFragment() {
        listener.onSignalOpenHabitHistoryFragment(habitDetails.getHabit().getId());
    }

    private void signalCloseFragment() {
        listener.onSignalCloseDetailsFragment();
    }

    private void enableEditMode() {
        editModeEnabled = true;

        enableEditTextForInput(titleEditText);
        enableEditTextForInput(descEditText);
    }

    private void enableEditTextForInput(EditText editText) {
        editText.setEnabled(true);
        editText.setFocusableInTouchMode(true);
    }

    private void reconfigureMenu() {
        getActivity().invalidateOptionsMenu();
    }

    private Habit buildHabitFromUI() {
        Habit originalHabit = habitDetails.getHabit();

        String title = titleEditText.getText().toString();
        String description = descEditText.getText().toString();

        Habit changedHabit = new Habit(originalHabit.getId(), title, description, originalHabit.getUnitSet(), originalHabit.getLastUpdatedMillis());
        return changedHabit;
    }

    private void disableEditMode() {
        editModeEnabled = false;

        disableEditTextForInput(titleEditText);
        disableEditTextForInput(descEditText);
    }

    private void disableEditTextForInput(EditText editText) {
        closeKeyboard();
        editText.setEnabled(false);
        editText.setFocusableInTouchMode(false);
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        View focused = getActivity().getCurrentFocus();
        if (focused != null) {
            imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
            focused.clearFocus();
        }
    }

    private void showEmptyTitleError() {
        String emptyTitleError = getString(R.string.empty_title_error);
        titleEditText.setError(emptyTitleError);
    }

    private void showTitleAlreadyExistsError() {
        String titleExistsError = getString(R.string.title_already_exists_error);
        titleEditText.setError(titleExistsError);
    }

    private void revertUIChanges() {
        revertTitleChanges();
        revertDescriptionChanges();
    }

    private void revertTitleChanges() {
        titleEditText.setText(habitDetails.getHabit().getTitle());
    }

    private void revertDescriptionChanges() {
        descEditText.setText(habitDetails.getHabit().getDescription());
    }

}
