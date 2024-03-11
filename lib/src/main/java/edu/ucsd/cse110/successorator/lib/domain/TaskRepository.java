package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    Subject<List<Task>> fetchSubjectList();
    void saveTask(Task task);
    void removeTask(int id);
    void replaceTask(Task task);
    void completeTask(LocalDate date, int id);
    void moveTaskToToday(int id);
    void moveTaskToTomorrow(int id, LocalDate currentDate);
    int generateNextId();
}