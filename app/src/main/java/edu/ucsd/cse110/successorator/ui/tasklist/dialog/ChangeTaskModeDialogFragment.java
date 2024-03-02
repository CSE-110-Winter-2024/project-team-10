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
    private Consumer<Integer> onTaskClick;

    public ChangeTaskModeDialogFragment(Task task, Consumer<Integer> onTaskClick) {
        this.task = task;
        this.onTaskClick = onTaskClick;
    }

    public static ChangeTaskModeDialogFragment newInstance(Task task, Consumer<Integer> onTaskClick) {
        ChangeTaskModeDialogFragment fragment = new ChangeTaskModeDialogFragment(task, onTaskClick);
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
        this.view = FragmentChangeTaskmodeDialogBinding.inflate(getLayoutInflater());
        View dialogView = view.getRoot();
        Button moveToTodayButton = dialogView.findViewById(R.id.move_to_today);
        Button  moveToTomorrowButton = dialogView.findViewById(R.id.move_to_tomorrow);
        Button  finishButton = dialogView.findViewById(R.id.finish);
        Button  deleteButton = dialogView.findViewById(R.id.delete);


        moveToTodayButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Move to Today clicked");
            dismiss();
        });

        moveToTomorrowButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Move to Tomorrow clicked");
            dismiss();
        });

        finishButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Finish clicked");
            if (task.isCompleted() == false) {
                onTaskClick.accept(task.id());
            }
            dismiss();
        });

        deleteButton.setOnClickListener(v -> {
            Log.i("TextView Click", "Delete clicked");
            dismiss();
        });

//        moveToTodayButton.setEnabled(task.due() == LocalDate.now());
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
    private void onOkayButtonClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}
