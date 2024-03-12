package edu.ucsd.cse110.successorator.ui.tasklist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.FragmentFocusMenuBinding;
import edu.ucsd.cse110.successorator.ui.tasklist.dialog.FocusMenuDialogFragment;

public class FocusMenuFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        edu.ucsd.cse110.successorator.databinding.FragmentFocusMenuBinding binding = FragmentFocusMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Enable options menu in this fragment
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here.
        if (item.getItemId() == R.id.action_settings) {
            FocusMenuDialogFragment.newInstance().show(getChildFragmentManager(), "FocusMenuDialogFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
