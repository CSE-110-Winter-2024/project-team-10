package edu.ucsd.cse110.successorator.ui.tasklist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.TaskBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskListAdapter extends ArrayAdapter<Task> {
    Consumer<Integer> onTaskClick;

    public TaskListAdapter(Context context, List<Task> taskList, Consumer<Integer> onTaskClick) {
        super(context, 0, new ArrayList<>(taskList));
        this.onTaskClick = onTaskClick;
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

        binding.task.setOnClickListener(v -> {
            var id = task.id();
            assert id != null;
            onTaskClick.accept(id); // (un)marks a task as complete
            if (task.isCompleted()) {
                // Apply the STRIKE_THRU_TEXT_FLAG and gray out the task
                binding.description.setPaintFlags(binding.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray));
            } else {
                // Remove the STRIKE_THRU_TEXT_FLAG and return to default white background
                binding.description.setPaintFlags(binding.description.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                binding.getRoot().setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
            }
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