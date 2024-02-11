package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {
    private final @Nullable Integer id;
    private final @NonNull String description;
    private final @NonNull Date dateCreated;

    public Task(Integer id, String description, Date dateCreated) {
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDateString() {
        return new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(dateCreated);
    }
}
