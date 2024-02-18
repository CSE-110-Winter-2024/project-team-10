package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    Subject<Task> find(String description);
    Subject<List<Task>> findAll();
    void save(Task task);
    int size();
    void completed(int id);
}