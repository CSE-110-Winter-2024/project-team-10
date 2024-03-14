package edu.ucsd.cse110.successorator;

// Tests the second of the "Scenario Based Milestone Tests"
// Target User Stories: US2, US4, US6, US7
// SBMT 2: Swap View Mode, Hamburger Menu, Focus Mode, & Schedule Task Recurrence
public class SBMT2 {
    // Start the app by tapping its icon.
    // Tap the ‘v’ dropdown button next to the date to reveal the options ‘today’,
    // ‘tomorrow’, ‘pending’ and ‘recurring’. (US 2)
    // Select ‘tomorrow’ to filter out the tasks that are completed and due on the
    // ‘current’ calendar date.
    // Since no tasks have been created yet, the screen should remain blank.


    // Create a task.
    // Return to the current calendar day by tapping the ‘v’ button and ‘today.’
    // Mark the task as completed and select “Tomorrow” using the dropdown button.
    // The task should remain in the completed list.


    // Tap the hamburger menu icon on the main screen.
    // A dropdown should appear, displaying different focus groups
    // along with a cancel option. (US 4)
    // Select any focus group to filter tasks by that category.
    // Since no tasks are created yet, the screen remains blank.


    // Tap the hamburger menu again and select "cancel" to close the dropdown
    // and return to the main screen. (US 4)


    // Tap the '+' icon to create a new task.
    // Enter a task name and select a repetition option (daily, weekly, monthly, yearly)
    // from the menu, and select the “School” context.
    // Create another task, selecting the “Home” context.
    // Tap the hamburger menu and select “School.”
    // There should only be a single task displayed due to the focus
    // on the “School” context. (US 6)


    // Tap the dropdown menu and select the “recurring” option.
    // Fill in the text field and select the “Weekly” option.
    // Also click the date at the bottom and select March 1st. Create the task. (US 7)
    // The task should not be visible on the current date (assuming it is prior to March 1st).


    // Tap until March 1st is displayed as the date. The task should appear. (US 7)


    // Mark the task as complete. Move forward another SEVEN days. The task should reappear in the completed list. (US 7)



    // Exit the app. This SBMT has now been completed successfully.
}

