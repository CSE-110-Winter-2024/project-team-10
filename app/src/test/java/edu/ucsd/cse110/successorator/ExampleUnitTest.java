package edu.ucsd.cse110.successorator;

import org.junit.Test;

import static org.junit.Assert.*;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.room.Room;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.ucsd.cse110.successorator.data.db.RoomTaskRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    //Tests isComplete()
    @Test
    public void isComplete_isCorrectFalse() {
        Task task = new Task(1, "Task 1", new Date(), false);

        var expected = false;
        var actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    //Tests isComplete()
    @Test
    public void isComplete_isCorrectTrue() {
        Task task = new Task(1, "Task 1", new Date(), true);

        var expected = true;
        var actual = task.isCompleted();
        assertEquals(expected, actual);
    }

    //Tests task description
    @Test
    public void description_isCorrect() {
        Task task = new Task(1, "Task 1", new Date(), true);

        var expected = "Task 1";
        var actual = task.getDescription();
        assertEquals(expected, actual);
    }

    //Tests task id
    @Test
    public void id_isCorrectTrue() {
        Task task = new Task(1, "Task 1", new Date(), true);

        int expected = 1;
        int actual = task.id();
        assertEquals(expected, actual);
    }

    //Tests task date
    @Test
    public void date_isCorrectTrue() {
        Date date = new Date();
        Task task = new Task(1, "Task 1", date, true);

        var expected = date;
        var actual = task.getDateCreated();
        assertEquals(expected, actual);
    }


}

