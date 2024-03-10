package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;

public class TaskListFragment extends Fragment {
    private LocalDate currentDate;
    private TaskListAdapter adapter;
    private FragmentActivity modelOwner;
    private ViewModelProvider.Factory modelFactory;
    private ViewModelProvider modelProvider;
    private static MainViewModel activityModel;

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

//        if (getArguments() != null) date = (LocalDate) getArguments().getSerializable("date_key");
//        else date = LocalDate.now();

        // Initialize the model
        modelOwner = requireActivity();
        modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the adapter
        this.adapter = new TaskListAdapter(
                        requireContext(),
                        List.of(),
                        activityModel::toggleTaskCompletion,
                        getParentFragmentManager(),
                        activityModel::toggleTaskDeletion,
                        activityModel::toggleTaskMoveToToday,
                        activityModel::toggleTaskMoveToTomorrow,
                        activityModel::toggleSingleTaskMoveToTomorrow);

//        activityModel.getTaskList().observe(list -> {
//            Log.i("TaskListFragment", "change value, list = " + list);
//            if (list == null) return;

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

        // TODO: via streams/maps?
        for (Task task : tasks) {
            if (!task.isCompleted() && task.due().isBefore(currentDate)) {
                activityModel.toggleTaskMoveToToday(task.id());
            }

            if (task.due().isEqual(currentDate)) {
                filteredList.add(task);
            }
        }

        return filteredList;
    }

    public void setDate(LocalDate currentDate) {
        this.currentDate = currentDate;
        updateTaskDueDates();
    }

    public void updateTaskDueDates() {
        List<Task> tasks = activityModel.getDateTaskPacketSubject().getValue().second;
        for (Task task : tasks) {
            if (!task.isCompleted() && task.due().isBefore(currentDate)) {
                activityModel.toggleTaskMoveToTomorrow(task.id());
            }

            //might need to be deleted
//            if (!task.isCompleted() && task.due().isAfter(date)) {
//                // If the task is due after the date, move it to today
//                activityModel.toggleTaskMoveToToday(task.id());
//            }
        }
    }
    public LocalDate getDate() {
        return this.currentDate;
    }

}