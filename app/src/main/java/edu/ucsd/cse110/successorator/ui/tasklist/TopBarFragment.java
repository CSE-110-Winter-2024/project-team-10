package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTopBarBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.FocusMenuDialogFragment;

public class TopBarFragment extends Fragment {

    private FragmentActivity modelOwner;
    private ViewModelProvider.Factory modelFactory;
    private ViewModelProvider modelProvider;
    private MainViewModel activityModel;
    private DateTimeFormatter dateTimeFormatter;

    public TopBarFragment() {}

    public static TopBarFragment newInstance() {
        TopBarFragment fragment = new TopBarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var view = FragmentTopBarBinding.inflate(inflater, container, false);

        // Obtain the main view model
        modelOwner = requireActivity();
        modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        modelProvider = new ViewModelProvider(modelOwner, modelFactory);

        activityModel = modelProvider.get(MainViewModel.class);

        // Make a date formatter
        dateTimeFormatter = DateTimeFormatter.ofPattern("EE, MMM dd", Locale.ENGLISH);

        // Update the date text whenever the date changes
        activityModel.getCurrentDateSubject().observe(v -> {
            view.dateText.setText(v.format(dateTimeFormatter));
        });

        // Button handlers
        view.nextButton.setOnClickListener(v -> {
            activityModel.moveNextDay();
        });

        view.prevButton.setOnClickListener(v -> {
            activityModel.movePreviousDay();
        });

        view.addButton.setOnClickListener(v -> {
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        view.actionSettings.setOnClickListener(v -> {
            // display the action menu
            var dialogFragment = FocusMenuDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "FocusMenuDialogFragment");

        });

        return view.getRoot();
    }
}