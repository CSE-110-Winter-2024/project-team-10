package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentChangeTaskmodeDialogBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRepository;

public class ChangeTaskModeDialogFragment extends DialogFragment {
    MainViewModel activityModel;
    FragmentChangeTaskmodeDialogBinding view;

    private Task task;
    private Consumer<Integer> onTaskClickComplete;
    private Consumer<Integer> onTaskPressDelete;
    private Consumer<Integer> onTaskPressMoveToToday;
    private Consumer<Integer> onTaskPressMoveToTomorrow;
    private Consumer<Integer> onTaskPressMoveSingleTaskToTomorrow;


    public ChangeTaskModeDialogFragment(Task task,
                                        Consumer<Integer> onTaskClickComplete,
                                        Consumer<Integer> onTaskPressDelete,
                                        Consumer<Integer> onTaskPressMoveToToday,
                                        Consumer<Integer> onTaskPressMoveToTomorrow,
                                        Consumer<Integer> onTaskPressMoveSingleTaskToTomorrow) {
        this.task = task;
        this.onTaskClickComplete = onTaskClickComplete;
        this.onTaskPressDelete = onTaskPressDelete;
        this.onTaskPressMoveToToday = onTaskPressMoveToToday;
        this.onTaskPressMoveToTomorrow = onTaskPressMoveToTomorrow;
        this.onTaskPressMoveSingleTaskToTomorrow = onTaskPressMoveSingleTaskToTomorrow;
    }

    public static ChangeTaskModeDialogFragment newInstance(
            Task task,
            Consumer<Integer> onTaskClick,
            Consumer<Integer> onTaskPressDelete,
            Consumer<Integer> onTaskPressMoveToToday,
            Consumer<Integer> onTaskPressMoveToTomorrow,
            Consumer<Integer> onTaskPressMoveSingleTaskToTomorrow) {
        ChangeTaskModeDialogFragment fragment = new ChangeTaskModeDialogFragment(
                task,
                onTaskClick,
                onTaskPressDelete,
                onTaskPressMoveToToday,
                onTaskPressMoveToTomorrow,
                onTaskPressMoveSingleTaskToTomorrow);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }


    //Long Press a Task: so that I can move it to today, tomorrow, finish, or delete it.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentChangeTaskmodeDialogBinding.inflate(getLayoutInflater());
        View dialogView = view.getRoot();
        Button moveToTodayButton = dialogView.findViewById(R.id.move_to_today);
        Button  moveToTomorrowButton = dialogView.findViewById(R.id.move_to_tomorrow);
        Button  finishButton = dialogView.findViewById(R.id.finish);
        Button  deleteButton = dialogView.findViewById(R.id.delete);


        moveToTodayButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Move to Today clicked");
            onTaskPressMoveToToday.accept(task.id());
            dismiss();
        });

        moveToTomorrowButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Move to Tomorrow clicked");
            onTaskPressMoveSingleTaskToTomorrow.accept(task.id());
            dismiss();
        });

        finishButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Finish clicked");
            if (task.isCompleted() == false) {
                onTaskClickComplete.accept(task.id());
            }
            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Delete clicked");
            onTaskPressDelete.accept(task.id());
            dismiss();
        });

        //moveToTodayButton.setEnabled(task.due() == LocalDate.now());
        //moveToTomorrowButton.setEnabled(task.due() == (LocalDate.now() + 1));
        finishButton.setEnabled(task.isCompleted() == false);

        return new AlertDialog.Builder(getActivity())
                .setTitle(task.getDescription())
                .setMessage("What would you like to do?")
                .setView(dialogView)
                .setNegativeButton("Cancel", this::onCancelButtonClick)
                .create();
    }

    private void onCancelButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}