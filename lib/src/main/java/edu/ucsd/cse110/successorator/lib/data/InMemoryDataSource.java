package edu.ucsd.cse110.successorator.lib.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class InMemoryDataSource {
    private final List<Task> taskList = new ArrayList<>();
    private final List<Subject<Task>> taskSubjects = new ArrayList<>();
    private final SimpleSubject<List<Task>> taskListSubject = new SimpleSubject<>();

    public InMemoryDataSource() {}

    public void addTask(Task newTask) {
        SimpleSubject<Task> newSubject = new SimpleSubject<Task>();
        newSubject.setValue(newTask);

        taskList.add(newTask);
        taskSubjects.add(newSubject);

        taskListSubject.setValue(taskList);
    }

    public Subject<List<Task>> getTaskListSubject() {
        return taskListSubject;
    }

    public static final List<Task> DEFAULT_TASKS = List.of(
            new Task(1, "Task 1", new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime()),
            new Task(2, "Task 2", new GregorianCalendar(2024, Calendar.FEBRUARY, 2).getTime()),
            new Task(3, "Task 3", new GregorianCalendar(2024, Calendar.FEBRUARY, 2).getTime())
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Task task : DEFAULT_TASKS) {
            data.addTask(task);
        }

        return data;
    }
}
