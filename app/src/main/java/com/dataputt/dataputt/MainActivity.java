package com.dataputt.dataputt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dataputt.dataputt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.putting:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PuttingFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
                return true;
            case R.id.stats:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, StatsFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
                return true;
            case R.id.settings:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment.class, null)
                        .setReorderingAllowed(true)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}