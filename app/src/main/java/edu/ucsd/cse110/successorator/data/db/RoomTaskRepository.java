package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;


import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomTaskRepository implements TaskRepository {
    private final TaskDao taskDao;

    public RoomTaskRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Subject<Task> find(int id) {
        LiveData<TaskEntity> entityLiveData = taskDao.findAsLiveData(id);
        LiveData<Task> taskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<Task> find(String description) {
        return null;
    }

    @Override
    public Subject<List<Task>> findAll() {
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    @Override
    public void save(Task task) {
        taskDao.insert(TaskEntity.fromTask(task));
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void completed(int id) {
        Task task = taskDao.find(id).toTask();
        taskDao.setIsCompleted(task.id(), !task.isCompleted());
    }

    @Override
    public void save(List<Task> tasks) {
        var entities = tasks.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void append(Task task) {
        taskDao.append(TaskEntity.fromTask(task));
    }

    /*@Override
    public void prepend(Task flashcard) {
        taskDao.prepend(TaskEntity.fromFlashcard(flashcard));
    }*/

    /*@Override
    public void remove(int id) {
        taskDao.delete(id);
    }*/
}

