package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;

public class TaskListFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTaskListBinding view;
    private TaskListAdapter adapter;


    public TaskListFragment() {}

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initializer the adapter
        this.adapter = new TaskListAdapter(requireContext(), List.of(), activityModel::toggleTaskCompletion);
        activityModel.getTaskList().observe(list -> {
            if (list == null)
                return;

            adapter.clear();
            adapter.addAll(new ArrayList<>(list));
            adapter.notifyDataSetChanged();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceData) {
        this.view = FragmentTaskListBinding.inflate(inflater, container, false);
        view.taskList.setAdapter(adapter);
        return view.getRoot();
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Set up click listener for each item in the task list
//        adapter.setOnItemClickListener((position, task) -> {
//            // Toggle task completion when a task item is clicked
//            activityModel.toggleTaskCompletion(task);
//        });
//    }
}