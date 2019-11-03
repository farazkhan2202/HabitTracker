package my.projects.habittracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.HabitValueChange;

public class HabitChangesAdapter extends RecyclerView.Adapter<HabitChangeViewHolder> {

    public interface HabitChangesAdapterListener {
        void onDeleteHabitChangeRequested(HabitValueChange change);
    }

    private List<HabitValueChange> changes;

    private HabitChangesAdapterListener listener;

    public HabitChangesAdapter(List<HabitValueChange> changes) {
        this.changes = changes;
    }

    public void setListener(HabitChangesAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_change, parent, false);

        HabitChangeViewHolder viewHolder = new HabitChangeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HabitChangeViewHolder holder, int position) {
        final HabitValueChange change = changes.get(position);
        holder.bind(change);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteHabitChangeRequested(change);
            }
        });
    }

    @Override
    public int getItemCount() {
        return changes.size();
    }
}
