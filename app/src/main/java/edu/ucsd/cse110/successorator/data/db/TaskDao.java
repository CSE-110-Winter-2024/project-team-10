package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskEntity task);

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity find(int id);

    @Query("SELECT * FROM tasks ORDER BY id")
    LiveData<List<TaskEntity>> fetchAllAsLiveData();

    // For reordering tasks and adding new tasks
    @Query("SELECT MAX(id) FROM tasks")
    int getMaxId();

    @Query("SELECT MIN(id) FROM tasks")
    int getMinId();

    @Transaction
    default int append(TaskEntity task) {
        var newTask = new TaskEntity(1 + getMaxId(), task.description, task.dateCreated, task.dateCompleted);
        return Math.toIntExact(insert(newTask));
    }

    @Transaction
    default int prepend(TaskEntity task) {
        var newTask = new TaskEntity(getMinId() - 1, task.description, task.dateCreated, task.dateCompleted);
        return Math.toIntExact(insert(newTask));
    }

    @Query("SELECT COUNT(*) FROM tasks")
    int size();
}
