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

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentChangeTaskmodeDialogBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

public class ChangeTaskModeDialogFragment extends DialogFragment {
    private final Task task;

    public ChangeTaskModeDialogFragment(Task task) {
            this.task = task;
    }

    public static ChangeTaskModeDialogFragment newInstance(Task task) {
        ChangeTaskModeDialogFragment fragment = new ChangeTaskModeDialogFragment(task);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        var view = FragmentChangeTaskmodeDialogBinding.inflate(getLayoutInflater());

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        var activityModel = modelProvider.get(MainViewModel.class);

        view.moveToToday.setOnClickListener(v -> {
            activityModel.changeTaskDateToday(task);
            dismiss();
        });

        view.moveToTomorrow.setOnClickListener(v -> {
            activityModel.changeTaskDateTomorrow(task);
            dismiss();
        });

        view.finish.setOnClickListener(v -> {
            activityModel.completeTask(task);
            dismiss();
        });

        view.delete.setOnClickListener(v -> {
            activityModel.removeTask(task);
            dismiss();
        });

        // Only enable moving dates if it a one time task
        view.moveToToday.setEnabled(task.getTaskRecurrence() == TaskRecurrence.ONE_TIME);
        view.moveToTomorrow.setEnabled(task.getTaskRecurrence() == TaskRecurrence.ONE_TIME);

        // Only enable completing a task if it is not already completed
        view.finish.setEnabled(!task.isCompleted());

        return new AlertDialog.Builder(getActivity())
                .setTitle(task.getDescription())
                .setMessage("What would you like to do?")
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onCancelButtonClick)
                .create();
    }

    private void onCancelButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}