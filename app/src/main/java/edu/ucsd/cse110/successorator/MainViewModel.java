package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    private LocalDate currentDate = LocalDate.now();
    private final TaskRepository taskRepository;
    private final Subject<List<Task>> taskListSubject;

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
    }

    public Subject<List<Task>> getTaskList() {
        return taskListSubject;
    }

    public void toggleTaskCompletion(int id) {
        taskRepository.completeTask(id);
    }

    //Used for long press to delete
    public void toggleTaskDeletion(int id) {
        taskRepository.removeTask(id);
    }

    public void createTask(String description) {
        var task =
                new Task(taskRepository.generateNextId(), description, LocalDateToDate(), false, true);
        taskRepository.saveTask(task);
    }

    public void setCurrentDate(LocalDate date) {
        currentDate = date;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    private Date LocalDateToDate() {
        return Date.from(currentDate.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}