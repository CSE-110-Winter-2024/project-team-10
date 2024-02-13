package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleTaskRepository implements TaskRepository {
    private final InMemoryDataSource dataSource;

    public SimpleTaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Task> find(String description) {
        return null;
    }

    @Override
    public Subject<List<Task>> findAll() {
        return dataSource.getTaskListSubject();
    }

    @Override
    public void save(Task task) {
        dataSource.addTask(task);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void completed(int id) {
        dataSource.completed(id);
        //return true;
    }



    //Completing a task
//    public void completeAndCrossOut(Task task) {
//        dataSource.putTask(task.getMaxSortOrder());
//    }

//    public void updateTask(Task task) {
//        // Print whether the task is completed or not
//        System.out.println("Task '" + task.getDescription() + "' is " + (task.isCompleted() ? "completed" : "not completed"));
//        // Delegate the task update operation to the InMemoryDataSource
//        dataSource.updateTask(task);
//    }

}
