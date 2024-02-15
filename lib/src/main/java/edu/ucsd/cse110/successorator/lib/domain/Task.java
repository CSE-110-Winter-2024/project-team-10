package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class Task {
    private final @Nullable Integer id;
    private final @NonNull String description;
    private final @NonNull LocalDateTime dateCreated;

    public Task(Integer id, String description, LocalDateTime dateCreated) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Integer id() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public String getDateString() {
        return dateCreated.toString();
//        return new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(dateCreated);
    }
}
