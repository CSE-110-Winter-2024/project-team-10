package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

public class TaskListFragment extends Fragment {
    private TaskListAdapter adapter;
    private FragmentActivity modelOwner;
    private ViewModelProvider.Factory modelFactory;
    private ViewModelProvider modelProvider;
    private MainViewModel activityModel;
    private List<Task> filteredTasks;
    private static MutableLiveData<TaskContext> taskContextMutableLiveData;

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
        modelOwner = requireActivity();
        modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        activityModel = modelProvider.get(MainViewModel.class);

        taskContextMutableLiveData = new MutableLiveData<>();
        taskContextMutableLiveData.setValue(null);

        // Initializer the adapter
        this.adapter = new TaskListAdapter(
                requireContext(),
                List.of(),
                getParentFragmentManager(),
                activityModel::toggleTaskCompletion,
                activityModel::toggleTaskDeletion,
                activityModel::toggleTaskMoveToToday,
                activityModel::toggleTaskMoveToTomorrow);

        //TODO: create an observer for taskContext
        taskContextMutableLiveData.observe(this, taskContext -> {
            if (taskContextMutableLiveData.getValue() != null){
                List<Task> tasklist = activityModel.getDateTaskPacketSubject().getValue().second;

                filteredTasks = filterTaskList(tasklist, taskContext);
                adapter.clear();
                adapter.addAll(filteredTasks);
                adapter.notifyDataSetChanged();
            }else {
                activityModel.getDateTaskPacketSubject().observe(packet -> {
                    boolean isNull = (packet == null)
                            || (packet.first == null)
                            || (packet.second == null);

                    if (isNull) {
                        return;
                    }

                    filteredTasks = filterTaskList(packet.second, packet.first);

                    adapter.clear();
                    adapter.addAll(filteredTasks);
                    adapter.notifyDataSetChanged();
                });
            }
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

    public static List<Task> filterTaskList(List<Task> tasks, TaskContext taskContext) {
        if (tasks == null) {
            return null;
        }
        List<Task> filteredList = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskContext() == taskContext) {
                filteredList.add(task);
            }
        }
        return filteredList;
    }

    public static void setTaskContextMutableLiveData(TaskContext taskContext) {
        taskContextMutableLiveData.setValue(taskContext);
    }
}

