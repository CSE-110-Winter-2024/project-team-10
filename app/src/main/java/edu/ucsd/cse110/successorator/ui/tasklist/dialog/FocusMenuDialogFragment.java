package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusMenuBinding;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

public class FocusMenuDialogFragment extends DialogFragment {
    private MainViewModel activityModel;

    public FocusMenuDialogFragment() {}

    public static FocusMenuDialogFragment newInstance() {
        FocusMenuDialogFragment fragment = new FocusMenuDialogFragment();
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

        activityModel = modelProvider.get(MainViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        var view = FragmentFocusMenuBinding.inflate(getLayoutInflater());

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onCancelButtonClick)
                .create();

        // Set OnClickListener for each TextView
        TextView homeFocus = view.getRoot().findViewById(R.id.home_focus);
        TextView workFocus = view.getRoot().findViewById(R.id.work_focus);
        TextView schoolFocus = view.getRoot().findViewById(R.id.school_focus);
        TextView errandFocus = view.getRoot().findViewById(R.id.errand_focus);

        homeFocus.setOnClickListener(v -> {
            activityModel.registerTaskContext(TaskContext.HOME);
            dialog.dismiss();
        });

        workFocus.setOnClickListener(v -> {
            activityModel.registerTaskContext(TaskContext.WORK);
            dialog.dismiss();
        });

        schoolFocus.setOnClickListener(v -> {
            activityModel.registerTaskContext(TaskContext.SCHOOL);
            dialog.dismiss();
        });

        errandFocus.setOnClickListener(v -> {
            activityModel.registerTaskContext(TaskContext.ERRAND);
            dialog.dismiss();
        });

        return dialog;
    }

    private void onCancelButtonClick(DialogInterface dialog, int which) {
        activityModel.registerTaskContext(null);
        dialog.cancel();
    }
}
