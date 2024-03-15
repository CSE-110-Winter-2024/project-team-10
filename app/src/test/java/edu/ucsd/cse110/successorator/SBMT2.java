package edu.ucsd.cse110.successorator;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.util.FilterPacket;
import edu.ucsd.cse110.successorator.util.TaskListFilter;

// Tests the second of the "Scenario Based Milestone Tests"
// Target User Stories: US2, US4, US6, US7
// SBMT 2: Swap View Mode, Hamburger Menu, Focus Mode, & Schedule Task Recurrence
public class SBMT2 {
    @Test
    public void SBMT2Test() {
        // Start the app by tapping its icon.
        List<Task> allTasks = new ArrayList<Task>();

        // Tap the ‘v’ dropdown button next to the date to reveal the options ‘today’,
        // ‘tomorrow’, ‘pending’ and ‘recurring’. (US 2)
        // Select ‘tomorrow’ to filter out the tasks that are completed and due on the
        // ‘current’ calendar date.
        // Since no tasks have been created yet, the screen should remain blank.
        assertEquals(allTasks, TaskListFilter.filterByDate(allTasks, LocalDate.now()));

        // Create a task.
        // Return to the current calendar day by tapping the ‘v’ button and ‘today.’
        // Mark the task as completed and select “Tomorrow” using the dropdown button.
        // The task should remain in the completed list.
        Task dailyTask = TaskBuilder.from(1).describe("Daily task")
                .schedule(TaskRecurrence.DAILY)
                .clarify(TaskContext.HOME)
                .build();
        allTasks.add(dailyTask);
        assertFalse(allTasks.get(0).isCompleted());

        dailyTask = TaskBuilder.from(1).describe("Daily task")
                .schedule(TaskRecurrence.DAILY)
                .completeOn(LocalDate.now())
                .clarify(TaskContext.HOME)
                .build();
        allTasks.set(0, dailyTask);
        assertTrue(allTasks.get(0).isCompleted()); //task is completed

        List<Task> tomorrowTasks = List.of();//Expected

        assertEquals(tomorrowTasks, TaskListFilter.filterByDate(allTasks, LocalDate.now().plusDays(1)));

        // Tap the hamburger menu icon on the main screen.
        // A dropdown should appear, displaying different focus groups
        // along with a cancel option. (US 4)
        // Select any focus group to filter tasks by that category.
        // Since no tasks are created yet, the screen remains blank.
        List<Task> workTasks = List.of();//Expected
        assertEquals(workTasks, TaskListFilter.filterByContext(allTasks, TaskContext.WORK));

        // Tap the hamburger menu again and select "cancel" to close the dropdown
        // and return to the main screen. (US 4)
        // Tap the '+' icon to create a new task.
        // Enter a task name and select a repetition option (daily, weekly, monthly, yearly)
        // from the menu, and select the “School” context.
        // Create another task, selecting the “Home” context.
        // Tap the hamburger menu and select “School.”
        // There should only be a single task displayed due to the focus
        // on the “School” context. (US 6)
        Task schoolTask = TaskBuilder.from(2).describe("School task")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.SCHOOL)
                .build();
        Task homeTask = TaskBuilder.from(2).describe("Home task")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.HOME)
                .build();
        allTasks.add(1, schoolTask);
        allTasks.add(2, homeTask);

        List<Task> schoolTasks = List.of(schoolTask);//Expected

        assertEquals(schoolTasks, TaskListFilter.filterByContext(allTasks, TaskContext.SCHOOL));



        // Tap the dropdown menu and select the “recurring” option.
        // Fill in the text field and select the “Weekly” option.
        // Also click the date at the bottom and select March 1st. Create the task. (US 7)
        // The task should not be visible on the current date (assuming it is prior to March 1st).
        // Tap until March 1st is displayed as the date. The task should appear. (US 7)
        // Mark the task as complete. Move forward another SEVEN days. The task should reappear in the completed list. (US 7)
        LocalDate date = LocalDate.now();
        LocalDate dateNextWeek = LocalDate.now().plusWeeks(1);
        LocalDate movedSameWeek = date.plusDays(5);
        LocalDate movedNextWeek = date.plusDays(9);

        Task task = TaskBuilder.from(1).describe("Task 1").createOn(date).schedule(edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence.WEEKLY).build();
        Assert.assertEquals(date, task.getDateCreated());

        task.refreshDates(movedSameWeek);
        Assert.assertEquals(date, task.getDateCreated());

        task.refreshDates(movedNextWeek);
        Assert.assertEquals(dateNextWeek, task.getDateCreated());
    }



    // Exit the app. This SBMT has now been completed successfully.
}

