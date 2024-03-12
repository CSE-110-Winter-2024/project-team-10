package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListAdapter;

public class MainViewModel extends ViewModel {
    private final SimpleSubject<LocalDate> currentDateSubject;
    private final TaskRepository taskRepository;
    private final Subject<List<Task>> taskListSubject;

    // Task view modes
    public enum ViewMode {
        TODAY, TOMORROW, PENDING, RECURRING;

        public static ViewMode fetch(int index) {
            switch (index) {
                case 0:
                    return TODAY;
                case 1:
                    return TOMORROW;
                case 2:
                    return PENDING;
                case 3:
                    return RECURRING;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    // Class is a record of data needed to filter the tasks
    public class FilterPacket {
        public LocalDate currentDate = null;
        public List<Task> taskList = null;
        public ViewMode viewMode = null;

        public FilterPacket() {}
        public FilterPacket(FilterPacket other) {
            this.currentDate = other.currentDate;
            this.taskList = other.taskList;
            this.viewMode = other.viewMode;
        }

        FilterPacket withDate(LocalDate currentDate) {
            FilterPacket fp = new FilterPacket(this);
            fp.currentDate = currentDate;
            return fp;
        }

        FilterPacket withTaskList(List<Task> taskList) {
            FilterPacket fp = new FilterPacket(this);
            fp.taskList = taskList;
            return fp;
        }

        FilterPacket withViewMode(ViewMode viewMode) {
            FilterPacket fp = new FilterPacket(this);
            fp.viewMode = viewMode;
            return fp;
        }
    };

    // Since proper filtering depends on each component,
    // we make an aggregate subject that is updated along with
    // any of its components
    private final SimpleSubject<FilterPacket> filterPacketSubject;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    }
            );

    public MainViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskListSubject = taskRepository.fetchSubjectList();

        // Configure the date to today
        var now = LocalDate.now();
        currentDateSubject = new SimpleSubject<>();

        // Configure a subject for the filtering process
        filterPacketSubject = new SimpleSubject<>();
        filterPacketSubject.setValue(new FilterPacket());

        // Filter packet to update itself if any component changes
        currentDateSubject.observe(date -> {
            var currentPacket = filterPacketSubject.getValue();
            if (date == null || currentPacket == null) {
                return;
            }

            var newPacket = currentPacket.withDate(date);
            filterPacketSubject.setValue(newPacket);
        });

        taskListSubject.observe(list -> {
            var currentPacket = filterPacketSubject.getValue();
            if (list == null || currentPacket == null) {
                return;
            }

            var newPacket = currentPacket.withTaskList(list);
            filterPacketSubject.setValue(newPacket);
        });

        // Update the list of tasks on date change
        currentDateSubject.observe(date -> {
            var list = taskListSubject.getValue();
            if (date == null || list == null) {
                return;
            }

            Log.d("MainViewModel", "refreshing task list due to date change");
            for (Task task : list) {
                task.refreshDateCreated(date);
                taskRepository.replaceTask(task);
            }
        });

        // Trigger date update after all the setup
        currentDateSubject.setValue(now);
    }

    public Subject<LocalDate> getCurrentDateSubject() {
        return currentDateSubject;
    }

    public Subject<FilterPacket> getDateTaskPacketSubject() {
        return filterPacketSubject;
    }

    public void completeTask(Task task) {
        if (task.isCompleted()) {
            // Already completed
            return;
        }

        taskRepository.toggleTaskCompletion(task.id(), currentDateSubject.getValue());
    }

    public void toggleTaskCompletion(Task task) {
        taskRepository.toggleTaskCompletion(task.id(), currentDateSubject.getValue());
    }

    public void removeTask(Task task) {
        taskRepository.removeTask(task.id());
    }

    // Used for long press move to today
    public void changeTaskDateToday(Task task) {
        LocalDate today = LocalDate.now();
        taskRepository.changeTaskDate(task.id(), today);
    }

    // Used for long press move to tomorrow
    public void changeTaskDateTomorrow(Task task) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        taskRepository.changeTaskDate(task.id(), tomorrow);
    }

    // Changing view mode
    public void registerViewMode(ViewMode viewMode) {
        var currentPacket = filterPacketSubject.getValue();
        var newPacket = currentPacket.withViewMode(viewMode);
        filterPacketSubject.setValue(newPacket);
    }

    // Adding new tasks
    public void createTask(String description, TaskRecurrence recurrence, TaskContext context) {
        var now = currentDateSubject.getValue();
        var task = TaskBuilder.from(taskRepository)
                .describe(description)
                .createOn(now)
                .schedule(recurrence)
                .clarify(context)
                .build();
        taskRepository.saveTask(task);
    }

    // Advancing the date forward OR backwards
    public void advanceNextDay() {
        var now = currentDateSubject.getValue();
        now = now.plusDays(1);
        currentDateSubject.setValue(now);
    }

    public void advancePreviousDay() {
        var now = currentDateSubject.getValue();
        now = now.minusDays(1);
        currentDateSubject.setValue(now);
    }
}