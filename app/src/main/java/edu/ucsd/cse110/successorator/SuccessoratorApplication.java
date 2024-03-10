package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class SuccessoratorApplication extends Application {
    private RoomTaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                SuccessoratorDatabase.class,
                "successorator-database"
        ).allowMainThreadQueries().build();

        this.taskRepository = new RoomTaskRepository(database.dao());

        // Configure preferences to mark first time
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun && database.dao().size() == 0) {
            for (Task task : DEFAULT_TASKS) {
                taskRepository.saveTask(task);
            }

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    public static final List<Task> DEFAULT_TASKS = List.of(
            new Task(1, "One-time task", LocalDate.now(),  null, TaskRecurrence.ONE_TIME),
            new Task(2, "Daily task",    LocalDate.now(),  null, TaskRecurrence.DAILY),
            new Task(3, "Weekly task",   LocalDate.now(),  null, TaskRecurrence.WEEKLY),
            new Task(4, "Monthly task",  LocalDate.now(),  null, TaskRecurrence.MONTHLY),
            new Task(5, "Yearly task",   LocalDate.now(),  null, TaskRecurrence.YEARLY)
    );
}