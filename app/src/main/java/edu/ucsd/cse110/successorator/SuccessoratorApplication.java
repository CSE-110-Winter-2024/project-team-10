package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
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
            // Properly loading dummy tasks
            List<Task> completed = new ArrayList<>();
            List<Task> uncompleted = new ArrayList<>();
            for (Task task : DEFAULT_TASKS) {
                if (task.isCompleted()) {
                    completed.add(task);
                } else {
                    uncompleted.add(task);
                }
            }

            // Uncompleted tasks first, then completed ones
            for (Task task : uncompleted) {
                taskRepository.saveTask(task);
            }

            for (Task task : completed) {
                taskRepository.saveTask(task);
            }

            // Unmark first run status
            sharedPreferences
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    public static final List<Task> DEFAULT_TASKS = List.of(
            TaskBuilder.from(1).describe("One-time task").build(),
            TaskBuilder.from(2).describe("Daily task").schedule(TaskRecurrence.DAILY).clarify(TaskContext.WORK).build(),
            TaskBuilder.from(3).describe("Weekly task").schedule(TaskRecurrence.WEEKLY).build(),
            TaskBuilder.from(4).describe("Monthly task").schedule(TaskRecurrence.MONTHLY).completeOn(LocalDate.now().minusDays(1)).clarify(TaskContext.ERRAND).build(),
            TaskBuilder.from(5).describe("Yearly task").schedule(TaskRecurrence.YEARLY).completeOn(LocalDate.now().minusWeeks(2)).clarify(TaskContext.SCHOOL).build()
    );
}