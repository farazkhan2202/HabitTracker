package my.projects.habittracker.view.adapter;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.Habit;

public class HabitViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView titleTextView;
    ImageButton addChangeButton;

    public HabitViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        titleTextView = itemView.findViewById(R.id.habit_item_title);
        addChangeButton = itemView.findViewById(R.id.habit_item_add_change_button);
    }

    public void bind(Habit habit) {
        titleTextView.setText(habit.getTitle());

        long dayInMillis = 24 * 60 * 60 * 1000;
        long now = new Date().getTime();
        long timeElapsed = now - habit.getLastUpdatedMillis();

        float saturation;
        if (timeElapsed < 3 * dayInMillis) {
            saturation = (61 * ((3 * dayInMillis) / timeElapsed)) / 100;
        } else {
            saturation = 0;
        }

        float[] hsv = new float[] {112, saturation, 0.75f};
        View indicatorView = itemView.findViewById(R.id.indicator_view);
        ((GradientDrawable)indicatorView.getBackground()).setColor(Color.HSVToColor(hsv));
        //itemView.setBackgroundColor(Color.HSVToColor(hsv));
    }
}
