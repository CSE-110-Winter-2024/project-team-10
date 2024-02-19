package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainActivity;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentTopBarBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.CreateTaskDialogFragment;

public class TopBarFragment extends Fragment {
    private LocalDate tomorrow;
    public TopBarFragment() {}

    public static TopBarFragment newInstance(LocalDate date) {
        TopBarFragment fragment = new TopBarFragment();
        Bundle args = new Bundle();
        args.putSerializable("date_key", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var view = FragmentTopBarBinding.inflate(inflater, container, false);

        if (getArguments() != null) tomorrow = (LocalDate) getArguments().getSerializable("date_key");
        else tomorrow = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE, MMM dd", Locale.ENGLISH);
        updateDateText(view, formatter);

        view.nextButton.setOnClickListener(v -> {
            tomorrow = tomorrow.plusDays(1);
            updateDateText(view, formatter);
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).onTopBarNextButtonClicked();
        });

        view.todayButton.setOnClickListener(v -> {
            tomorrow = LocalDate.now();
            updateDateText(view, formatter);
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).onTopBarTodayButtonClicked();
        });

        view.addButton.setOnClickListener(v -> {
            Log.i("addButton", "adding task");
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        return view.getRoot();
    }
    private void updateDateText(FragmentTopBarBinding view, DateTimeFormatter formatter) {
        String formattedDate = tomorrow.format(formatter);
        view.dateText.setText(formattedDate);
        Log.i("date", "Date set to: " + formattedDate);
    }

    public String getDateText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH);
        return tomorrow.format(formatter);
    }
}