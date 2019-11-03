package my.projects.habittracker.view.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.HabitValueChange;

public class HabitChangeViewHolder extends RecyclerView.ViewHolder {
    TextView changeText;
    ImageButton deleteButton;

    public HabitChangeViewHolder(@NonNull View itemView) {
        super(itemView);
        changeText = itemView.findViewById(R.id.change_item_text);
        deleteButton = itemView.findViewById(R.id.change_item_delete_button);
    }

    public void bind(HabitValueChange change) {
        changeText.setText(change.toString());
    }
}
