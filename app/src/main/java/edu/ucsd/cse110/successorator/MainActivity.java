package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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


        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        String strDate = new SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(new Date());
        view.dateText.setText(strDate);

        setContentView(view.getRoot());

    }
}