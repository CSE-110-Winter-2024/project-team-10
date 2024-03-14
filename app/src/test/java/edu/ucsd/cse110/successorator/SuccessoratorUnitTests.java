package edu.ucsd.cse110.successorator;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.MemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.MemoryTaskRepository;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.util.FilterPacket;
import edu.ucsd.cse110.successorator.util.TaskListFilter;

public class SuccessoratorUnitTests {
    public static final List<Task> DEFAULT_TASKS = List.of(
            TaskBuilder.from(1).describe("One-time task").build(),
            TaskBuilder.from(2).describe("Daily task").schedule(TaskRecurrence.DAILY).build(),
            TaskBuilder.from(3).describe("Weekly task").schedule(TaskRecurrence.WEEKLY).build(),
            TaskBuilder.from(4).describe("Monthly task").schedule(TaskRecurrence.MONTHLY).build(),
            TaskBuilder.from(5).describe("Yearly task").schedule(TaskRecurrence.YEARLY).build()
    );

    public static MemoryTaskRepository listRepository(List<Task> taskList) {
        MemoryDataSource dataSource = MemoryDataSource.fromList(taskList);
        return new MemoryTaskRepository(dataSource);
    }

    // Tests isComplete()
    @Test
    public void isCompleteFalseTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").build();
        assertEquals(false, task.isCompleted());
    }

    // Tests isComplete()
    @Test
    public void isCompleteTrueTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").completeOn(LocalDate.now()).build();
        assertEquals(true, task.isCompleted());
    }

    // Tests task description
    @Test
    public void descriptionTest() {
        Task task = TaskBuilder.from(1).describe("Task 1").build();

        var expected = "Task 1";
        var actual = task.getDescription();
        assertEquals(expected, actual);
    }

    // Tests task id
    @Test
    public void idTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").build();

        int expected = 1;
        int actual = task.id();
        assertEquals(expected, actual);
    }

    // Tests pending status
    @Test
    public void isPendingFalseTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").createOn(LocalDate.now()).build();
        assertEquals(false, task.isPending());
    }

    @Test
    public void isPendingTrueTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").createOn(null).build();
        assertEquals(true, task.isPending());
    }

    // Tests task date
    @Test
    public void dateTest() {
        LocalDate date = LocalDate.now();
        Task task = TaskBuilder.from(1).describe("One-time task").createOn(date).build();
        var actual = task.getDateCreated();
        assertEquals(date, actual);
    }

    // Tests task refreshing for recurrence
    @Test
    public void onetimeRecurrenceTest() {
        LocalDate date = LocalDate.now();
        LocalDate moved = date.plusDays(5);

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).build();
        assertEquals(date, task.getDateCreated());

        task.refreshDates(moved);
        assertEquals(date, task.getDateCreated());
    }

    @Test
    public void dailyRecurrenceTest() {
        LocalDate date = LocalDate.now();
        LocalDate moved = date.plusDays(5);

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(TaskRecurrence.DAILY).build();
        assertEquals(date, task.getDateCreated());

        task.refreshDates(moved);
        assertEquals(moved, task.getDateCreated());
    }

    @Test
    public void weeklyRecurrenceTest() {
        LocalDate date = LocalDate.now();
        LocalDate dateNextWeek = LocalDate.now().plusWeeks(1);
        LocalDate movedSameWeek = date.plusDays(5);
        LocalDate movedNextWeek = date.plusDays(9);

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(TaskRecurrence.WEEKLY).build();
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedSameWeek);
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedNextWeek);
        assertEquals(dateNextWeek, task.getDateCreated());
    }

    @Test
    public void monthlyRecurrenceTest() {
        LocalDate date = LocalDate.now();
        LocalDate dateNextMonth = LocalDate.now().plusWeeks(4);
        LocalDate movedSameMonth = date.plusDays(5);
        LocalDate movedNextMonth = date.plusMonths(1).plusDays(2);

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(TaskRecurrence.MONTHLY).build();
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedSameMonth);
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedNextMonth);
        assertEquals(dateNextMonth, task.getDateCreated());
    }

    @Test
    public void yearlyRecurrenceTest() {
        LocalDate date = LocalDate.now();
        LocalDate dateNextYear = LocalDate.now().plusYears(1);
        LocalDate movedSameYear = date.plusMonths(7).plusDays(5);
        LocalDate movedNextYear = date.plusYears(1).plusMonths(5).plusDays(2);

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(TaskRecurrence.YEARLY).build();
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedSameYear);
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedNextYear);
        assertEquals(dateNextYear, task.getDateCreated());
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


    // Checking that the task list can filter by a selected focus
    @Test
    public void filterContextTest() {
        // Benchmark task list
        Task errandTask = TaskBuilder.from(1).describe("Monthly task").schedule(TaskRecurrence.MONTHLY).completeOn(LocalDate.now().minusDays(1)).clarify(TaskContext.ERRAND).build();
        Task homeTask1 = TaskBuilder.from(2).describe("One-time task (1)").build();
        Task homeTask2 = TaskBuilder.from(3).describe("Weekly task").schedule(TaskRecurrence.WEEKLY).build();
        Task workTask1 = TaskBuilder.from(4).describe("One-time task (2)").clarify(TaskContext.WORK).build();
        Task workTask2 = TaskBuilder.from(5).describe("Daily task").schedule(TaskRecurrence.DAILY).clarify(TaskContext.WORK).build();
        Task schoolTask = TaskBuilder.from(6).describe("Yearly task").schedule(TaskRecurrence.YEARLY).completeOn(LocalDate.now().minusWeeks(2)).clarify(TaskContext.SCHOOL).build();

        List<Task> taskList = List.of(errandTask, homeTask1, homeTask2, workTask1, workTask2, schoolTask);

        // Errand
        FilterPacket errandPacket = (new FilterPacket())
                .withTaskList(taskList)
                .withTaskContext(TaskContext.ERRAND);

        List<Task> actualErrand = TaskListFilter.from(errandPacket).filter();
        List<Task> expectedErrand = List.of();
        assertEquals(expectedErrand, actualErrand);

        // Home
        FilterPacket homePacket = (new FilterPacket())
                .withTaskList(taskList)
                .withTaskContext(TaskContext.HOME);

        List<Task> actualHome = TaskListFilter.from(homePacket).filter();
        List<Task> expectedHome = List.of(homeTask1, homeTask2);
        assertEquals(expectedHome, actualHome);

        // Work
        FilterPacket workPacket = (new FilterPacket())
                .withTaskList(taskList)
                .withTaskContext(TaskContext.WORK);

        List<Task> actualWork = TaskListFilter.from(workPacket).filter();
        List<Task> expectedWork = List.of(workTask1, workTask2);
        assertEquals(expectedWork, actualWork);

        // School
        FilterPacket schoolPacket = (new FilterPacket())
                .withTaskList(taskList)
                .withTaskContext(TaskContext.SCHOOL);

        List<Task> actualSchool = TaskListFilter.from(schoolPacket).filter();
        List<Task> expectedSchool = List.of();
        assertEquals(expectedSchool, actualSchool);
    }

    // Testing task creation with HOME context
    @Test
    public void homeContextTest() {
        Task task = TaskBuilder.from(10)
                .describe("Home task example")
                .createOn(LocalDate.now())
                .schedule(TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.HOME)
                .build();

        assertEquals("Task context for a home-related task should be correctly assigned",
                TaskContext.HOME, task.getTaskContext());
    }

    // Testing task creation with SCHOOL context
    @Test
    public void schoolContextTest() {
        Task task = TaskBuilder.from(20)
                .describe("School task example")
                .createOn(LocalDate.now())
                .schedule(TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.SCHOOL)
                .build();

        assertEquals("Task context for a school-related task should be correctly assigned",
                TaskContext.SCHOOL, task.getTaskContext());
    }

    // Testing task creation with ERRANDS context
    @Test
    public void errandContextTest() {
        Task task = TaskBuilder.from(30)
                .describe("Errands task example")
                .createOn(LocalDate.now())
                .schedule(TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.ERRAND)
                .build();

        assertEquals("Task context for an errands-related task should be correctly assigned",
                TaskContext.ERRAND, task.getTaskContext());
    }

    // Testing task creation with WORK context
    @Test
    public void workContextTest() {
        Task task = TaskBuilder.from(40)
                .describe("Work task example")
                .createOn(LocalDate.now())
                .schedule(TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.WORK)
                .build();

        assertEquals("Task context for a work-related task should be correctly assigned",
                TaskContext.WORK, task.getTaskContext());
    }
}
