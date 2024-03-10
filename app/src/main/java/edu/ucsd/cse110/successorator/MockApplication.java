package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.MockDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;


//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
public class MockApplication extends Application {
    private RoomTaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                getApplicationContext(),
                MockDatabase.class,
                "successorator-database"
        ).allowMainThreadQueries().build();

        this.taskRepository = new RoomTaskRepository(database.dao());

        // Configure preferences to mark first time
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun && database.dao().size() == 0) {
            for (Task task : DEFAULT_TASKS)
                taskRepository.saveTask(task);

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    // Used for testing
    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    // Used for testing
    public RoomTaskRepository getRoomTaskRepository() {return taskRepository;}

    public static final List<Task> DEFAULT_TASKS = List.of(
            new Task(100, "Task 1", new Date(), false, LocalDate.now()),
            new Task(200, "Task 2", new Date(), false, LocalDate.now()),
            new Task(300, "Prev Day: complete", new GregorianCalendar(2024, Calendar.FEBRUARY, 15).getTime(), true, LocalDate.now().minusDays(1)),
            new Task(400, "Prev Day: uncompleted", new GregorianCalendar(2024, Calendar.FEBRUARY, 15).getTime(), false, LocalDate.now().minusDays(1)),
            new Task(500, "Tomorrow Task", new Date(), false, LocalDate.now().plusDays(1))
    );
}