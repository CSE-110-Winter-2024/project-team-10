package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.TaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.ChangeTaskModeDialogFragment;

public class TaskListAdapter extends ArrayAdapter<Task> {
    private Consumer<Integer> onTaskClickComplete;
    private Consumer<Integer> onTaskPressDelete;
    private Consumer<Integer> onTaskPressMoveToToday;
    private Consumer<Integer> onTaskPressMoveToTomorrow;
    private FragmentManager fragmentManager;

    public TaskListAdapter(Context context, List<Task> taskList,
                           Consumer<Integer> onTaskClickComplete,
                           FragmentManager fragmentManager,
                           Consumer<Integer> onTaskPressDelete,
                           Consumer<Integer> onTaskPressMoveToToday,
                           Consumer<Integer> onTaskPressMoveToTomorrow) {
        super(context, 0, new ArrayList<>(taskList));
        this.onTaskClickComplete = onTaskClickComplete;
        this.fragmentManager = fragmentManager;
        this.onTaskPressDelete = onTaskPressDelete;
        this.onTaskPressMoveToToday = onTaskPressMoveToToday;
        this.onTaskPressMoveToTomorrow = onTaskPressMoveToTomorrow;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        var task = getItem(position);
        assert task != null;

        TaskBinding binding;
        if (convertView != null) {
            binding = TaskBinding.bind(convertView);
        } else {
            var layoutInflater = LayoutInflater.from(getContext());
            binding = TaskBinding.inflate(layoutInflater, parent, false);
        }

        binding.description.setText(task.getDescription());
        binding.date.setText(task.getDateString());

        // Set appearance depending on task completion status
        var paintFlags = binding.description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG);
        var backgroundColor = ContextCompat.getColor(getContext(), android.R.color.white);

        if (task.isCompleted()) {
            paintFlags = binding.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG;
            backgroundColor = ContextCompat.getColor(getContext(), R.color.gray);
        }

        binding.description.setPaintFlags(paintFlags);
        binding.getRoot().setBackgroundColor(backgroundColor);

        binding.task.setOnLongClickListener(v -> {
            // Show the ChangeTaskModeDialogFragment when a task is long-pressed
            showChangeTaskModeDialog(task);
            return true;
        });

        binding.task.setOnClickListener(v -> {
            var id = task.id();
            assert id != null;

            Log.i("Click", "id " + task.id() + ", completed? " + task.isCompleted());
            // (un)marks a task as complete
            onTaskClickComplete.accept(id);
            Log.i("Click (After accept)", "id " + task.id() + ", completed? " + task.isCompleted());
        });

        return binding.getRoot();
    }

    // Method to show the ChangeTaskModeDialogFragment
    private void showChangeTaskModeDialog(Task task) {
        ChangeTaskModeDialogFragment dialogFragment = ChangeTaskModeDialogFragment.newInstance(task, onTaskClickComplete, onTaskPressDelete, onTaskPressMoveToToday, onTaskPressMoveToTomorrow);
        dialogFragment.show(fragmentManager, "ChangeTaskModeDialogFragment");
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var task = getItem(position);
        assert task != null;

        var id = task.id();
        assert id != null;

        return id;
    }
}
