package edu.ucsd.cse110.successorator;

import android.app.Application;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private SimpleTaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.taskRepository = new SimpleTaskRepository(dataSource);
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}