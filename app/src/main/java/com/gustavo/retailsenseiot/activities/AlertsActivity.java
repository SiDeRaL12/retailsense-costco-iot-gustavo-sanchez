package com.gustavo.retailsenseiot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.adapters.AlertsAdapter;
import com.gustavo.retailsenseiot.databinding.ActivityAlertsBinding;
import com.gustavo.retailsenseiot.dialogs.AlertSuppressDialog;
import com.gustavo.retailsenseiot.models.Alert;
import com.gustavo.retailsenseiot.models.Device;
import com.gustavo.retailsenseiot.models.Store;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AlertsActivity extends AppCompatActivity {
    private ActivityAlertsBinding binding;
    private AlertsAdapter alertsAdapter;
    private List<Alert> allAlerts;
    private Set<String> selectedSeverities = new HashSet<>();
    private Set<String> selectedTypes = new HashSet<>();
    private Set<String> selectedStores = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlertsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        loadAlerts();
        setupRecyclerView();
        setupFilterChips();
        setupAcknowledgeAllButton();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadAlerts() {
        allAlerts = DataManager.getAllAlerts();
        updateEmptyState();
    }

    private void setupRecyclerView() {
        alertsAdapter = new AlertsAdapter(
            new ArrayList<>(allAlerts),
            this::acknowledgeAlert,
            this::suppressAlert,
            this::openDevice
        );

        binding.rvAlerts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAlerts.setAdapter(alertsAdapter);

        applyFilters();
    }

    private void setupFilterChips() {
        setupSeverityChips();
        setupTypeChips();
        setupStoreChips();
    }

    private void setupSeverityChips() {
        Set<String> severities = allAlerts.stream()
            .map(Alert::getSeverity)
            .collect(Collectors.toSet());

        for (String severity : severities) {
            Chip chip = new Chip(this);
            chip.setText(severity);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedSeverities.add(severity);
                } else {
                    selectedSeverities.remove(severity);
                }
                applyFilters();
            });
            binding.chipGroupSeverity.addView(chip);
        }
    }

    private void setupTypeChips() {
        Set<String> types = allAlerts.stream()
            .map(Alert::getType)
            .collect(Collectors.toSet());

        for (String type : types) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTypes.add(type);
                } else {
                    selectedTypes.remove(type);
                }
                applyFilters();
            });
            binding.chipGroupType.addView(chip);
        }
    }

    private void setupStoreChips() {
        Set<String> storeIds = allAlerts.stream()
            .map(alert -> {
                Device device = DataManager.getDeviceById(alert.getDeviceId());
                return device != null ? device.getStoreId() : null;
            })
            .filter(storeId -> storeId != null)
            .collect(Collectors.toSet());

        for (String storeId : storeIds) {
            Store store = DataManager.getStoreById(storeId);
            if (store != null) {
                Chip chip = new Chip(this);
                chip.setText(store.getName());
                chip.setCheckable(true);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedStores.add(storeId);
                    } else {
                        selectedStores.remove(storeId);
                    }
                    applyFilters();
                });
                binding.chipGroupStore.addView(chip);
            }
        }
    }

    private void setupAcknowledgeAllButton() {
        binding.btnAcknowledgeAll.setOnClickListener(v -> {
            List<Alert> currentAlerts = alertsAdapter.getCurrentAlerts();
            for (Alert alert : currentAlerts) {
                alert.setAck(true);
            }

            // Remove acknowledged alerts from the list
            allAlerts.removeAll(currentAlerts);
            applyFilters();

            Snackbar.make(binding.getRoot(),
                currentAlerts.size() + " alerts acknowledged",
                Snackbar.LENGTH_SHORT).show();
        });
    }

    private void applyFilters() {
        List<Alert> filteredAlerts = allAlerts.stream()
            .filter(alert -> !alert.isAck()) // Only show unacknowledged alerts
            .filter(alert -> selectedSeverities.isEmpty() || selectedSeverities.contains(alert.getSeverity()))
            .filter(alert -> selectedTypes.isEmpty() || selectedTypes.contains(alert.getType()))
            .filter(alert -> {
                if (selectedStores.isEmpty()) return true;
                Device device = DataManager.getDeviceById(alert.getDeviceId());
                return device != null && selectedStores.contains(device.getStoreId());
            })
            .collect(Collectors.toList());

        alertsAdapter.updateAlerts(filteredAlerts);
        updateEmptyState();
    }

    private void updateEmptyState() {
        List<Alert> currentAlerts = alertsAdapter != null ? alertsAdapter.getCurrentAlerts() : allAlerts;
        boolean isEmpty = currentAlerts.isEmpty();

        binding.rvAlerts.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.emptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        binding.btnAcknowledgeAll.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void acknowledgeAlert(Alert alert) {
        alert.setAck(true);
        allAlerts.remove(alert);
        applyFilters();

        Snackbar.make(binding.getRoot(), "Alert acknowledged", Snackbar.LENGTH_SHORT).show();
    }

    private void suppressAlert(Alert alert) {
        AlertSuppressDialog.show(this, alert, suppressedAlert -> {
            applyFilters(); // Refresh alerts list
            Snackbar.make(binding.getRoot(), "Alert suppressed", Snackbar.LENGTH_SHORT).show();
        });
    }

    private void openDevice(Alert alert) {
        Device device = DataManager.getDeviceById(alert.getDeviceId());
        if (device != null) {
            Intent intent = new Intent(this, DeviceDetailActivity.class);
            intent.putExtra("deviceId", device.getId());
            startActivity(intent);
        }
    }
}
