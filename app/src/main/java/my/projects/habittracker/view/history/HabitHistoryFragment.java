package my.projects.habittracker.view.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.HabitValueChange;
import my.projects.habittracker.model.data.unit.Unit;
import my.projects.habittracker.model.data.unit.UnitSet;
import my.projects.habittracker.view.adapter.HabitChangesAdapter;
import my.projects.habittracker.view.adapter.UnitSetSpinnerAdapter;
import my.projects.habittracker.view.adapter.UnitSpinnerAdapter;
import my.projects.habittracker.viewmodel.history.HabitHistoryViewModel;

public class HabitHistoryFragment extends Fragment {

    public static final String HABIT_ID_ARGUMENT_KEY = "habit_id";

    private HabitHistoryViewModel viewModel;

    private RecyclerView changesRecyclerView;
    private Spinner unitsSpinner;

    private List<HabitValueChange> changes;

    public static HabitHistoryFragment newInstance(int habitId) {
        Bundle args = new Bundle();
        args.putInt(HABIT_ID_ARGUMENT_KEY, habitId);

        HabitHistoryFragment fragment = new HabitHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_habit_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changesRecyclerView = view.findViewById(R.id.changes);
        changesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        unitsSpinner = view.findViewById(R.id.units_spinner);

        configureUnitsSpinner();
        requestChanges();
    }

    private void configureUnitsSpinner() {
        unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Unit baseUnit = (Unit) parent.getItemAtPosition(position);
                convertValuesToBaseUnit(changes, baseUnit);
                showChangesDetails(); // update history with new base unit
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void requestChanges() {
        Bundle args = getArguments();
        int habitId = args.getInt(HABIT_ID_ARGUMENT_KEY);

        viewModel = ViewModelProviders.of(this).get(HabitHistoryViewModel.class);
        viewModel.getChanges(habitId).observe(this, new Observer<List<HabitValueChange>>() {
            @Override
            public void onChanged(List<HabitValueChange> changes) {
                HabitHistoryFragment.this.changes = changes;
                if (changes.size() == 0) {
                    showNoChangesPlaceholder();
                    clearChangeDetails();
                } else {
                    hideNoChangesPlaceholder();
                    populateUnitsSpinner();

                    Unit baseUnit = (Unit) unitsSpinner.getSelectedItem();
                    convertValuesToBaseUnit(changes, baseUnit);

                    showChangesDetails();
                }
            }
        });
    }

    private void showNoChangesPlaceholder() {
        TextView noChangesPlaceholder = getView().findViewById(R.id.no_changes_placeholder);
        noChangesPlaceholder.setVisibility(View.VISIBLE);
    }

    private void clearChangeDetails() {
        clearPlot();
        clearChangeHistory();
        clearTotal();
    }

    private void clearPlot() {
        BarChart barChart = getView().findViewById(R.id.changes_graph);
        barChart.setData(null);
        barChart.invalidate();
    }

    private void clearChangeHistory() {
        changesRecyclerView.setAdapter(null);
    }

    private void clearTotal() {
        TextView changesTotalTextView = getView().findViewById(R.id.changes_total_text);
        changesTotalTextView.setText("");
    }

    private void hideNoChangesPlaceholder() {
        TextView noChangesPlaceholder = getView().findViewById(R.id.no_changes_placeholder);
        noChangesPlaceholder.setVisibility(View.GONE);
    }

    private void populateUnitsSpinner() {
        if (changes.size() == 0) {
            return;
        }
        HabitValueChange change = changes.get(0);
        UnitSet unitSet = change.getUnit().getUnitSet();

        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(getContext(), unitSet.getUnits());
        unitsSpinner.setAdapter(adapter);
    }

    private void convertValuesToBaseUnit(List<HabitValueChange> changes, Unit base) {
        for (HabitValueChange change : changes) {
            double value = change.getDelta();
            double convertedValue = change.getUnit().convertValueTo(value, base);
            change.setDelta(convertedValue);
            change.setUnit(base);
        }
    }

    private void showChangesDetails() {
        showChangeHistory();
        showChangePlot();
        showTotal();
    }

    private void showChangeHistory() {
        HabitChangesAdapter adapter = new HabitChangesAdapter(changes);
        adapter.setListener(new HabitChangesAdapter.HabitChangesAdapterListener() {
            @Override
            public void onDeleteHabitChangeRequested(HabitValueChange change) {
                viewModel.deleteHabitChange(change);
            }
        });
        changesRecyclerView.setAdapter(adapter);
    }

    private void showChangePlot() {
        Map<Date, Double> dayToValueMap = changesToDayToValueMap();
        Map<Date, Double> sortedDayToValueMap = sortDayToValueMap(dayToValueMap);

        Unit baseUnit = (Unit) unitsSpinner.getSelectedItem();
        showPlot(sortedDayToValueMap, baseUnit.getUnitName());
    }

    private Map<Date, Double> changesToDayToValueMap() {
        Map<Date, Double> dayToValueMap = new HashMap<>();
        for (HabitValueChange change : changes) {
            Date day = timeInMillisToDay(change.getTimeInMillis());
            if (dayToValueMap.containsKey(day)) {
                Double storedValue = dayToValueMap.get(day);
                dayToValueMap.put(day, storedValue + change.getDelta());
            } else {
                dayToValueMap.put(day, change.getDelta());
            }
        }
        return dayToValueMap;
    }

    private Date timeInMillisToDay(long timeInMillis) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Map<Date, Double> sortDayToValueMap(Map<Date, Double> dayToValueMap) {
        return new TreeMap<>(dayToValueMap);
    }

    private void showPlot(Map<Date, Double> sortedDayToValueMap, String unitName) {
        BarChart barChart = getView().findViewById(R.id.changes_graph);
        barChart.setData(null);

        List<BarEntry> barEntries = new ArrayList<>();
        int counter = 0;
        for (Double value : sortedDayToValueMap.values()) {
            BarEntry barEntry = new BarEntry(counter++, value.floatValue());
            barEntries.add(barEntry);
        }

        final Date[] dates = sortedDayToValueMap.keySet().toArray(new Date[] {});
        IAxisValueFormatter xAxisValueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (index < dates.length) {
                    DateFormat formatter = DateFormat.getDateInstance();
                    return formatter.format(dates[index]);
                } else {
                    return String.valueOf(value);
                }
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(xAxisValueFormatter);
        xAxis.setGranularity(1);

        BarDataSet barDataSet = new BarDataSet(barEntries, unitName);
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(7);

        barChart.setData(barData);
        barChart.invalidate();
    }

    private void showTotal() {
        double sum = 0;
        for (HabitValueChange change : changes) {
            sum += change.getDelta();
        }
        TextView changesTotalTextView = getView().findViewById(R.id.changes_total_text);
        Unit baseUnit = (Unit) unitsSpinner.getSelectedItem();
        String text = getString(R.string.change_total, sum, baseUnit.getUnitName());
        changesTotalTextView.setText(text);
    }
}
