package edu.ucsd.cse110.successorator;

import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import edu.ucsd.cse110.successorator.data.db.TaskDao;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.util.DueDateHandler;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class MS2_US3_UnitTests {
    private final List<Task> taskList = new ArrayList<>();

    @Test
    public void setDueDateTest() {
        DueDateHandler dueDateHandler = new DueDateHandler();
        dueDateHandler.setDueDate(LocalDate.now());
        LocalDate handlerNow = dueDateHandler.getDueDate();
        LocalDate now = LocalDate.now();
        assertEquals(now, handlerNow);
    }

    //Long Press Move to Today
    @Test
    public void moveToTodayTest() {
        Task task1 = new Task(1, "Buy groceries", new Date(), false, LocalDate.now());
        Task task2 = new Task(500, "Buy groceries", new Date(), false, LocalDate.now().plusDays(5));

        DueDateHandler dueDateHandler = new DueDateHandler();
        dueDateHandler.moveTaskToToday(task2);

        assertEquals(task1.due(), task2.due());
    }

    //Long Press Move to Tomorrow
    @Test
    public void moveToTomorrowTest() {
        Task task1 = new Task(1, "Buy groceries", new Date(), false, LocalDate.now().plusDays(1));
        Task task2 = new Task(500, "Buy groceries", new Date(), false, LocalDate.now());


        DueDateHandler dueDateHandler = new DueDateHandler();
        dueDateHandler.moveTaskToTomorrow(task2);

        assertEquals(task1.due(), task2.due());
    }

    // tests starting current date, then long press "move to tomorrow",
    // then one day passes, then task should be in "move to today"
    @Test
    public void moveToTomorrowThenTodayTest() {

    }

    //Long Press Finish
    @Test
    public void FinishTaskTest() {
        Task task = new Task(1, "Buy groceries", new Date(), false, LocalDate.now());
        task.setCompleted(true);
        assertEquals(true, task.isCompleted());

    }

    //Long Press Delete
    @Test
    public void DeleteTaskTest() {
        Task task1 = new Task(1, "Buy groceries", new Date(), false, LocalDate.now());
        Task task2 = new Task(1, "Buy groceries", new Date(), false, LocalDate.now());
        taskList.add(task1);
        taskList.remove(task1);
    }
}
