package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusMenuBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class FocusMenuDialogFragment extends DialogFragment {
    MainViewModel activityModel;
    @NonNull FragmentFocusMenuBinding view;

    private MutableLiveData<TaskContext> taskContextLiveData;

    private Subject<List<Task>> TaskListSubject;

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
        this.activityModel = modelProvider.get(MainViewModel.class);
        taskContextLiveData = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusMenuBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//                .setTitle(".")
                .setView(view.getRoot())
//                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();

        // Set OnClickListener for each TextView
        setupTextViewClickListeners();
        TaskListSubject = activityModel.getTaskListSubject();

//        // Observe changes in the task context
//        taskContextLiveData.observe(getViewLifecycleOwner(), taskContext -> {
//            // Handle task context changes here
//        });

        return alertDialog;
    }

    private void setupTextViewClickListeners() {
        TextView homeFocus = view.getRoot().findViewById(R.id.home_focus);
        TextView workFocus = view.getRoot().findViewById(R.id.work_focus);
        TextView schoolFocus = view.getRoot().findViewById(R.id.school_focus);
        TextView errandFocus = view.getRoot().findViewById(R.id.errand_focus);
        TextView cancelFocus = view.getRoot().findViewById(R.id.cancel_focus);

        // Set OnClickListener for each TextView
        homeFocus.setOnClickListener(this::onHomeFocusClick);
        workFocus.setOnClickListener(this::onWorkFocusClick);
        schoolFocus.setOnClickListener(this::onSchoolFocusClick);
        errandFocus.setOnClickListener(this::onErrandFocusClick);
        cancelFocus.setOnClickListener(this::onCancelFocusClick);
    }
//
//    private void onPositiveButtonClick(@NonNull DialogInterface dialog, int which) {
//        activityModel.createTask(view.descriptionText.getText().toString());
//        dialog.dismiss();
//    }

    private void onHomeFocusClick(View v) {
        // Handle click for homeFocus
        taskContextLiveData.setValue(TaskContext.HOME);;
        getDialog().dismiss();
    }

    private void onWorkFocusClick(View v) {
        // Handle click for workFocus
        taskContextLiveData = new MutableLiveData<>(TaskContext.WORK);
        getDialog().dismiss();
    }

    private void onSchoolFocusClick(View v) {
        // Handle click for schoolFocus
        taskContextLiveData.setValue(TaskContext.SCHOOL);
        getDialog().dismiss();
    }

    private void onErrandFocusClick(View v) {
        // Handle click for errandFocus
        taskContextLiveData.setValue(TaskContext.ERRAND);
        getDialog().dismiss();
    }

    private void onCancelFocusClick(View v) {
        // Handle click for cancelFocus
        taskContextLiveData.setValue(null);
        getDialog().dismiss();
    }

    public MutableLiveData<TaskContext> getTaskContextLiveData() {
        return taskContextLiveData;
    }

    public Subject<List<Task>> getTaskListSubject() {
        return TaskListSubject;
    }


//    private void onNegativeButtonClick(@NonNull DialogInterface dialog, int which) {
//        dialog.cancel();
//    }

//    private Observer<TaskContext> taskContextObserver = new Observer<TaskContext>() {
//        @Override
//        public void onChanged(TaskContext taskContext) {
//            //observe the task context variable here
//        }
//    };

}
