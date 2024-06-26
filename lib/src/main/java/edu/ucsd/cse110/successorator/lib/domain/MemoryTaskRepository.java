package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.MemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import kotlin.NotImplementedError;

public class MemoryTaskRepository implements TaskRepository {
    private final MemoryDataSource dataSource;

    public MemoryTaskRepository(MemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Subject<List<Task>> fetchSubjectList() {
        return dataSource.getTaskListSubject();
    }

    @Override
    public void saveTask(Task task) {
        dataSource.addTask(task);
    }

    @Override
    public void removeTask(int id) {
        dataSource.removeTask(id);
    }

    @Override
    public void replaceTask(Task task) {
        throw new NotImplementedError();
    }

    @Override
    public void toggleTaskCompletion(int id, @NonNull LocalDate dateCompleted) {
        dataSource.toggleTaskCompletion(id, dateCompleted);
    }

    @Override
    public void changeTaskDate(int id, LocalDate date) {
        throw new NotImplementedError();
    }

    @Override
    public int generateNextId() {
        return dataSource.getMaxId() + 1;
    }

    public int taskListSize() {
        return dataSource.taskListSize();
    }
}