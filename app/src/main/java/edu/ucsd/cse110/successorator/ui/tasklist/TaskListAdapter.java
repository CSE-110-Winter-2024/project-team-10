package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
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

        String dateText = "Pending";
        if (!task.isPending()) {
            dateText = task.getTaskRecurrence() + " " + task.getDateCreatedString();
        }

        binding.date.setText(dateText);
        binding.dateCompleted.setText(task.getDateCompletedString());

        String contextChar = "" + task.getTaskContext().symbol();
        binding.context.setText(contextChar);

        Drawable contextBackground;
        switch (task.getTaskContext()) {
            case HOME:
                contextBackground = AppCompatResources.getDrawable(getContext(), R.drawable.home_context);
                break;
            case WORK:
                contextBackground = AppCompatResources.getDrawable(getContext(), R.drawable.work_context);
                break;
            case SCHOOL:
                contextBackground = AppCompatResources.getDrawable(getContext(), R.drawable.school_context);
                break;
            case ERRAND:
                contextBackground = AppCompatResources.getDrawable(getContext(), R.drawable.errand_context);
                break;
            default:
                throw new IllegalStateException();
        }

        binding.context.setBackground(contextBackground);

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