package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.MemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

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
    public void toggleTaskCompletion(LocalDate dateCompleted, int id) {
        dataSource.toggleTaskComplettion(dateCompleted, id);
    }

    @Override
    public int generateNextId() {
        return dataSource.getMaxId() + 1;
    }

    public int taskListSize() {
        return dataSource.taskListSize();
    }
}