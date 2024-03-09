package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.TopBarFragment;

public class MainActivity extends AppCompatActivity {
    private TaskListFragment taskListFragment;
    LocalDate date;
    private MainViewModel mainViewModel;
    private TopBarFragment topBarFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        date = LocalDate.now();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        topBarFragment = TopBarFragment.newInstance(date);
        taskListFragment = TaskListFragment.newInstance(date);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_bar, topBarFragment)
                .replace(R.id.task_list, taskListFragment)
                .commit();
//        getSupportFragmentManager().beginTransaction().replace(R.id.task_list, taskListFragment).commit();
    }

//    public void onDateChanged(LocalDate newDate){
//        date = newDate;
//        passDateToTasks();
//    }

    public void onTopBarNextButtonClicked() {
        date = date.plusDays(1);
        mainViewModel.setCurrentDate(date);
        Log.i("date:", "tomo: " + date);
//        passDateToTasks();
        taskListFragment.setDate(date);
    }

//    public void onTopBarTodayButtonClicked() {
//        date = LocalDate.now();
//        mainViewModel.setCurrentDate(date);
//        Log.i("date:", "today: " + date);
////        passDateToTasks();
//        taskListFragment.setDate(date);
//    }

    private void passDateToTasks() {
        taskListFragment = TaskListFragment.newInstance(date);
        getSupportFragmentManager().beginTransaction().replace(R.id.task_list, taskListFragment).commit();

        if (taskListFragment != null) {
            taskListFragment.setDate(date);
        }
    }

}