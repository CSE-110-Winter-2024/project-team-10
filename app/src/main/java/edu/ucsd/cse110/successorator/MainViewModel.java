package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    private final TaskRepository taskRepository;
    private final Subject<List<Task>> taskListSubject;
    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository(), app.getDate());
                    }
            );



    public MainViewModel(TaskRepository taskRepository, String date) {
        this.taskRepository = taskRepository;
        this.taskListSubject = taskRepository.findAll();
    }

    public Subject<List<Task>> getTaskList() {
        return taskListSubject;
    }


    public void toggleTaskCompletion(int id) {
        taskRepository.completed(id);
    }

    public void appendTask(String description) {
        var task = new Task(1, "Task 4", new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime(), false, 0);
        taskRepository.save(task);
    }

}