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

    private void onPositiveButtonClick(@NonNull DialogInterface dialog, int which) {
        activityModel.createTask(view.descriptionText.getText().toString());
        dialog.dismiss();
    }
    private void onNegativeButtonClick(@NonNull DialogInterface dialog, int which) {
        dialog.cancel();
    }
}