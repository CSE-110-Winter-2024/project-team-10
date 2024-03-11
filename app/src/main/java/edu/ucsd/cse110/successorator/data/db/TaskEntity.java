package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskMode;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

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

    // Task recurrence as an integer
    @ColumnInfo(name = "taskRecurrence")
    public Integer taskRecurrence;

    // Task mode as an integer
    @ColumnInfo(name = "taskMode")
    public Integer taskMode;

    // Task context as a character (H, S, W, E)
    @ColumnInfo(name = "taskContext")
    public Character taskContext;

    public TaskEntity
            (@NonNull Integer id,
             @NonNull String description,
             @NonNull Long dateCreated,
             @NonNull Long dateCompleted,
             @NonNull Integer taskRecurrence,
             @NonNull Integer taskMode,
             @NonNull Character taskContext) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateCompleted = dateCompleted;
        this.taskRecurrence = taskRecurrence;
        this.taskMode = taskMode;
        this.taskContext = taskContext;
    }

    public @NonNull Task toTask() {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate created = Instant.ofEpochSecond(this.dateCreated).atZone(zone).toLocalDate();
        LocalDate completed = null;

        if (this.dateCompleted >= 0) {
            completed = Instant.ofEpochSecond(this.dateCompleted).atZone(zone).toLocalDate();
        }

        return new Task(
                id, description,
                created, completed,
                TaskRecurrence.fetch(taskRecurrence),
                TaskMode.fetch(taskMode),
                TaskContext.fetch(taskContext)
        );
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

        return new TaskEntity(
                task.id(), task.getDescription(),
                epochSecondsCreated, epochSecondsCompleted,
                task.getTaskRecurrence().value(),
                task.getTaskMode().value(),
                task.getTaskContext().symbol()
        );
    }

    // Rolls over task modes
    public void switchMode (Task task) {
        if (task.getTaskMode() == TaskMode.TOMORROW) {
            task.setTaskMode(TaskMode.TODAY);
        }
        else if (task.getTaskMode() == TaskMode.TODAY) {
            task.setTaskMode(TaskMode.TODAY);
        }
    }
}