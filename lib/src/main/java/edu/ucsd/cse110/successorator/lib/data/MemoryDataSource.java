package edu.ucsd.cse110.successorator.lib.data;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MemoryDataSource {
    private final List<Task> taskList = new ArrayList<>();
    private final List<Subject<Task>> taskSubjects = new ArrayList<>();
    private final SimpleSubject<List<Task>> taskListSubject = new SimpleSubject<>();

    public MemoryDataSource() {}

    public static MemoryDataSource fromList(List<Task> taskList) {
        var data = new MemoryDataSource();
        var currentDate = new Date().toInstant().atZone(ZoneId.systemDefault());
        for (Task task : taskList) {
            LocalDate taskDate = task.getDateCreated();
            if (taskDate.isAfter(ChronoLocalDate.from(currentDate)) || !task.isCompleted()) {
                data.addTask(task);
            }
        }

        return data;
    }

    public Subject<List<Task>> getTaskListSubject() {
        return taskListSubject;
    }

    public int getMaxId() {
        int max = Integer.MIN_VALUE;
        for (Task task : taskList) {
            max = Math.max(task.id(), max);
        }

        return max;
    }

    public void toggleTaskComplettion(LocalDate dateCompleted, int id) {
        var task = findTask(id);
        task.toggleDateCompleted(dateCompleted);

        removeTask(task.id());
        if (task.isCompleted()) {
            addTask(task);
        } else {
            prependTask(task);
        }
    }

    public void removeTask(int id) {
        var task = findTask(id);
        if (task == null)
            throw new IllegalStateException();

        taskList.remove(task);

        // TODO: this is wrong, but ok for now
        if (taskList.contains(id)) {
            taskSubjects.remove(id);
        }

        taskListSubject.setValue(List.copyOf(taskList));
    }

    public Task findTask(int id){
        for (Task task : taskList) {
            if (task.id() == id) {
                return task;
            }
        }

        return null;
    }

    public void addTask(Task newTask) {
        SimpleSubject<Task> newSubject = new SimpleSubject<Task>();
        newSubject.setValue(newTask);
        taskList.add(newTask);
        taskSubjects.add(newSubject);
        taskListSubject.setValue(taskList);
    }

    public void prependTask(Task newTask) {
        SimpleSubject<Task> newSubject = new SimpleSubject<Task>();
        newSubject.setValue(newTask);
        taskList.add(0, newTask);
        taskSubjects.add(0, newSubject);
        taskListSubject.setValue(taskList);
    }

    public int taskListSize() {
        return taskList.size();
    }
}