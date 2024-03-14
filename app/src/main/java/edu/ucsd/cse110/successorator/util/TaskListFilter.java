package edu.ucsd.cse110.successorator.util;

import androidx.annotation.NonNull;

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

    private TaskListFilter(List<Task> taskList, @NonNull LocalDate currentDate, ViewMode viewMode, TaskContext taskContext) {
        this.currentDate = currentDate;
        this.viewMode = viewMode;
        this.taskContext = taskContext;
        this.taskList = taskList;
    }

    public static TaskListFilter from(FilterPacket packet) {
        return new TaskListFilter(packet.taskList, packet.currentDate, packet.viewMode, packet.taskContext);
    }

    public List<Task> filter() {
        // Two stages of filtering
        // 1. Filter by the context
        // 2. Filter by view mode
        List<Task> through = taskList;

        // Context filtering
        if (taskContext != null) {
            through = filterByContext(through, taskContext);
        }

        // View mode filtering
        switch (viewMode) {
            case TODAY:
                through = filterByDate(through, currentDate);
                break;
            case TOMORROW:
                through = filterByDate(through, currentDate.plusDays(1));
                break;
            case PENDING:
                through = filterPending(through);
                break;
            case RECURRING:
                through = filterRecurring(through);
                break;
            default:
                // Should not get here
                throw new IllegalStateException();
        }

        return through;
    }

    // Static specialized filters
    private static List<Task> filterByDate(List<Task> taskList, LocalDate compareDate) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.displayOnDate(compareDate)) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private static List<Task> filterPending(List<Task> taskList) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.isPending()) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private static List<Task> filterRecurring(List<Task> taskList) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getTaskRecurrence() != TaskRecurrence.ONE_TIME) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    private static List<Task> filterByContext(List<Task> taskList, TaskContext taskContext) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getTaskContext() == taskContext) {
                tasks.add(task);
            }
        }

        return tasks;
    }
}
