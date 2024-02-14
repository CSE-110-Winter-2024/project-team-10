package edu.ucsd.cse110.successorator;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

//    public void toggleTaskCompletion(Task task) {
//        task.setCompleted(!task.isCompleted());
//        taskRepository.updateTask(task);
//    }

    public void toggleTaskCompletion(int id) {
        //task.setCompleted(!task.isCompleted());
        //System.out.println(task.isCompleted());

        taskRepository.completed(id);
    }

    //public void addedTask(Task task) {
    //    taskRepository.addingTask(task);
    //}

    public void remove(int id) {
        taskRepository.remove(id);
    }


}
