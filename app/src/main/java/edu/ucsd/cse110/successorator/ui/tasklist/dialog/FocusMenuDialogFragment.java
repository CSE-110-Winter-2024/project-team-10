package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusMenuBinding;
import edu.ucsd.cse110.successorator.lib.domain.Task;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.ui.tasklist.TaskListFragment;

public class FocusMenuDialogFragment extends DialogFragment {
    MainViewModel activityModel;
    @NonNull FragmentFocusMenuBinding view;

    private MutableLiveData<TaskContext> taskContextLiveData = new MutableLiveData<>();

    private Subject<List<Task>> TaskListSubject;
    private TaskListFragment taskListFragment;
    private FragmentActivity modelOwner;
    private ViewModelProvider.Factory modelFactory;
    private ViewModelProvider modelProvider;

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

        this.modelOwner = requireActivity();
        this.modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        this.modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
        taskContextLiveData.setValue(TaskContext.HOME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        this.view = FragmentFocusMenuBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();

        // Set OnClickListener for each TextView
        setupTextViewClickListeners();
        TaskListSubject = activityModel.getTaskListSubject();

        return alertDialog;
    }

    private void setupTextViewClickListeners() {
        TextView homeFocus = view.getRoot().findViewById(R.id.home_focus);
        TextView workFocus = view.getRoot().findViewById(R.id.work_focus);
        TextView schoolFocus = view.getRoot().findViewById(R.id.school_focus);
        TextView errandFocus = view.getRoot().findViewById(R.id.errand_focus);
        TextView cancelFocus = view.getRoot().findViewById(R.id.cancel_focus);

        homeFocus.setOnClickListener(this::onHomeFocusClick);
        workFocus.setOnClickListener(this::onWorkFocusClick);
        schoolFocus.setOnClickListener(this::onSchoolFocusClick);
        errandFocus.setOnClickListener(this::onErrandFocusClick);
        cancelFocus.setOnClickListener(this::onCancelFocusClick);
    }

    private void onHomeFocusClick(View v) {
        // Handle click for homeFocus
        taskContextLiveData.setValue(TaskContext.HOME);
        setFragmentListTaskContext();
        getDialog().dismiss();
    }

    private void onWorkFocusClick(View v) {
        // Handle click for workFocus
        taskContextLiveData.setValue(TaskContext.WORK);
        setFragmentListTaskContext();
        getDialog().dismiss();
    }

    private void onSchoolFocusClick(View v) {
        // Handle click for schoolFocus
        taskContextLiveData.setValue(TaskContext.SCHOOL);
        setFragmentListTaskContext();
        getDialog().dismiss();
    }

    private void onErrandFocusClick(View v) {
        // Handle click for errandFocus
        taskContextLiveData.setValue(TaskContext.ERRAND);
        setFragmentListTaskContext();
        getDialog().dismiss();
    }

    private void onCancelFocusClick(View v) {
        // Handle click for cancelFocus
        taskContextLiveData.setValue(null);
        setFragmentListTaskContext();
        getDialog().dismiss();
    }

    private void setFragmentListTaskContext() {
        TaskListFragment.setTaskContextMutableLiveData(taskContextLiveData.getValue());
    }
}
