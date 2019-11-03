package my.projects.habittracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.Habit;

public class HabitsAdapter extends RecyclerView.Adapter<HabitViewHolder> {

    public interface HabitAdapterListener {
        void onHabitClicked(long habitId);
        void onAddChangeForHabitClicked(Habit habit);
    }

    private List<Habit> habits;

    private HabitAdapterListener listener;

    public HabitsAdapter(List<Habit> habits) {
        this.habits = habits;
    }

    public void setListener(HabitAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.habit_list_item, parent, false);

        HabitViewHolder viewHolder = new HabitViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        final Habit habit = habits.get(position);

        holder.bind(habit);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onHabitClicked(habit.getId());
            }
        });
        holder.addChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddChangeForHabitClicked(habit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}
