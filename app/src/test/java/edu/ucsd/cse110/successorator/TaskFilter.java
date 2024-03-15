package edu.ucsd.cse110.successorator;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.util.FilterPacket;
import edu.ucsd.cse110.successorator.util.TaskListFilter;

public class TaskFilter {
    static final List<Task> DEFAULT_TASKS = List.of(
        TaskBuilder.from(1).describe("Monthly task")
                .schedule(TaskRecurrence.MONTHLY)
                .completeOn(LocalDate.now().minusDays(1))
                .clarify(TaskContext.ERRAND)
                .build(),

        TaskBuilder.from(2).describe("One-time task (1)")
                .build(),

        TaskBuilder.from(3).describe("Weekly task")
                .schedule(TaskRecurrence.WEEKLY)
                .build(),

        TaskBuilder.from(4).describe("One-time task (2)")
                .clarify(TaskContext.WORK)
                .build(),

        TaskBuilder.from(5).describe("Daily task")
                .schedule(TaskRecurrence.DAILY)
                .clarify(TaskContext.WORK).build(),

        TaskBuilder.from(6).describe("Yearly task")
                .schedule(TaskRecurrence.YEARLY)
                .completeOn(LocalDate.now().minusWeeks(2))
                .clarify(TaskContext.SCHOOL)
                .build(),

        TaskBuilder.from(7).describe("Pending task")
                .createOn(null)
                .build()
    );

    // Specialized filters
    @Test
    public void filterSpecializedByContext() {
        List<Task> actual = TaskListFilter.filterByContext(DEFAULT_TASKS, TaskContext.WORK);
        List<Task> expected = List.of(DEFAULT_TASKS.get(3), DEFAULT_TASKS.get(4));
        assertEquals(actual, expected);
    }

    @Test
    public void filterSpecializedByPending() {
        List<Task> actual = TaskListFilter.filterPending(DEFAULT_TASKS);
        List<Task> expected = List.of(DEFAULT_TASKS.get(6));
        assertEquals(actual, expected);
    }

    @Test
    public void filterSpecializedRecurring() {
        List<Task> actual = TaskListFilter.filterRecurring(DEFAULT_TASKS);
        List<Task> expected = List.of(DEFAULT_TASKS.get(0), DEFAULT_TASKS.get(2), DEFAULT_TASKS.get(4), DEFAULT_TASKS.get(5));
        assertEquals(actual, expected);
    }

    // Checking that the task list can filter by a selected focus
    @Test
    public void filterByContextTest() {
        // Errand
        FilterPacket errandPacket = (new FilterPacket())
                .withTaskList(DEFAULT_TASKS)
                .withTaskContext(TaskContext.ERRAND);

        List<Task> actualErrand = TaskListFilter.from(errandPacket).filter();
        List<Task> expectedErrand = List.of();
        assertEquals(expectedErrand, actualErrand);

        // Home
        FilterPacket homePacket = (new FilterPacket())
                .withTaskList(DEFAULT_TASKS)
                .withTaskContext(TaskContext.HOME);

        List<Task> actualHome = TaskListFilter.from(homePacket).filter();
        List<Task> expectedHome = List.of(DEFAULT_TASKS.get(1), DEFAULT_TASKS.get(2));
        assertEquals(expectedHome, actualHome);

        // Work
        FilterPacket workPacket = (new FilterPacket())
                .withTaskList(DEFAULT_TASKS)
                .withTaskContext(TaskContext.WORK);

        List<Task> actualWork = TaskListFilter.from(workPacket).filter();
        List<Task> expectedWork = List.of(DEFAULT_TASKS.get(3), DEFAULT_TASKS.get(4));
        assertEquals(expectedWork, actualWork);

        // School
        FilterPacket schoolPacket = (new FilterPacket())
                .withTaskList(DEFAULT_TASKS)
                .withTaskContext(TaskContext.SCHOOL);

        List<Task> actualSchool = TaskListFilter.from(schoolPacket).filter();
        List<Task> expectedSchool = List.of();
        assertEquals(expectedSchool, actualSchool);
    }
}
