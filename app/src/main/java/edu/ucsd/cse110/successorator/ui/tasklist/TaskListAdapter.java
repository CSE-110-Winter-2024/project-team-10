package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.TaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task> {
    public TaskListAdapter(Context context, List<Task> taskList) {
        super(context, 0, new ArrayList<>(taskList));
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

        binding.getRoot().setOnClickListener(v -> {
            Log.d("TaskListAdapter", "Item clicked: " + task.getDescription());
            // Toggle the completion status of the task
            task.setCompleted(!task.isCompleted());
            // Log the task description and completion status
            String completionStatus = task.isCompleted() ? "completed" : "not completed";
            Log.d("TaskListAdapter", task.getDescription() + " is now " + completionStatus);
            // Update the UI to reflect the change
            notifyDataSetChanged();
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
        assert  id != null;

        return id;
    }
}
