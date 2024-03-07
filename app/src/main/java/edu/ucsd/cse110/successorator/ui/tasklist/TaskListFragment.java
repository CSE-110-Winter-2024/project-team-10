package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskListFragment extends Fragment {
    private LocalDate date;

    private TaskListAdapter adapter;

    public TaskListFragment() {}

    public static TaskListFragment newInstance(LocalDate date) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        args.putSerializable("date_key", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) date = (LocalDate) getArguments().getSerializable("date_key");
        else date = LocalDate.now();

        // Initialize the model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);

        var activityModel = modelProvider.get(MainViewModel.class);

        // Initializer the adapter
        this.adapter = new TaskListAdapter(requireContext(), List.of(), activityModel::toggleTaskCompletion, getParentFragmentManager(), activityModel::toggleTaskDeletion, activityModel::toggleTaskMoveToToday, activityModel::toggleTaskMoveToTomorrow);
        activityModel.getTaskList().observe(list -> {
            Log.i("TaskListFragment", "change value, list = " + list);
            if (list == null) return;

            List<Task> filteredList = filterTasks(list);

            adapter.clear();
            adapter.addAll(filteredList);
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceData) {
        var view = FragmentTaskListBinding.inflate(inflater, container, false);
        view.taskList.setAdapter(adapter);
        return view.getRoot();
    }

    private List<Task> filterTasks(List<Task> tasks) {
        List<Task> filteredList = new ArrayList<>();
        for (Task task : tasks) {
            Log.i("date", "date: " + date);
//            if (!task.isCompleted() || !task.getDateCreated().toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate().isBefore(date)) {
//                filteredList.add(task);
//            }
            if (!task.isCompleted() && task.due().isBefore(date)) {
                task.setDue(LocalDate.now());
            }
            if (!task.isCompleted() || !task.due().isBefore(date)) {
                filteredList.add(task);
            }
        }
        return filteredList;
    }
}