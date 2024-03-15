package edu.ucsd.cse110.successorator;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskBuilder;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;
import edu.ucsd.cse110.successorator.util.TaskListFilter;

// Tests the first of the "Scenario Based Milestone Tests"
// Target User Stories: US1, US3, US5
// SBMT 1: Task Scheduling Options, Long Press & Task Context
public class SBMT1 {
    List<Task> allTasks = new ArrayList<Task>();
    @Test
    public void SBMT1Test() {
        // Start the app by tapping its icon.
        // Further progress into the app’s UI by tapping the ‘+’ icon.
        // The task creation menu should display the text input box, the contexts,
        // and the scheduling options (US 1 & US 5).
        // Input a task name and select the “Daily” scheduling option.
        // Tap “Add Task.” The new task should appear in the task list.
        // Complete the task and go to the next day. The task will be marked as incomplete.
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
        assertTrue(allTasks.get(0).isCompleted());

        // Create ONE more task with the “Weekly” scheduling option.
        // Advance THREE days by clicking the next day button THREE times.
        // Mark the “Weekly” task complete. Go to the next day by clicking the
        // next day button ONE time.
        // The task will not be displayed in the completed list.
        allTasks.remove(0);
        Task weeklyTask = TaskBuilder.from(2).describe("Weekly task")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.HOME)
                .build();
        allTasks.add(weeklyTask);
        assertFalse(allTasks.get(0).isCompleted());

        weeklyTask = TaskBuilder.from(2).describe("Daily task")
                .schedule(TaskRecurrence.DAILY)
                .completeOn(LocalDate.now())
                .clarify(TaskContext.HOME)
                .build();
        allTasks.set(0, weeklyTask);
        assertTrue(allTasks.get(0).isCompleted());

        // Create a task.
        // Do a long press on the task. A menu will pop up with the options
        // "Move to Today", “Move to Tomorrow”, “Finish”, and “Delete”. (US 3)
        // Select the “Finish” option and the task will be moved down the list,
        // grayed out and striked-through.
        allTasks.remove(0);
        Task finishTask = TaskBuilder.from(3).describe("Finish task")
                .schedule(TaskRecurrence.WEEKLY)
                .completeOn(LocalDate.now())
                .clarify(TaskContext.HOME)
                .build();
        allTasks.add(finishTask);
        assertTrue(allTasks.get(0).isCompleted());

        // Do another long press on the task.
        // A menu will pop up with the options "Move to Today", “Move to Tomorrow”,
        // “Finish”, and “Delete”.
        // Select the “Delete” option and the task will be permanently deleted.
        // This task will no longer show up on the screen.
        allTasks.remove(0);
        //assertEquals(null, allTasks.get(0));


        // Create TWO tasks, selecting the “School” context for each.
        // Create TWO tasks, selecting the “Home” context.
        // Create ONE task, selecting the “School” context.
        // The “School” tasks should be grouped together, and the “Home” context
        // should be grouped separately, irrespective of the sequence of creation. (US 5)
        Task schoolTask1 = TaskBuilder.from(2).describe("School task 1")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.SCHOOL)
                .build();
        Task schoolTask2 = TaskBuilder.from(2).describe("School task 2")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.SCHOOL)
                .build();
        Task homeTask1 = TaskBuilder.from(2).describe("Home task 1")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.HOME)
                .build();
        Task homeTask2 = TaskBuilder.from(2).describe("Home task 2")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.HOME)
                .build();
        Task schoolTask3 = TaskBuilder.from(2).describe("Other school task")
                .schedule(TaskRecurrence.WEEKLY)
                .clarify(TaskContext.SCHOOL)
                .build();

        allTasks = List.of(schoolTask1,schoolTask2, homeTask1, homeTask2, schoolTask3);

        List<Task> schoolTasks = List.of(schoolTask1, schoolTask2, schoolTask3);
        List<Task> homeTasks = List.of(homeTask1, homeTask2);

        assertEquals(schoolTasks, TaskListFilter.filterByContext(allTasks, TaskContext.SCHOOL));
        assertEquals(homeTasks, TaskListFilter.filterByContext(allTasks, TaskContext.HOME));

    }


        // Exit the app. This SBMT has now been completed successfully.

}
