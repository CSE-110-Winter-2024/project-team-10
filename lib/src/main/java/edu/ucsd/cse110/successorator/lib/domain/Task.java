package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Task {
    // A task is complete if dateCompleted != null
    private final @Nullable Integer id;
    private final @NonNull String description;
    private final @NonNull LocalDate dateCreated;
    private @Nullable LocalDate dateCompleted;

    public Task(@Nullable Integer id, @NonNull String description, @NonNull LocalDate dateCreated, @Nullable LocalDate dateCompleted) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
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

    @NonNull
    public LocalDate getDateCompleted() {
        return dateCompleted;
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

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public void toggleDateCompleted(@NonNull LocalDate dateCompleted) {
        if (isCompleted()) {
            this.dateCompleted = null;
        } else {
            this.dateCompleted = dateCompleted;
        }
    }

    // The following method decides whether to display a task or not at a specific date
    public boolean displaySelf(LocalDate currentDate) {
        if (isCompleted()) {
            ZoneId zone = ZoneId.systemDefault();
            long currentEpochSeconds = currentDate.atStartOfDay(zone).toInstant().getEpochSecond();
            long completedEpochSeconds = dateCompleted.atStartOfDay(zone).toInstant().getEpochSecond();
            return currentEpochSeconds <= completedEpochSeconds;
        } else {
            return true;
        }
    }
}