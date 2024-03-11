package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
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
        this.adapter = new TaskListAdapter(requireContext(), List.of(),
                getParentFragmentManager(),
                activityModel::toggleTaskCompletion,
                activityModel::toggleTaskDeletion,
                activityModel::toggleTaskMoveToToday,
                activityModel::toggleTaskMoveToTomorrow,
                activityModel::toggleSingleTaskMoveToTomorrow);

        activityModel.getDateTaskPacketSubject().observe(packet -> {
            boolean isNull = (packet == null)
                    || (packet.first == null)
                    || (packet.second == null);

            if (isNull) {
                return;
            }

            var filteredTasks = filterTaskList(packet.second, packet.first);

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

    static private List<Task> filterTaskList(List<Task> tasks, LocalDate currentDate) {
        List<Task> filteredList = new ArrayList<>();

        for (Task task : tasks) {
            if (task.displaySelf(currentDate)) {
                filteredList.add(task);
            }
        }

        return filteredList;
    }
}