package edu.ucsd.cse110.successorator;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.MemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.MemoryTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SuccessoratorUnitTests {
    public static final List<Task> DEFAULT_TASKS = List.of(
            new Task(1, "One-time task", LocalDate.now(), null, TaskRecurrence.ONE_TIME),
            new Task(2, "Daily task", LocalDate.now(), null, TaskRecurrence.DAILY),
            new Task(3, "Weekly task", LocalDate.now(), null, TaskRecurrence.WEEKLY),
            new Task(4, "Monthly task", LocalDate.now(), null, TaskRecurrence.MONTHLY),
            new Task(5, "Yearly task", LocalDate.now(), null, TaskRecurrence.YEARLY)
    );

    public static MemoryTaskRepository listRepository(List<Task> taskList) {
        MemoryDataSource dataSource = MemoryDataSource.fromList(taskList);
        return new MemoryTaskRepository(dataSource);
    }

    // Tests isComplete()
    @Test
    public void isCompleteFalseTest() {
        Task task = new Task(1, "One-time task", LocalDate.now(), null, TaskRecurrence.ONE_TIME);

        var expected = false;
        var actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    // Tests isComplete()
    @Test
    public void isCompleteTrueTest() {
        Task task = new Task(1, "One-time task", LocalDate.now(), LocalDate.now(), TaskRecurrence.ONE_TIME);

        var expected = true;
        var actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    // Tests task description
    @Test
    public void descriptionTest() {
        Task task = new Task(1, "Task 1", LocalDate.now(), null, TaskRecurrence.ONE_TIME);

        var expected = "Task 1";
        var actual = task.getDescription();
        assertEquals(expected, actual);
    }

    // Tests task id
    @Test
    public void idTest() {
        Task task = new Task(1, "Task 1", LocalDate.now(), null, TaskRecurrence.ONE_TIME);

        int expected = 1;
        int actual = task.id();
        assertEquals(expected, actual);
    }

    // Tests task date
    @Test
    public void dateTest() {
        LocalDate date = LocalDate.now();
        Task task = new Task(1, "Task 1", date, null, TaskRecurrence.ONE_TIME);

        var expected = date;
        var actual = task.getDateCreated();
        assertEquals(expected, actual);
    }

    // Checking repository saveTask functionality
    @Test
    public void repositoryAddTest() {
        var repo = listRepository(DEFAULT_TASKS);

        Task task;
        task = new Task(5, "Task 3", LocalDate.now(), null, TaskRecurrence.ONE_TIME);
        repo.saveTask(task);

        // The task "Prev Day: completed" should not be there anymore
        assertEquals(6, repo.taskListSize());

        task = new Task(6, "Task 4", LocalDate.now(), null, TaskRecurrence.ONE_TIME);
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
                new Task(1, "Task 1", LocalDate.now(), null, TaskRecurrence.ONE_TIME),
                new Task(2, "Task 2", LocalDate.now(), null, TaskRecurrence.ONE_TIME),
                new Task(3, "Prev Day: complete", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), TaskRecurrence.ONE_TIME),
                new Task(4, "Prev Day: uncompleted", LocalDate.now().minusDays(1), null, TaskRecurrence.ONE_TIME)
        );

        final List<Task> TEST_LIST2 = List.of(
                new Task(1, "Task 1", LocalDate.now(), null, TaskRecurrence.ONE_TIME),
                new Task(2, "Task 2", LocalDate.now(), null, TaskRecurrence.ONE_TIME),
                new Task(3, "Prev Day: complete", LocalDate.now().minusDays(1), LocalDate.now(), TaskRecurrence.ONE_TIME),
                new Task(4, "Prev Day: complete (2)", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), TaskRecurrence.ONE_TIME),
                new Task(5, "Prev Day: uncompleted", LocalDate.now().minusDays(1), null, TaskRecurrence.ONE_TIME),
                new Task(6, "Prev Day: uncompleted (2)", LocalDate.now().minusDays(2), null, TaskRecurrence.ONE_TIME)
        );

        var repo1 = listRepository(TEST_LIST1);
        var repo2 = listRepository(TEST_LIST2);

        assertEquals(3, repo1.taskListSize());
        assertEquals(4, repo2.taskListSize());
    }
}

