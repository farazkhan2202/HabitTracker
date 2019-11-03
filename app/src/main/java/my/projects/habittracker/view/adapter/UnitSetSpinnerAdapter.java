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
import my.projects.habittracker.model.data.unit.UnitSet;

public class UnitSetSpinnerAdapter extends ArrayAdapter<UnitSet> {

    private List<UnitSet> unitSets;

    public UnitSetSpinnerAdapter(@NonNull Context context, @NonNull List<UnitSet> objects) {
        super(context, R.layout.item_unit_set, objects);
        this.unitSets = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent, true);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent, false);
    }

    private View createItemView(int position, ViewGroup parent, boolean dropdown) {
        UnitSet unitSet = unitSets.get(position);
        int targetLayout = dropdown ? R.layout.item_unit_set_dropdown : R.layout.item_unit_set;
        View view = LayoutInflater.from(parent.getContext()).inflate(targetLayout, parent, false);

        TextView unitSetTitleTextView = view.findViewById(R.id.item_unit_set_title);
        unitSetTitleTextView.setText(unitSet.getSetName());

        return view;
    }
}
