package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentCreateTaskDialogBinding;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.domain.TaskRecurrence;

public class CreateTaskDialogFragment extends DialogFragment {
    MainViewModel activityModel;
    FragmentCreateTaskDialogBinding view;

    public CreateTaskDialogFragment() {}

    public static CreateTaskDialogFragment newInstance() {
        CreateTaskDialogFragment fragment = new CreateTaskDialogFragment();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentCreateTaskDialogBinding.inflate(getLayoutInflater());
        return new AlertDialog.Builder(getActivity())
                .setTitle("Create Task")
                .setView(view.getRoot())
                .setPositiveButton("Add Task", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which) {
        String description = view.descriptionText.getText().toString();

        int recurrenceSelected = view.recurrenceButtons.getCheckedRadioButtonId();
        TaskRecurrence recurrence;

        int contextSelected = view.contextButtons.getCheckedRadioButtonId();
        TaskContext context;

        if (recurrenceSelected == view.onetimeTask.getId()) {
            recurrence = TaskRecurrence.ONE_TIME;
        } else if (recurrenceSelected == view.dailyTask.getId()) {
            recurrence = TaskRecurrence.DAILY;
        } else if (recurrenceSelected == view.weeklyTask.getId()) {
            recurrence = TaskRecurrence.WEEKLY;
        } else if (recurrenceSelected == view.monthlyTask.getId()) {
            recurrence = TaskRecurrence.MONTHLY;
        } else if (recurrenceSelected == view.yearlyTask.getId()) {
            recurrence = TaskRecurrence.YEARLY;
        } else {
            throw new IllegalArgumentException();
        }

        if (contextSelected == view.homeContext.getId()) {
            context = TaskContext.HOME;
        } else if (contextSelected == view.workContext.getId()) {
            context = TaskContext.WORK;
        } else if (contextSelected == view.schoolContext.getId()) {
            context = TaskContext.SCHOOL;
        } else if (contextSelected == view.errandsContext.getId()) {
            context = TaskContext.ERRAND;
        } else {
            throw new IllegalArgumentException();
        }

        activityModel.createTask(description, recurrence, context);
        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}