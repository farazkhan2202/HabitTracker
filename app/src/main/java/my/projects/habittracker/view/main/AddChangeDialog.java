package my.projects.habittracker.view.main;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.UnitSet;
import my.projects.habittracker.model.data.unit.convert.db.UnitTypesConverter;
import my.projects.habittracker.view.adapter.UnitSpinnerAdapter;
import my.projects.habittracker.viewmodel.main.AddChangeViewModel;

public class AddChangeDialog extends DialogFragment {

    public static final String HABIT_ID_ARGUMENT = "habit_id_argument";
    public static final String UNIT_SET_ID_ARGUMENT = "unit_set_id_argument";

    private AddChangeViewModel viewModel;

    private EditText valueEditText;
    private Spinner unitsSpinner;
    private Button confirmButton;
    private Button cancelButton;

    public static AddChangeDialog newInstance(Habit habit) {
        Bundle args = new Bundle();
        args.putInt(HABIT_ID_ARGUMENT, habit.getId());
        args.putInt(UNIT_SET_ID_ARGUMENT, habit.getUnitSet().getId());
        AddChangeDialog dialog = new AddChangeDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AddChangeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_change, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.add_progress);
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkThatHabitIdWasPassed();
        bindUIVariables();
        configureUi();
    }

    private void checkThatHabitIdWasPassed() {
        if (getArguments() == null || getArguments().getInt(HABIT_ID_ARGUMENT, 0) == 0) {
            throw new IllegalStateException("Use static newInstance(int) to create the AddChangeDialog");
        }
    }

    private void bindUIVariables() {
        View view = getView();
        valueEditText = view.findViewById(R.id.add_change_value_edit_text);
        unitsSpinner = view.findViewById(R.id.add_change_unit_spinner);
        confirmButton = view.findViewById(R.id.confirm_button);
        cancelButton = view.findViewById(R.id.cancel_button);
    }

    private void configureUi() {
        configureConfirmButton();
        configureCancelButton();
        configureUnitsSpinner();
    }

    private void configureConfirmButton() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueTextIsValid()) {
                    HabitValueChange change = buildHabitValueChangeFromUI();
                    viewModel.addChange(change);
                    signalChangeAdded();
                    dismiss();
                } else {
                    showValueTextError();
                }
            }
        });
    }

    private boolean valueTextIsValid() {
        try {
            String valueText = valueEditText.getText().toString();
            Double value = Double.parseDouble(valueText);
            if (value <= 0) {
                return false;
            }
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    private HabitValueChange buildHabitValueChangeFromUI() {
        HabitValueChange change = new HabitValueChange();

        int habitId = getArguments().getInt(HABIT_ID_ARGUMENT);
        change.setHabitId(habitId);


        change.setDelta(Double.parseDouble(valueEditText.getText().toString()));

        long currentTimeInMillis = System.currentTimeMillis();
        change.setTimeInMillis(currentTimeInMillis);

        Unit selectedUnit = (Unit) unitsSpinner.getSelectedItem();
        change.setUnit(selectedUnit);

        return change;
    }

    private void signalChangeAdded() {
        Toast.makeText(getActivity(), R.string.change_added, Toast.LENGTH_SHORT).show();
    }

    private void showValueTextError() {
        String error = getString(R.string.invalid_value_error);
        valueEditText.setError(error);
        valueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                valueEditText.setError(null);
                valueEditText.removeTextChangedListener(this);
            }
        });
    }

    public void configureCancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void configureUnitsSpinner() {
        int unitSetId = getArguments().getInt(UNIT_SET_ID_ARGUMENT);
        UnitSet unitSet = UnitTypesConverter.toUnitSetFromId(unitSetId);
        populateUnitsSpinner(unitSet.getUnits());
    }

    private void populateUnitsSpinner(List<Unit> units) {
        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(getActivity(), units);
        unitsSpinner.setAdapter(adapter);
    }
}
