package com.gustavo.retailsenseiot.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.snackbar.Snackbar;
import com.gustavo.retailsenseiot.adapters.RecentAlertsAdapter;
import com.gustavo.retailsenseiot.adapters.StoreDevicesAdapter;
import com.gustavo.retailsenseiot.databinding.ActivityStoreDashboardBinding;
import com.gustavo.retailsenseiot.models.Alert;
import com.gustavo.retailsenseiot.models.Device;
import com.gustavo.retailsenseiot.models.Store;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StoreDashboardActivity extends AppCompatActivity {
    private ActivityStoreDashboardBinding binding;
    private String storeId;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoreDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        storeId = getIntent().getStringExtra("storeId");
        store = DataManager.getStoreById(storeId);

        if (store == null) {
            Toast.makeText(this, "Store not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupToolbar();
        setupData();
        setupRecyclerViews();
        setupButtons();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(store.getName());
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupData() {
        // Simulated time
        String currentTime = new SimpleDateFormat("EEEE, MMM dd 'at' HH:mm", Locale.getDefault())
                .format(new Date());
        binding.tvSimulatedTime.setText("Simulated: " + currentTime);

        List<Device> devices = DataManager.getDevicesByStore(storeId);
        List<Alert> alerts = DataManager.getActiveAlertsForStore(storeId);

        // Header counters
        int onlineDevices = devices.size(); // Assume all online for simulation
        binding.tvDevicesOnline.setText(onlineDevices + " Devices Online");
        binding.tvActiveAlerts.setText(alerts.size() + " Active Alerts");

        // TODO: Implement tiles and charts in Phase 6-8
        // For now, show placeholder data
    }

    private void setupRecyclerViews() {
        // Recent Alerts
        List<Alert> recentAlerts = DataManager.getActiveAlertsForStore(storeId);
        RecentAlertsAdapter alertsAdapter = new RecentAlertsAdapter(recentAlerts,
            alert -> {
                // TODO: Navigate to alert detail
                Toast.makeText(this, "Alert: " + alert.getMessage(), Toast.LENGTH_SHORT).show();
            });
        binding.rvRecentAlerts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRecentAlerts.setAdapter(alertsAdapter);

        // Store Devices
        List<Device> devices = DataManager.getDevicesByStore(storeId);
        StoreDevicesAdapter devicesAdapter = new StoreDevicesAdapter(devices,
            device -> {
                // TODO: Navigate to device detail
                Toast.makeText(this, "Device: " + device.getLabel(), Toast.LENGTH_SHORT).show();
            });
        binding.rvStoreDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStoreDevices.setAdapter(devicesAdapter);
    }

    private void setupButtons() {
        binding.btnAcknowledgeAll.setOnClickListener(v -> {
            // TODO: Implement acknowledge all
            Snackbar.make(binding.getRoot(), "All alerts acknowledged", Snackbar.LENGTH_SHORT).show();
        });

        binding.btnAdvanceHour.setOnClickListener(v -> {
            // TODO: Implement advance hour simulation
            Snackbar.make(binding.getRoot(), "Advanced 1 hour", Snackbar.LENGTH_SHORT).show();
        });
    }
}

