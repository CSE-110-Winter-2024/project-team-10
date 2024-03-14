package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.MemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.MemoryTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

// This file covers testing of the Task repositories
public class TaskRepository {
    public static final List<Task> DEFAULT_TASKS = List.of(
            TaskBuilder.from(1).describe("One-time task").build(),
            TaskBuilder.from(2).describe("Daily task").schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.DAILY).build(),
            TaskBuilder.from(3).describe("Weekly task").schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.WEEKLY).build(),
            TaskBuilder.from(4).describe("Monthly task").schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.MONTHLY).build(),
            TaskBuilder.from(5).describe("Yearly task").schedule(TaskRecurrence.YEARLY).build()
    );

    public static MemoryTaskRepository listRepository(List<Task> taskList) {
        MemoryDataSource dataSource = MemoryDataSource.fromList(taskList);
        return new MemoryTaskRepository(dataSource);
    }

    // Checking repository saveTask functionality
    @Test
    public void repositoryAddTest() {
        var repo = listRepository(DEFAULT_TASKS);

        Task task;
        task = TaskBuilder.from(5).describe("Task 3").build();
        repo.saveTask(task);

        // The task "Prev Day: completed" should not be there anymore
        assertEquals(6, repo.taskListSize());

        task = TaskBuilder.from(6).describe("Task 3").build();
        repo.saveTask(task);

        assertEquals(7, repo.taskListSize());
    }

    // Checking repository removeTask functionality
    @Test
    public void repositoryRemoveTest() {
        var repo = listRepository(DEFAULT_TASKS);

        repo.removeTask(1);
        assertEquals(4, repo.taskListSize());

        repo.removeTask(4);
        assertEquals(3, repo.taskListSize());
    }

    // Checking repository ID generation functionality
    @Test
    public void repositoryIdGenerationTest() {
        var repo = listRepository(DEFAULT_TASKS);

        int nextId;
        nextId = repo.generateNextId();
        assertEquals(6, nextId);

        repo.removeTask(2);
        nextId = repo.generateNextId();
        assertEquals(6, nextId);

        repo.removeTask(4);
        nextId = repo.generateNextId();
        assertEquals(6, nextId);
    }

    // Checking the rollover during creation
    @Test
    public void repositoryRolloverTest() {
        final List<Task> TEST_LIST1 = List.of(
                TaskBuilder.from(1).describe("Task 1").build(),
                TaskBuilder.from(2).describe("Task 2").build(),
                TaskBuilder.from(3).describe("Previous Day: complete")
                        .createOn(LocalDate.now().minusDays(1))
                        .completeOn(LocalDate.now().minusDays(1))
                        .build(),
                TaskBuilder.from(4).describe("Previous Day: incomplete")
                        .createOn(LocalDate.now().minusDays(1))
                        .build()
        );

        final List<Task> TEST_LIST2 = List.of(
                TaskBuilder.from(1).describe("Task 1").build(),
                TaskBuilder.from(2).describe("Task 2").build(),
                TaskBuilder.from(3).describe("Previous Day: complete (1)")
                        .createOn(LocalDate.now().minusDays(1))
                        .completeOn(LocalDate.now())
                        .build(),
                TaskBuilder.from(4).describe("Previous Day: complete (2)")
                        .createOn(LocalDate.now().minusDays(1))
                        .completeOn(LocalDate.now().minusDays(1))
                        .build(),
                TaskBuilder.from(5).describe("Previous Day: incomplete (1)")
                        .createOn(LocalDate.now().minusDays(1))
                        .build(),
                TaskBuilder.from(6).describe("Previous Day: incomplete (2)")
                        .createOn(LocalDate.now().minusDays(2))
                        .build()
        );

        var repo1 = listRepository(TEST_LIST1);
        var repo2 = listRepository(TEST_LIST2);

        assertEquals(3, repo1.taskListSize());
        assertEquals(4, repo2.taskListSize());
    }
}
