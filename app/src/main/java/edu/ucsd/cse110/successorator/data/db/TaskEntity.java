package edu.ucsd.cse110.successorator.data.db;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.ucsd.cse110.successorator.lib.domain.Task;
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "dateCreated")
    public String dateCreated;
    @ColumnInfo(name = "sort_order")
    public int sortOrder;
    @ColumnInfo(name = "isCompleted")
    public boolean isCompleted;


    TaskEntity(@NonNull String description,@NonNull String dateCreated, @NonNull boolean isCompleted,int sortOrder) {
        this.description = description;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
        this.sortOrder =  sortOrder;
    }

    public static TaskEntity fromTask(@NonNull Task task) {
        var card = new TaskEntity(task.getDescription(), task.getDateString(), task.isCompleted(), task.getSortOrder());
        card.id = task.id();
        return card;
    }

    public @NonNull Task toTask() {

        return new Task(id, description, new GregorianCalendar(2024, Calendar.FEBRUARY, 1).getTime(), isCompleted, sortOrder);
    }
}
