package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    Subject<List<Task>> fetchSubjectList();
    void saveTask(Task task);
    void removeTask(int id);
    void completeTask(int id);
    int generateNextId();


    //public boolean isCompleted();
    //public void setCompleted(boolean completed);
    //public boolean exists();
    //public void setExists(boolean exists);
}