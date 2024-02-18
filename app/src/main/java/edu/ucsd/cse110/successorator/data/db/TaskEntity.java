package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.util.Date;

import edu.ucsd.cse110.successorator.lib.domain.Task;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "dateCreated")
    public Long dateCreated;

    @ColumnInfo(name = "isCompleted")
    public Boolean isCompleted;

    public TaskEntity(@NonNull Integer id, @NonNull String description, @NonNull Long dateCreated, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
    }

    public @NonNull Task toTask() {
        return new Task(id, description, Date.from(Instant.ofEpochSecond(dateCreated)), isCompleted);
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        return new TaskEntity(task.id(), task.getDescription(), task.getDateCreated().toInstant().getEpochSecond(), task.isCompleted());
    }
}
