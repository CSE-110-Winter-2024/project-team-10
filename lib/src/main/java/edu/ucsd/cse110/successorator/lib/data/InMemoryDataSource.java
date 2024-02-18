package edu.ucsd.cse110.successorator.lib.data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
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

    public Subject<List<Task>> getTaskListSubject() { return taskListSubject; }

    public static final List<Task> DEFAULT_TASKS = List.of(
            new Task(1, "Task 1", new Date(), false),
            new Task(2, "Task 2", new Date(), false),
            new Task(3, "Prev Day: complete", new GregorianCalendar(2024, Calendar.FEBRUARY, 15).getTime(), true),
            new Task(4, "PrevDay: uncomplete", new GregorianCalendar(2024, Calendar.FEBRUARY, 15).getTime(), false)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        var currentDate = new Date().toInstant().atZone(ZoneId.systemDefault());
        for (Task task : DEFAULT_TASKS) {
            LocalDate taskDate = task.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (taskDate.isBefore(ChronoLocalDate.from(currentDate)) && task.isCompleted()){
                data.removeTask(task.id());
            }else {
                data.addTask(task);
            }
        }
        return data;
    }

    public void completed(int id, InMemoryDataSource data) {
        var task = findTask(id);
        task.setCompleted(!task.isCompleted());
        if (task.isCompleted()){
            data.removeTask(task.id());
            data.addTask(task);
        }else {
            data.removeTask(task.id());
            data.prependTask(task);
        }
    }

    public void removeTask(int id) {
        var task = findTask(id);

        if (task != null) {
            taskList.remove(task);
        } else {
            System.out.println("Task with ID " + id + " not found.");
        }

        if (taskSubjects.contains(id)) {
            taskSubjects.remove(id);
        }
        taskListSubject.setValue(getTasks());
    }

    public List<Task> getTasks() {
        return List.copyOf(taskList);
    }

    public Task findTask(int id){
        Task taskToComplete = null;

        for (Task task : taskList) {
            if (task.id() == id) {
                taskToComplete = task;
                break;
            }
        }

        return taskToComplete;
    }

    public void prependTask(Task newTask) {
        SimpleSubject<Task> newSubject = new SimpleSubject<Task>();
        newSubject.setValue(newTask);

        taskList.add(0, newTask); // Add task at index 0 to prepend it
        taskSubjects.add(0, newSubject); // Add subject at index 0 to prepend it

        taskListSubject.setValue(taskList);
    }
}