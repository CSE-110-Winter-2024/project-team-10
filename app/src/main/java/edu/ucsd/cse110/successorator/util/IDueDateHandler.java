package edu.ucsd.cse110.successorator.util;

import java.time.LocalDate;

import edu.ucsd.cse110.successorator.lib.domain.Task;

public interface IDueDateHandler {
    public void setDueDate(LocalDate dueDate);
    public LocalDate getDueDate();

    public void moveTaskToTomorrow(Task task);
}
