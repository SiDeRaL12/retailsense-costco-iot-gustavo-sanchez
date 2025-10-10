package com.gustavo.retailsenseiot;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.gustavo.retailsenseiot.activities.StoresActivity;
import com.gustavo.retailsenseiot.databinding.ActivityMainBinding;
import com.gustavo.retailsenseiot.utils.DataManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize DataManager
        DataManager.initialize(this);
        setupToolbar();
        setupCardClicks();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_reset_day) {
                showResetDayDialog();
                return true;
            } else if (item.getItemId() == R.id.action_about) {
                showAboutDialog();
                return true;
            }
            return false;
        });
    }

    private void setupCardClicks() {
        binding.cardStores.setOnClickListener(v -> {
            startActivity(new Intent(this, StoresActivity.class));
        });
        binding.cardDevices.setOnClickListener(v -> {
            startActivity(new Intent(this, com.gustavo.retailsenseiot.activities.DevicesActivity.class));
        });
        binding.cardAlerts.setOnClickListener(v -> {
            startActivity(new Intent(this, com.gustavo.retailsenseiot.activities.AlertsActivity.class));
        });
        binding.cardInsights.setOnClickListener(v -> {
            startActivity(new Intent(this, com.gustavo.retailsenseiot.activities.InsightsActivity.class));
        });
    }

    private void showResetDayDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Day")
                .setMessage("Reset simulation to 08:00 today and reload data?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Reset", (dialog, which) -> {
                    DataManager.resetDay(this);
                    Snackbar.make(binding.getRoot(), "Reset complete", Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About RetailSense IoT")
                .setMessage("Costco IoT Store Device Management\nv1.0.0 for MAP524")
                .setPositiveButton("OK", null)
                .show();
    }
}