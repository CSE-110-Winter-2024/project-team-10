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
import androidx.lifecycle.Observer;


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
    private FragmentActivity modelOwner;
    private ViewModelProvider.Factory modelFactory;
    private ViewModelProvider modelProvider;
    private MainViewModel activityModel;

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
                        activityModel::toggleTaskMoveToTomorrow);

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
//            if (!task.isCompleted() || !task.getDateCreated().toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate().isBefore(date)) {
//                filteredList.add(task);
//            }
            //Tasks should not vanish the moment you complete it
            if (task.isCompleted() && date.isAfter(task.due())) {
                task.setDue(date.minusDays(1));

            //save date before refresh
            //if date before refresh != current date, continue and don't add
            //if date before refresh == current date, let it add

               continue; //moves onto next task, doesn't add current task
            }



            //roll over task mode from "move to today" to "move to today"
            if (!task.isCompleted() && (task.due() == date)) {
                task.setDue(date);
            }
            //roll over task mode from "move to tomorrow" to "move to today"
            else if (!task.isCompleted() && (task.due()== date.plusDays(1))) {
                task.setDue(date);
            }
            //from setting the date in the phone settings
            else if (!task.isCompleted() && (task.due().isBefore(date))) {
                task.setDue(date);
            }

            //This is the previous way we were doing the rollover
//            Log.i("date", "date: " + date);
//        for (Task task : tasks) {
//            if (!task.isCompleted() || !task.getDateCreated().toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate().isBefore(date)) {
//                filteredList.add(task);
//            }

            /* Get rid of tasks that are completed and from previous date
             * Tasks should not vanish the moment you complete it
             *
             * save date before refresh
             * if date before refresh != current date, don't add
             * if date before refresh == current date, let it add
             */
            if (!task.isCompleted() && (dateBeforeClick == date) && (dateBeforeRefresh == date)) {
            //click won't vanish task
            //save refresh date

                filteredList.add(task);
            }




//            if (!task.isCompleted() && task.due().isBefore(date)) {
//                activityModel.toggleTaskMoveToToday(task.id());
//            }
//
//            if (!task.isCompleted() && task.due().isEqual(date)) {
//                filteredList.add(task);
//                Log.i("filtered task", "date: " + date +" | " + "task: " + task.getDescription());
//
//            }
        }
        return filteredList;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        updateTaskDueDates();
    }

    public void updateTaskDueDates() {
        List<Task> tasks = activityModel.getTaskList().getValue();
        for (Task task : tasks) {
            if (!task.isCompleted() && task.due().isBefore(date)) {
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
        return this.date;
    }

}