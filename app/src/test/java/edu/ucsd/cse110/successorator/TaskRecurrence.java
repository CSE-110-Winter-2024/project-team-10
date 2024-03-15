package edu.ucsd.cse110.successorator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.LocalDate;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;

// This file covers testing of task recurrence
public class TaskRecurrence {
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

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.DAILY).build();
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

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.WEEKLY).build();
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

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.MONTHLY).build();
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

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.YEARLY).build();
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedSameYear);
        assertEquals(date, task.getDateCreated());

        task.refreshDates(movedNextYear);
        assertEquals(dateNextYear, task.getDateCreated());
    }
}