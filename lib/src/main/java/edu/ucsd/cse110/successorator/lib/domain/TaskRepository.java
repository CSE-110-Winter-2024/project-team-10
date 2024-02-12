package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    public Subject<Task> find(String description);
    public Subject<List<Task>> findAll();
    public void save(Task task);
    public int size();
    public void updateTask(Task task);
}