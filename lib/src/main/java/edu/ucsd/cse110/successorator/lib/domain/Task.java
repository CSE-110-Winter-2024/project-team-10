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

    private boolean isCompleted;
    //private int sortOrder;

    public Task(Integer id, String description, Date dateCreated, Boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
        //this.sortOrder =  sortOrder;
    }

    public Integer id() { return id; }

    public String getDescription() {
        return description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDateString() {
        return new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(dateCreated);
    }

//    public int sortOrder() {
//        return sortOrder;
//    }

    public boolean isCompleted() {
        return isCompleted();
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }
}
