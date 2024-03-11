package edu.ucsd.cse110.successorator;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.FocusMenuDialogFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_settings);
        menuItem.setOnMenuItemClickListener(this::onMenuItemClick);
        return true;
    }

    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.i("menu", "menu clicked");
            var dialogFragment = FocusMenuDialogFragment.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "FocusMenuDialogFragment");
            return true;
        }

        return false;
    }
}