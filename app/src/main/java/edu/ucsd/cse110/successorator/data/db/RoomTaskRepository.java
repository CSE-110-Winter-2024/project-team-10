package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.Transformations;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubject;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao dao;

    public RoomTaskRepository(TaskDao dao) {
        this.dao = dao;
    }

    @Override
    public Subject<List<Task>> fetchSubjectList() {
        var entitiesLiveData = dao.fetchAllAsLiveData();
        var liveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });

        return new LiveDataSubject<>(liveData);
    }

    @Override
    public void saveTask(Task task) {
        if (task.isCompleted()) {
            dao.append(TaskEntity.fromTask(task));
        } else {
            dao.prepend(TaskEntity.fromTask(task));
        }
    }

    @Override
    public void removeTask(int id) {
        dao.delete(id);
    }

    @Override
    public void toggleTaskCompletion(@NonNull LocalDate dateCompleted, int id) {
        var task = dao.find(id).toTask();
        task.toggleDateCompleted(dateCompleted);

        dao.delete(id);
        if (task.isCompleted()) {
            dao.append(TaskEntity.fromTask(task));
        } else {
            dao.prepend(TaskEntity.fromTask(task));
        }
    }

    @Override
    public int generateNextId() {
        return 1 + dao.getMaxId();
    }
}
