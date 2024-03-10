package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import edu.ucsd.cse110.successorator.lib.domain.Task;

@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;

    @ColumnInfo(name = "description")
    public String description;

    // Dates are represented as integers, specifically time from EPOCH in SECONDS
    @ColumnInfo(name = "dateCreated")
    public Long dateCreated;

    // If the task has not been completed, this will be -1
    @ColumnInfo(name = "dateCompleted")
    public Long dateCompleted;

    public TaskEntity(@NonNull Integer id, @NonNull String description, @NonNull Long dateCreated, @NonNull Long dateCompleted) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
    }

    public @NonNull Task toTask() {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate dateCreated = Instant.ofEpochSecond(this.dateCreated).atZone(zone).toLocalDate();
        LocalDate dateCompleted = null;

        if (this.dateCompleted >= 0) {
            dateCompleted = Instant.ofEpochSecond(this.dateCompleted).atZone(zone).toLocalDate();
        }

        return new Task(id, description, dateCreated, dateCompleted);
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        ZoneId zone = ZoneId.systemDefault();
        long epochSecondsCreated = task.getDateCreated()
                .atStartOfDay(zone)
                .toInstant()
                .getEpochSecond();

        long epochSecondsCompleted = -1;
        if (task.isCompleted()) {
            epochSecondsCompleted = task.getDateCompleted()
                    .atStartOfDay(zone)
                    .toInstant()
                    .getEpochSecond();
        }

        return new TaskEntity(task.id(), task.getDescription(), epochSecondsCreated, epochSecondsCompleted);
    }
}
