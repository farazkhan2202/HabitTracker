package my.projects.habittracker.view.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import my.projects.habittracker.R;
import my.projects.habittracker.model.data.habit.Habit;
import my.projects.habittracker.view.adapter.HabitsAdapter;
import my.projects.habittracker.viewmodel.main.MainViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HabitsFragment extends Fragment {

    public interface HabitsFragmentListener {
        void onSignalOpenHabitDetailsFragment(int habitId);
        void onSignalOpenAddHabitFragment();
    }

    private HabitsFragmentListener listener;

    private static final String ADD_CHANGE_DIALOG_TAG = "add_change_dialog";

    private RecyclerView recyclerView;
    private TextView noHabitsPlaceholder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (HabitsFragmentListener) context;
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
        return inflater.inflate(R.layout.fragment_habit_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.habit_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noHabitsPlaceholder = view.findViewById(R.id.no_habits_placeholder);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(@Nullable List<Habit> habits) {
                showHabits(habits);
            }
        });
    }

    private void showHabits(List<Habit> habits) {
        if (habits.size() == 0) {
            noHabitsPlaceholder.setVisibility(VISIBLE);
        } else {
            noHabitsPlaceholder.setVisibility(GONE);
        }

        HabitsAdapter adapter = new HabitsAdapter(habits);
        adapter.setListener(new HabitsAdapter.HabitAdapterListener() {
            @Override
            public void onHabitClicked(long habitId) {
                signalOpenHabitDetailsFragment((int) habitId);
            }

            @Override
            public void onAddChangeForHabitClicked(Habit habit) {
                showAddChangeToHabitDialog(habit);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void signalOpenHabitDetailsFragment(int habitId) {
        listener.onSignalOpenHabitDetailsFragment(habitId);
    }

    private void showAddChangeToHabitDialog(Habit habit) {
        AddChangeDialog dialog = AddChangeDialog.newInstance(habit);
        dialog.show(getFragmentManager(), ADD_CHANGE_DIALOG_TAG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_habits, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_item:
                signalOpenAddHabitFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void signalOpenAddHabitFragment() {
        listener.onSignalOpenAddHabitFragment();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem addHabitMenuItem = menu.findItem(R.id.add_menu_item);
        if (((MainActivity) getActivity()).hasAddHabitFragmentOpenInSecondFrame()) {
            addHabitMenuItem.setVisible(false);
        } else {
            addHabitMenuItem.setVisible(true);
        }
    }
}
