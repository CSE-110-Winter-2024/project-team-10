package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentCreateTaskDialogBinding;

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

    private void onPositiveButtonClick(DialogInterface dialog, int which) { // change this to receive the current day
        // Get the ID of the selected radio button in the RadioGroup
        int selectedRadioButtonId = view.repeatOptions.getCheckedRadioButtonId();

        // Get the selected RadioButton
        RadioButton selectedRadioButton = view.getRoot().findViewById(selectedRadioButtonId);
        String repeatOption = selectedRadioButton.getText().toString();
        Date repeatDate = new Date(); // Make this the selected day

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(repeatDate);

        switch (repeatOption.toLowerCase()) {
            case "daily":
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case "weekly":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "yearly":
                calendar.add(Calendar.YEAR, 1);
                break;
            case "none":
            default:
                break;
        }
        repeatDate = calendar.getTime();

        // Now you can use the text of the selected RadioButton as needed
        activityModel.createTask(view.descriptionText.getText().toString(), repeatDate);
        dialog.dismiss();
    }
    private void onNegativeButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}