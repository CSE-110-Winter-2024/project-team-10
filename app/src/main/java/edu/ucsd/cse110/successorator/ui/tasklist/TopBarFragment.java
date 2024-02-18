package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        String strDate = new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(new Date());
        view.dateText.setText(strDate);

        view.addButton.setOnClickListener(v -> {
            Log.i("addButton", "adding task");
            var dialogFragment = CreateTaskDialogFragment.newInstance();
            dialogFragment.show(getParentFragmentManager(), "CreateTaskDialogFragment");
        });

        return view.getRoot();
    }
}