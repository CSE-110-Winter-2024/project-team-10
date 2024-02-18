package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Task {
    private final @Nullable Integer id;
    private final @NonNull String description;
    private final @NonNull Date dateCreated;

    private boolean isCompleted;

    public Task(@Nullable Integer id, @NonNull String description, @NonNull Date dateCreated, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
    }

    public Integer id() { return id; }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDateString() {
        return new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(dateCreated);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

}