package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentTopBarBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;

public class TopBarFragment extends Fragment {
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
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);

        var activityModel = modelProvider.get(MainViewModel.class);

        // Make a date formatter
        var formatter = DateTimeFormatter.ofPattern("EE, MMM dd", Locale.ENGLISH);

        // Update the date text whenever the date changes
        activityModel.getCurrentDateSubject().observe(v -> {
            view.dateText.setText(v.format(formatter));
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

        return view.getRoot();
    }
}