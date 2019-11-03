package my.projects.habittracker.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.unit.Unit;

public class UnitSpinnerAdapter extends ArrayAdapter<Unit> {

    private List<Unit> units;

    public UnitSpinnerAdapter(Context context, List<Unit> units) {
        super(context, R.layout.item_unit, units);
        this.units = units;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(units.get(position), parent, false);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(units.get(position), parent, true);
    }

    private View createView(Unit unit, ViewGroup parent, boolean dropdown) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        int targetLayout = dropdown ? R.layout.item_unit_dropdown : R.layout.item_unit;
        View unitItemView = inflater.inflate(targetLayout, parent, false);

        TextView unitText = unitItemView.findViewById(R.id.item_unit_title);
        unitText.setText(unit.getUnitName());

        return unitItemView;
    }
}
