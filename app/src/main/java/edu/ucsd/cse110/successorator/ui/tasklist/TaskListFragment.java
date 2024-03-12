package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

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
            boolean isNull = (packet == null)
                    || (packet.currentDate == null)
                    || (packet.taskList == null);

            if (isNull) {
                return;
            }

            var filteredTasks = filterTaskList(packet);

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

    static private List<Task> filterTaskList(MainViewModel.FilterPacket packet) {
        Log.d("filterTaskList", "filtering tasks with date=" + packet.currentDate.toString());

        // Date to compare to for the task filtering
        LocalDate compareDate = packet.currentDate;
        if (packet.viewMode == MainViewModel.ViewMode.TOMORROW) {
            compareDate = compareDate.plusDays(1);
        }

        List<Task> filteredList = new ArrayList<>();
        for (Task task : packet.taskList) {
            if (packet.viewMode == MainViewModel.ViewMode.PENDING) {
                if (!task.isPending()) {
                    continue;
                }
            } else if (packet.viewMode == MainViewModel.ViewMode.RECURRING) {
                if (task.getTaskRecurrence() == TaskRecurrence.ONE_TIME) {
                    continue;
                }
            } else {
                // Must be TODAY or TOMORROW, so its
                // enough to check with compareDate
                if (!task.displayOnDate(compareDate)) {
                    continue;
                }
            }

            // If we are here then the appropriate checks have passed
            filteredList.add(task);
        }

        return filteredList;
    }
}