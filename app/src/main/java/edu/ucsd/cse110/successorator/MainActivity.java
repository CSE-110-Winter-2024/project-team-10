package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;

    private MainViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the model

        // Get the view
        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        // M -> V binding
//        String strDate = new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(new Date());
//        view.dateText.setText(strDate);

        // V -> M binding
//        view.plusButton.setOnClickListener(v -> {
//            // do something with model
//            // 1st simple: append hardcoded
//            // 2nd complex: show dialog, then ...
//            viewModel.appendTask(null);
//        });

        setContentView(view.getRoot());
    }


}