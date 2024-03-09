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
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
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

        // Configure the date to today
        var now = LocalDate.now();
        currentDateSubject = new SimpleSubject<>();
        currentDateSubject.setValue(now);

        // Configure a subject for the combination of date and task list
        var currentPacket = new Pair<LocalDate, List<Task>>(null, null);
        dateTaskPacketSubject = new SimpleSubject<>();
        dateTaskPacketSubject.setValue(currentPacket);

        taskListSubject.observe(list -> {
            var currentValue = dateTaskPacketSubject.getValue();
            if (list == null || currentValue == null) {
                return;
            }

            var newValue = new Pair<>(currentValue.first, list);
            Log.d("MainViewModel", "[1] newValue=" + newValue.toString());
            dateTaskPacketSubject.setValue(newValue);
        });

        currentDateSubject.observe(date -> {
            var currentValue = dateTaskPacketSubject.getValue();
            if (date == null || currentValue == null) {
                return;
            }

            var newValue = new Pair<>(date, currentValue.second);
            Log.d("MainViewModel", "[2] newValue=" + newValue.toString());
            dateTaskPacketSubject.setValue(newValue);
        });
    }

    public Subject<List<Task>> getTaskListSubject() {
        return taskListSubject;
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

    public void createTask(String description) {
        var task = new Task(taskRepository.generateNextId(), description, LocalDateToDate(), false);
        taskRepository.saveTask(task);
    }

    public void moveNextDay() {
        var now = currentDateSubject.getValue();
        now = now.plusDays(1);
        currentDateSubject.setValue(now);
    }

    public void movePreviousDay() {
        var now = currentDateSubject.getValue();
        now = now.minusDays(1);
        currentDateSubject.setValue(now);
    }

    // TODO: remove this?
    private Date LocalDateToDate() {
        var now = currentDateSubject.getValue();
        return Date.from(now.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}