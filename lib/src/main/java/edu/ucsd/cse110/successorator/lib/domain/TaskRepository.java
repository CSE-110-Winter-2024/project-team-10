package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface TaskRepository {
    Subject<List<Task>> fetchSubjectList();
    void saveTask(Task task);
    void removeTask(int id);
    void replaceTask(Task task);
    void toggleTaskCompletion(int id, @NonNull LocalDate date);
    void changeTaskDate(int id, LocalDate date);
    int generateNextId();
}