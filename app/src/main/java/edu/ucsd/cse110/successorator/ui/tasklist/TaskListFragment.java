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

import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.util.TaskListFilter;

public class TaskListFragment extends Fragment {
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

        // Obtain the main view model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        var activityModel = modelProvider.get(MainViewModel.class);

        // Initializer the adapter
        this.adapter = new TaskListAdapter(requireContext(), List.of(), getParentFragmentManager(), activityModel::toggleTaskCompletion);

        activityModel.getDateTaskPacketSubject().observe(packet -> {
            // Task context being null is fine
            boolean isNull = (packet == null)
                    || (packet.currentDate == null)
                    || (packet.taskList == null)
                    || (packet.viewMode == null);

            if (isNull) {
                return;
            }

            Log.d("filterTaskList", "filtering tasks with date=" + packet.currentDate.toString());
            var filteredTasks = TaskListFilter.from(packet).filter();

            adapter.clear();
            adapter.addAll(filteredTasks);
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
}

