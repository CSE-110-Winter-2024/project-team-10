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
    // A task is complete if dateCompleted != null
    private final @Nullable Integer id;
    private final @NonNull String description;
    private @NonNull LocalDate dateCreated;
    private @Nullable LocalDate dateCompleted;
    private @NonNull TaskRecurrence taskRecurrence;
    private @NonNull TaskContext taskContext;

    // TODO: create a builder class
    public Task
            (@Nullable Integer id,
             @NonNull String description,
             @NonNull LocalDate dateCreated,
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

    @NonNull
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

    public void toggleDateCompleted(@NonNull LocalDate dateCompleted) {
        if (isCompleted()) {
            this.dateCompleted = null;
        } else {
            this.dateCompleted = dateCompleted;
        }
    }

    // Checks if dateCreated should be refreshed based on the current date (triggered on date changes)
    public void refreshDateCreated(LocalDate currentDate) {
        var previousDateCreated = dateCreated;

        switch (taskRecurrence) {
            case ONE_TIME:
                // Never refresh one-time tasks
                break;
            case DAILY:
                // Refresh to the currentDate
                // Need a local copy so evaluate a dummy expression
                dateCreated = currentDate.minusDays(0);
                break;
            case WEEKLY:
                // Find the corresponding day of the week in currentDate
                long weeksBetween = ChronoUnit.WEEKS.between(dateCreated, currentDate);
                if (currentDate.isAfter(dateCreated)) {
                    dateCreated = dateCreated.plusWeeks(weeksBetween);
                }
                break;
            case MONTHLY:
                // Find the corresponding day of the month in currentDate
                long monthsBetween = ChronoUnit.MONTHS.between(dateCreated, currentDate);
                if (currentDate.isAfter(dateCreated)) {
                    dateCreated = dateCreated.plusMonths(monthsBetween);
                }
                break;
            case YEARLY:
                // Find the corresponding day of the year in currentDate
                long yearsBetween = ChronoUnit.YEARS.between(dateCreated, currentDate);
                if (currentDate.isAfter(dateCreated)) {
                    dateCreated = dateCreated.plusYears(yearsBetween);
                }
                break;
            default:
                break;
        }

        // Reset the completion date if needed
        // This is only when the date of creation changes
        if (!previousDateCreated.equals(dateCreated)) {
            dateCompleted = null;
        }
    }

    // The following method decides whether to display a task or not at a specific date
    // TODO: pass the current task context
    public boolean displaySelf(LocalDate currentDate) {
        // Always show if not completed
        if (isCompleted()) {
            // Show if the completion date is before or at the current one
            ZoneId zone = ZoneId.systemDefault();
            long currentEpochSeconds = currentDate.atStartOfDay(zone).toInstant().getEpochSecond();
            long completedEpochSeconds = dateCompleted.atStartOfDay(zone).toInstant().getEpochSecond();
            return currentEpochSeconds <= completedEpochSeconds;
        } else {
            return true;
        }
    }
}