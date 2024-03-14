package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

// This file covers testing of the Task class and TaskBuilder utility
public class TaskAtomics {
    @Test
    public void isCompleteFalseTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").build();
        assertEquals(false, task.isCompleted());
    }

    @Test
    public void isCompleteTrueTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").completeOn(LocalDate.now()).build();
        assertEquals(true, task.isCompleted());
    }

    @Test
    public void descriptionTest() {
        Task task = TaskBuilder.from(1).describe("Task 1").build();

        var expected = "Task 1";
        var actual = task.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void idTest() {
        Task task = TaskBuilder.from(1).describe("One-time task").build();

        int expected = 1;
        int actual = task.id();
        assertEquals(expected, actual);
    }

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

    @Test
    public void dateTest() {
        LocalDate date = LocalDate.now();
        Task task = TaskBuilder.from(1).describe("One-time task").createOn(date).build();
        var actual = task.getDateCreated();
        assertEquals(date, actual);
    }

    @Test
    public void homeContextTest() {
        Task task = TaskBuilder.from(10)
                .describe("Home task example")
                .createOn(LocalDate.now())
                .schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.HOME)
                .build();

        assertEquals("Task context for a home-related task should be correctly assigned",
                TaskContext.HOME, task.getTaskContext());
    }

    @Test
    public void schoolContextTest() {
        Task task = TaskBuilder.from(20)
                .describe("School task example")
                .createOn(LocalDate.now())
                .schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.SCHOOL)
                .build();

        assertEquals("Task context for a school-related task should be correctly assigned",
                TaskContext.SCHOOL, task.getTaskContext());
    }

    @Test
    public void errandContextTest() {
        Task task = TaskBuilder.from(30)
                .describe("Errands task example")
                .createOn(LocalDate.now())
                .schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.ONE_TIME)
                .clarify(TaskContext.ERRAND)
                .build();

        assertEquals("Task context for an errands-related task should be correctly assigned",
                TaskContext.ERRAND, task.getTaskContext());
    }

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
