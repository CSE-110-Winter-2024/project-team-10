package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class Task {
    // A task is pending if dateCreated == null
    // A task is complete if dateCompleted != null
    private final @Nullable Integer id;
    private final @NonNull String description;
    private @Nullable LocalDate dateCreated;
    private @Nullable LocalDate dateCompleted;
    private @NonNull TaskRecurrence taskRecurrence;
    private @NonNull TaskContext taskContext;

    // TODO: create a builder class
    public Task
            (@Nullable Integer id,
             @NonNull String description,
             @Nullable LocalDate dateCreated,
             @Nullable LocalDate dateCompleted,
             @NonNull TaskRecurrence taskRecurrence,
             @NonNull TaskContext taskContext) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.taskRecurrence = taskRecurrence;
        this.taskContext = taskContext;
    }

    public Integer id() { return id; }

    @NonNull
    public String getDescription() {
        return description;
    }

    @Nullable
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    @Nullable
    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    @NonNull
    public TaskRecurrence getTaskRecurrence() {
        return taskRecurrence;
    }

    @NonNull
    public TaskContext getTaskContext() {
        return taskContext;
    }

    public String getDateCreatedString() {
        var formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd");
        return dateCreated.format(formatter);
    }

    // For debugging only
    public String getDateCompletedString() {
        if (dateCompleted == null) {
            return "";
        }

        var formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd");
        return dateCompleted.format(formatter);
    }

    public boolean isCompleted() {
        return dateCompleted != null;
    }

    public boolean isPending() {
        return dateCreated == null;
    }

    public void toggleDateCompleted(@NonNull LocalDate dateCompleted) {
        if (isCompleted()) {
            this.dateCompleted = null;
        } else {
            this.dateCompleted = dateCompleted;
        }
    }

    // The following method decides whether to display
    // the task or not at a specific date, considering
    // only its creation date and (up to date) completion
    // date (rollover or not)
    public boolean displayOnDate(LocalDate currentDate) {
        // Pending means null dateCreated
        if (isPending()) {
            return false;
        }

        // If the current date is before, skip
        if (currentDate.isBefore(dateCreated)) {
            return false;
        }

        // Considering the task completion
        if (isCompleted()) {
            return currentDate.isBefore(dateCompleted);
        } else {
            return true;
        }
    }

    // Update the creation date of the task depending on
    // the current date and recurrence, assumes that the
    // currenDate does not move backwards
    public void refreshDates(LocalDate currentDate) {
        // Skip if pending, ahead of currentDate/schedule
        if (isPending() || currentDate.isBefore(dateCreated)) {
            return;
        }

        // Record old date for later
        LocalDate previousDate = dateCreated;

        switch (taskRecurrence) {
            case ONE_TIME:
                // No refreshes
                break;
            case DAILY:
                dateCreated = currentDate;
                break;
            case WEEKLY:
                long weeks = ChronoUnit.WEEKS.between(dateCreated, currentDate);
                dateCreated = dateCreated.plusWeeks(weeks);
                break;
            case MONTHLY:
                // A month is ~4 weeks (to avoid end of month errors)
                long months = ChronoUnit.WEEKS.between(dateCreated, currentDate)/4;
                dateCreated = dateCreated.plusWeeks(4 * months);
                break;
            case YEARLY:
                long years = ChronoUnit.YEARS.between(dateCreated, currentDate);
                dateCreated = dateCreated.plusYears(years);
                break;
            default:
                throw new IllegalStateException();
        }

        // If the date was update, then reset task completion
        if (!previousDate.equals(dateCreated)) {
            dateCompleted = null;
        }
    }
}