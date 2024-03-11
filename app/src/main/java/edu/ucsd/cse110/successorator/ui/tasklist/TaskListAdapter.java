package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.graphics.Paint;
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
    private final Consumer<Task> onTaskClickComplete;
    private final FragmentManager fragmentManager;

    public TaskListAdapter(Context context, List<Task> taskList, FragmentManager fragmentManager, Consumer<Task> onTaskClickComplete) {
        super(context, 0, new ArrayList<>(taskList));
        this.fragmentManager = fragmentManager;
        this.onTaskClickComplete = onTaskClickComplete;
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

        String dateText = task.getTaskRecurrence() + " " + task.getDateCreatedString();
        binding.date.setText(dateText);
        binding.dateCompleted.setText(task.getDateCompletedString());

        String contextText = "[" + task.getTaskContext().symbol() + "]";
        binding.context.setText(contextText);

        // Set appearance depending on task completion status
        var paintFlags = binding.description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG);
        var backgroundColor = ContextCompat.getColor(getContext(), android.R.color.white);

        if (task.isCompleted()) {
            paintFlags = binding.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG;
            backgroundColor = ContextCompat.getColor(getContext(), R.color.gray);
        }

        binding.description.setPaintFlags(paintFlags);
        binding.getRoot().setBackgroundColor(backgroundColor);

        binding.task.setOnClickListener(v -> {
            // (un)marks a task as complete
            onTaskClickComplete.accept(task);
        });

        binding.task.setOnLongClickListener(v -> {
            // Show the ChangeTaskModeDialogFragment when a task is long-pressed
            ChangeTaskModeDialogFragment dialogFragment = ChangeTaskModeDialogFragment.newInstance(task);
            dialogFragment.show(fragmentManager, "ChangeTaskModeDialogFragment");
            return true;
        });

        return binding.getRoot();
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