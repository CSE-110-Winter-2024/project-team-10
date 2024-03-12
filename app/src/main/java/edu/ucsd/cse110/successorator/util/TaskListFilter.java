package edu.ucsd.cse110.successorator.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;

public class TaskListFilter {
    // Conditional fields
    List<Task> taskList;
    LocalDate currentDate;
    ViewMode viewMode;
    TaskContext taskContext;

    private TaskListFilter(List<Task> taskList, LocalDate currentDate, ViewMode viewMode, TaskContext taskContext) {
        this.taskList = taskList;
        this.currentDate = currentDate;
        this.viewMode = viewMode;
        this.taskContext = taskContext;
    }

    public static TaskListFilter from(FilterPacket packet) {
        return new TaskListFilter(packet.taskList, packet.currentDate, packet.viewMode, packet.taskContext);
    }

    private List<Task> filterByDate(LocalDate compareDate) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.displayOnDate(compareDate)) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private List<Task> filterPending() {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.isPending()) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private List<Task> filterRecurring() {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getTaskRecurrence() != TaskRecurrence.ONE_TIME) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private List<Task> filterByContext() {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getTaskContext() == taskContext) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    public List<Task> filter() {
        // Context filtering
        if (taskContext != null) {
            return filterByContext();
        }

        // View mode filtering
        switch (viewMode) {
            case TODAY:
                return filterByDate(currentDate);
            case TOMORROW:
                return filterByDate(currentDate.plusDays(1));
            case PENDING:
                return filterPending();
            case RECURRING:
                return filterRecurring();
        }

        // Should not get here, since viewMode should never be null
        throw new IllegalStateException();
    }
}
