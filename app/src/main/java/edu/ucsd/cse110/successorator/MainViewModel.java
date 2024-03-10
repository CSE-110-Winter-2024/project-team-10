package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Pair;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.TopBarFragment;

public class MainViewModel extends ViewModel {
    private TaskListFragment taskListFragment = new TaskListFragment();
    private final SimpleSubject<LocalDate> currentDateSubject;
    private final TaskRepository taskRepository;
    private final Subject<List<Task>> taskListSubject;
    private SimpleSubject<Pair<LocalDate, List<Task>>> dateTaskPacketSubject;

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

//        // Configure the date to "today" within the app
//        // This is where the app can keep changes when refreshed
        var now = LocalDate.now();
        currentDateSubject = new SimpleSubject<>();
        currentDateSubject.setValue(now);

        // Configure a subject for the combination of date and task list
        var currentPacket = new Pair<LocalDate, List<Task>>(null, null);
        dateTaskPacketSubject = new SimpleSubject<>();
        dateTaskPacketSubject.setValue(currentPacket);

        // Needs to update itself if either component changes
        taskListSubject.observe(list -> {
            var currentValue = dateTaskPacketSubject.getValue();
            if (list == null || currentValue == null) {
                return;
            }

            var newValue = new Pair<>(currentValue.first, list);
            dateTaskPacketSubject.setValue(newValue);
        });

        currentDateSubject.observe(date -> {
            var currentValue = dateTaskPacketSubject.getValue();
            if (date == null || currentValue == null) {
                return;
            }

            var newValue = new Pair<>(date, currentValue.second);
            dateTaskPacketSubject.setValue(newValue);
        });
    }

    public Subject<LocalDate> getCurrentDateSubject() {
        return currentDateSubject;
    }

    public Subject<Pair<LocalDate, List<Task>>> getDateTaskPacketSubject() {
        return dateTaskPacketSubject;
    }

    // TODO: move outta here?
    public void toggleTaskCompletion(int id) {
        taskRepository.completeTask(id);
    }

    //Used for long press to delete
    public void toggleTaskDeletion(int id) {
        taskRepository.removeTask(id);
    }

    //Used for long press move to today
    public void toggleTaskMoveToToday(int id) {
        taskRepository.moveTaskToToday(id);
    }

    //Used for long press move to tomorrow
    public void toggleTaskMoveToTomorrow(int id) {
        LocalDate currentDate = currentDateSubject.getValue();
        taskRepository.moveTaskToTomorrow(id, currentDate);
    }

    public void toggleSingleTaskMoveToTomorrow(int id) {
        LocalDate currentDate = currentDateSubject.getValue();
        taskRepository.moveTaskToTomorrow(id, currentDate.plusDays(1));
    }


    //Where tasks are created
    public void createTask(String description) {
        var nowDate = currentDateSubject.getValue();
        var nowLocalDate = Date.from(
                nowDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );

        var task = new Task(
                taskRepository.generateNextId(),
                description, nowLocalDate,
                false,
                currentDateSubject.getValue());

        taskRepository.saveTask(task);
    }

    // Advancing the date forward OR backwards
    public void moveNextDay() {
        var now = currentDateSubject.getValue();
        now = now.plusDays(1);
        currentDateSubject.setValue(now);
        taskListFragment.setDate(now);
    }

    public void movePreviousDay() {
        var now = currentDateSubject.getValue();
        now = now.minusDays(1);
        currentDateSubject.setValue(now);
    }
}