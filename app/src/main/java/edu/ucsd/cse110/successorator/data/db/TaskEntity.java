package edu.ucsd.cse110.successorator.data.db;

import static edu.ucsd.cse110.successorator.lib.util.LocalDateConvereter.LocalDateToString;
import static edu.ucsd.cse110.successorator.lib.util.LocalDateConvereter.StringToLocalDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;


import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.LocalDateConvereter;

@Entity(tableName = "tasks")
@TypeConverters(LocalDateConvereter.class)
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

    @ColumnInfo(name = "due")
    public String due;

    public TaskEntity(@NonNull Integer id, @NonNull String description, @NonNull Long dateCreated, boolean isCompleted, String due) {
        this.id = id;
        this.description = description;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
        this.due = due;
    }

    public @NonNull Task toTask() {
        return new Task(id, description, Date.from(Instant.ofEpochSecond(dateCreated)), isCompleted, StringToLocalDate(due));
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        return new TaskEntity(task.id(), task.getDescription(), task.getDateCreated().toInstant().getEpochSecond(), task.isCompleted(), LocalDateToString(task.due()));
    }

    //public String getDateText() {
    //    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH);
    //    return due.format(formatter);
    //}
}
