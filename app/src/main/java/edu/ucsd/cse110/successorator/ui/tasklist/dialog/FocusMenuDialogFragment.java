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
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;

public class FocusMenuDialogFragment extends DialogFragment {
    MainViewModel activityModel;


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

        activityModel.registerTaskContext(TaskContext.HOME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        var view = FragmentFocusMenuBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view.getRoot())
                .create();

        // Set OnClickListener for each TextView
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

        return alertDialog;
    }

    private void onHomeFocusClick(View v) {
        // Handle click for homeFocus
        activityModel.registerTaskContext(TaskContext.HOME);
        getDialog().dismiss();
    }

    private void onWorkFocusClick(View v) {
        // Handle click for workFocus
        activityModel.registerTaskContext(TaskContext.WORK);
        getDialog().dismiss();
    }

    private void onSchoolFocusClick(View v) {
        // Handle click for schoolFocus
        activityModel.registerTaskContext(TaskContext.SCHOOL);
        getDialog().dismiss();
    }

    private void onErrandFocusClick(View v) {
        // Handle click for errandFocus
        activityModel.registerTaskContext(TaskContext.ERRAND);
        getDialog().dismiss();
    }

    private void onCancelFocusClick(View v) {
        // Handle click for cancelFocus
        activityModel.registerTaskContext(null);
        getDialog().dismiss();
    }
}
