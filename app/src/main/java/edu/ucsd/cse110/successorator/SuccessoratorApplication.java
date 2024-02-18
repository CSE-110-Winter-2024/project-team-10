package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.SimpleTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private TaskRepository taskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

       //this.dataSource = InMemoryDataSource.fromDefault();
       //this.taskRepository = new SimpleTaskRepository(dataSource);

        var database = Room.databaseBuilder(
                getApplicationContext(),
                SuccessoratorDatabase.class,
                "successorator-database"
        )
                .allowMainThreadQueries()
                .build();

        this.taskRepository = new RoomTaskRepository(database.taskDao());
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun && database.taskDao().count() == 0) {
            taskRepository.save(InMemoryDataSource.DEFAULT_TASKS);

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
    public String getDate() {
        return new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(new Date());
    }
}