package edu.ucsd.cse110.successorator;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Task;
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

        Log.i("debug", "database has " + database.dao().size() + " elements");
        Log.i("debug", "fetch value is " + database.dao().findAllAsLiveData().getValue());

        this.taskRepository = new RoomTaskRepository(database.dao());

        // Configure preferences to mark first time
        var sharedPreferences = getSharedPreferences("successorator", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun && database.dao().size() == 0) {
            for (Task task : InMemoryDataSource.DEFAULT_TASKS) {
                taskRepository.saveTask(task);
            }

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }

//        for (var task : taskRepository.fetchSubjectList().getValue()) {
//            Log.i("Application::DB", "task <" + task.getDescription() + ", " + task.isCompleted() + ">");
//        }
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}