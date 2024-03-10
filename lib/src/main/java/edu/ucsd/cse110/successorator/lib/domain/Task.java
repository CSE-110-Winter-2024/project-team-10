package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Task {
    // A task is complete if dateCompleted != null
    private final @Nullable Integer id;
    private final @NonNull String description;
    private @NonNull LocalDate dateCreated;
    private @Nullable LocalDate dateCompleted;
    private @NonNull TaskRecurrence taskRecurrence;

    // TODO: builder?
    public Task(@Nullable Integer id, @NonNull String description, @NonNull LocalDate dateCreated, @Nullable LocalDate dateCompleted, @NonNull TaskRecurrence taskRecurrence) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.taskRecurrence = taskRecurrence;
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
                DayOfWeek dow = dateCreated.getDayOfWeek();
                dateCreated = currentDate.with(dow);
                break;
            case MONTHLY:
                // Find the corresponding day of the month in currentDate
                int dom = dateCreated.getDayOfMonth();
                dateCreated = currentDate.withDayOfMonth(dom);
                break;
            case YEARLY:
                // Find the corresponding day of the year in currentDate
                int doy = dateCreated.getDayOfYear();
                dateCreated = currentDate.withDayOfYear(doy);
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