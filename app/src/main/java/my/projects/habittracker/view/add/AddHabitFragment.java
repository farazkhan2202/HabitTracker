package my.projects.habittracker.view.add;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.UnitSet;
import my.projects.habittracker.view.adapter.UnitSetSpinnerAdapter;
import my.projects.habittracker.view.util.HabitInputValidationObserver;
import my.projects.habittracker.view.util.HabitInputValidationResponse;
import my.projects.habittracker.viewmodel.add.AddHabitViewModel;

public class AddHabitFragment extends Fragment {

    public interface AddHabitFragmentListener {
        void onSignalCloseAddFragment();
    }

    private AddHabitFragmentListener listener;

    private AddHabitViewModel addHabitViewModel;

    private EditText titleEditText;
    private EditText descEditText;

    private Spinner unitSetSpinner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddHabitFragmentListener) context;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_habit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindUIVariables();
        populateUnitSetSpinner();
        configureUnitSetSpinner();

        addHabitViewModel = ViewModelProviders.of(this).get(AddHabitViewModel.class);
    }

    private void bindUIVariables() {
        titleEditText = getView().findViewById(R.id.add_title_edit_text);
        descEditText = getView().findViewById(R.id.add_desc_edit_text);
        unitSetSpinner = getView().findViewById(R.id.add_unit_set_spinner);
    }

    private void populateUnitSetSpinner() {
        UnitSetSpinnerAdapter adapter = new UnitSetSpinnerAdapter(getContext(), UnitSet.getAll());
        unitSetSpinner.setAdapter(adapter);
    }

    private void configureUnitSetSpinner() {
        final TextView examplesTextView = getView().findViewById(R.id.add_unit_set_examples_text);
        unitSetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UnitSet selectedUnitSet = (UnitSet) parent.getItemAtPosition(position);
                examplesTextView.setText(selectedUnitSet.getUnitsExamplesString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ok_menu_item:
                final Habit createdHabit = createHabitFromUI();
                LiveData<HabitInputValidationResponse> responseLiveData = addHabitViewModel.validateInput(createdHabit);
                responseLiveData.observe(this, new HabitInputValidationObserver() {
                    @Override
                    public void onOK() {
                        addHabitViewModel.addHabit(createdHabit);
                        signalCloseAddHabitFragment();
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
                default:
                    return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private Habit createHabitFromUI() {
        String title = titleEditText.getText().toString();
        String desc = descEditText.getText().toString();
        UnitSet selectedUnitSet = (UnitSet) unitSetSpinner.getSelectedItem();

        Habit habit = new Habit(title, desc, selectedUnitSet);
        return habit;
    }

    private void signalCloseAddHabitFragment() {
        listener.onSignalCloseAddFragment();
    }

    private void showEmptyTitleError() {
        titleEditText.setError(getString(R.string.empty_title_error));
        configureResetErrorOnInput();
    }

    private void showTitleAlreadyExistsError() {
        titleEditText.setError(getString(R.string.title_already_exists_error));
        configureResetErrorOnInput();
    }

    private void configureResetErrorOnInput() {
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                titleEditText.setError(null);
                titleEditText.removeTextChangedListener(this);
            }
        });
    }

}
