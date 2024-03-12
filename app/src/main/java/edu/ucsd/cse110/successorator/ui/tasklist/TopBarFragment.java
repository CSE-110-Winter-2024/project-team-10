package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import edu.ucsd.cse110.successorator.databinding.FragmentTopBarBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;
import edu.ucsd.cse110.successorator.util.ViewMode;

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
        dateTimeFormatter = DateTimeFormatter.ofPattern("EE MM/dd", Locale.ENGLISH);

        // Drop down handler
        Spinner spinner = view.spinner;

        activityModel.getCurrentDateSubject().observe(v -> {
            ArrayList<String> headers = new ArrayList<>();
            headers.add("Today, " + v.format(dateTimeFormatter));
            headers.add("Tomorrow, " + v.plusDays(1).format(dateTimeFormatter));
            headers.add("Pending");
            headers.add("Recurring");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, headers);

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

        return view.getRoot();
    }
}