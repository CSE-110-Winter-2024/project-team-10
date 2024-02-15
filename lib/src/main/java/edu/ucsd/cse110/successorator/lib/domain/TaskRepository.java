package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    public Subject<Task> find(String description);
    public Subject<List<Task>> findAll();
    public void save(Task task);
    public int size();


    //public void completeAndCrossOut(Task task);

    public void completed(int id);
    //public void updateTask(Task task);

    //public void append(Task task);

    public void addingTask(Task task);

    public void remove(int id);
}