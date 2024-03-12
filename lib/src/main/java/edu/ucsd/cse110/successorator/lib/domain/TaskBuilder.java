package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;

public class TaskBuilder {
    private @Nullable Integer id;
    private @NonNull String description;
    private @NonNull LocalDate dateCreated;
    private @Nullable LocalDate dateCompleted;
    private @NonNull TaskRecurrence taskRecurrence;
    private @NonNull TaskContext taskContext;

    public TaskBuilder(int id) {
        this.id = id;

        description = "";
        dateCreated = LocalDate.now();
        dateCompleted = null;
        taskRecurrence = TaskRecurrence.ONE_TIME;
        taskContext = TaskContext.HOME;
    }

    // Manual ID
    public static TaskBuilder from(int id) {
        return new TaskBuilder(id);
    }

    // Automatic ID from repository
    public static TaskBuilder from(TaskRepository repository) {
        return new TaskBuilder(repository.generateNextId());
    }

    // Configure the description
    public TaskBuilder describe(String description) {
        assert description != null;
        this.description = description;
        return this;
    }

    // Configure the creation date
    public TaskBuilder createOn(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    // Configure the completion date
    public TaskBuilder completeOn(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }

    // Configure recurrence
    public TaskBuilder schedule(TaskRecurrence recurrence) {
        this.taskRecurrence = recurrence;
        return this;
    }

    // Configure context
    public TaskBuilder clarify(TaskContext context) {
        this.taskContext = context;
        return this;
    }

    // Final build
    public Task build() {
        return new Task(id, description, dateCreated, dateCompleted, taskRecurrence, taskContext);
    }
}
