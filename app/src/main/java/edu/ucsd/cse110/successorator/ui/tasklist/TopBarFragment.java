package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentTopBarBinding;
import edu.ucsd.cse110.successorator.lib.domain.TaskContext;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.FocusMenuDialogFragment;
import edu.ucsd.cse110.successorator.util.ViewMode;

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
        var dateTimeFormatter = DateTimeFormatter.ofPattern("EE MM/dd", Locale.ENGLISH);

        // Drop down handler
        Spinner spinner = view.spinner;

        activityModel.getCurrentDateSubject().observe(v -> {
            ArrayList<String> headers = new ArrayList<>();
            headers.add("Today, " + v.format(dateTimeFormatter));
            headers.add("Tomorrow, " + v.plusDays(1).format(dateTimeFormatter));
            headers.add("Pending");
            headers.add("Recurring");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.view_mode_text, headers);

            spinner.setAdapter(adapter);
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Spinner selected", "position:" + position);
                activityModel.registerViewMode(ViewMode.fetch(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Button handlers
        view.nextButton.setOnClickListener(v -> {
            activityModel.advanceNextDay();
        });

        view.prevButton.setOnClickListener(v -> {
            activityModel.advancePreviousDay();
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

        activityModel.getSelectedTaskContext().observe(getViewLifecycleOwner(), taskContext -> {
            configureMenuColor(view, taskContext);
        });

        // Disable the previous button
        view.prevButton.setVisibility(View.INVISIBLE);
        view.prevButton.setEnabled(false);

        return view.getRoot();
    }

    void configureMenuColor(FragmentTopBarBinding view, TaskContext taskContext) {
        if (taskContext != null) {
            switch (taskContext) {
                case HOME:
                    view.actionSettings.setBackgroundColor(getResources().getColor(R.color.home_focus_color, null));
                    break;
                case WORK:
                    view.actionSettings.setBackgroundColor(getResources().getColor(R.color.work_focus_color, null));
                    break;
                case SCHOOL:
                    view.actionSettings.setBackgroundColor(getResources().getColor(R.color.school_focus_color, null));
                    break;
                case ERRAND:
                    view.actionSettings.setBackgroundColor(getResources().getColor(R.color.errand_focus_color, null));
                    break;
                default:
                    view.actionSettings.setBackgroundColor(getResources().getColor(R.color.default_focus_color, null)); // A default color
            }
        } else {
            view.actionSettings.setBackgroundColor(getResources().getColor(R.color.default_focus_color, null)); // Reset to default if null
        }
    }
}