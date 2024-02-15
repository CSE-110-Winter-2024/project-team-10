package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.databinding.FragmentTaskListBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentTopBarBinding;

public class TopBarFragment extends Fragment {

    private MainViewModel activityModel;
    private FragmentTopBarBinding view;

    public TopBarFragment() {}

    public static TopBarFragment newInstance() {
        TopBarFragment fragment = new TopBarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = FragmentTopBarBinding.inflate(inflater, container, false);

        String strDate = new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(new Date());
        view.dateText.setText(strDate);

        view.plusButton.setOnClickListener(v -> {
           activityModel.appendTask(null);
        });

        return view.getRoot();
    }
}