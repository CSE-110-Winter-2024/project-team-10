package edu.ucsd.cse110.successorator.util;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

// This class is a record of data needed to filter the tasks
public class FilterPacket {
    public LocalDate currentDate = LocalDate.now();
    public List<Task> taskList = null;
    public ViewMode viewMode = null;
    public TaskContext taskContext = null;

    public FilterPacket() {}
    public FilterPacket(FilterPacket other) {
        this.currentDate = other.currentDate;
        this.taskList = other.taskList;
        this.viewMode = other.viewMode;
        this.taskContext = other.taskContext;
    }

    @NonNull
    public FilterPacket withDate(LocalDate currentDate) {
        FilterPacket fp = new FilterPacket(this);
        fp.currentDate = currentDate;
        return fp;
    }

    @NonNull
    public FilterPacket withTaskList(List<Task> taskList) {
        FilterPacket fp = new FilterPacket(this);
        fp.taskList = taskList;
        return fp;
    }

    @NonNull
    public FilterPacket withViewMode(ViewMode viewMode) {
        FilterPacket fp = new FilterPacket(this);
        fp.viewMode = viewMode;
        return fp;
    }

    @NonNull
    public FilterPacket withTaskContext(TaskContext taskContext) {
        FilterPacket fp = new FilterPacket(this);
        fp.taskContext = taskContext;
        return fp;
    }
}
