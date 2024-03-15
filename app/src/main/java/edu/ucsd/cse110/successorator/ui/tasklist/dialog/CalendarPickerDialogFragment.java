package edu.ucsd.cse110.successorator.ui.tasklist.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.time.LocalDate;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentCalendarPickerDialogBinding;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarPickerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarPickerDialogFragment extends DialogFragment {
    MutableSubject<LocalDate> selectedDate;

    public CalendarPickerDialogFragment(MutableSubject<LocalDate> selectedDate) {
        this.selectedDate = selectedDate;
    }

    public static CalendarPickerDialogFragment newInstance(MutableSubject<LocalDate> selectedDate) {
        CalendarPickerDialogFragment fragment = new CalendarPickerDialogFragment(selectedDate);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(@Nullable  Bundle savedInstanceState) {
        var view = FragmentCalendarPickerDialogBinding.inflate(getLayoutInflater());

        view.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("calendar picker", "selected date" + year + "/" + month + "/" + dayOfMonth);
                LocalDate date = LocalDate.of(year, month + 1, dayOfMonth);
                selectedDate.setValue(date);
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("Starting Date")
                .setMessage("Select a starting date for this recurring task")
                .setView(view.getRoot())
                .setNegativeButton("Cancel", this::onCancelButtonClick)
                .create();
    }

    private void onCancelButtonClick(DialogInterface dialog, int which) {
        dialog.cancel();
    }
}