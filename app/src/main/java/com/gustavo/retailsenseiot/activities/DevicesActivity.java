package com.gustavo.retailsenseiot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.chip.Chip;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.adapters.DevicesAdapter;
import com.gustavo.retailsenseiot.databinding.ActivityDevicesBinding;
import com.gustavo.retailsenseiot.models.Device;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DevicesActivity extends AppCompatActivity {
    private ActivityDevicesBinding binding;
    private DevicesAdapter adapter;
    private List<Device> allDevices;
    private Set<String> selectedTypes = new HashSet<>();
    private Set<String> selectedStores = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        loadDevices();
        setupFilterChips();
        setupSearchFilter();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadDevices() {
        allDevices = DataManager.getDevices();
    }

    private void setupFilterChips() {
        // Type filter chips
        Set<String> deviceTypes = new HashSet<>();
        Set<String> storeIds = new HashSet<>();

        for (Device device : allDevices) {
            deviceTypes.add(device.getType());
            storeIds.add(device.getStoreId());
        }

        // Add type filter chips
        for (String type : deviceTypes) {
            Chip chip = new Chip(this);
            chip.setText(type);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTypes.add(type);
                } else {
                    selectedTypes.remove(type);
                }
                filterDevices(); // V1: Fast filtering <100ms
            });
            binding.chipGroupType.addView(chip);
        }

        // Add store filter chips
        for (String storeId : storeIds) {
            String storeName = DataManager.getStoreById(storeId).getName();
            Chip chip = new Chip(this);
            chip.setText(storeName);
            chip.setCheckable(true);
            chip.setTag(storeId);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedStores.add(storeId);
                } else {
                    selectedStores.remove(storeId);
                }
                filterDevices();
            });
            binding.chipGroupStore.addView(chip);
        }
    }

    private void setupSearchFilter() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDevices();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRecyclerView() {
        adapter = new DevicesAdapter(
            allDevices,
            device -> openDeviceDetail(device),
            device -> showMaintenanceDialog(device),
            device -> toggleDeviceEnabled(device) // V2: Disable functionality
        );

        binding.rvDevices.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDevices.setAdapter(adapter);

        updateEmptyState();
    }

    private void filterDevices() {
        long startTime = System.currentTimeMillis();

        List<Device> filteredDevices = new ArrayList<>();
        String searchQuery = binding.etSearch.getText().toString().toLowerCase();

        for (Device device : allDevices) {
            // Skip disabled devices from aggregates (V2)
            if (device.getStatus().equals("Disabled")) {
                continue;
            }

            // Text search filter
            boolean matchesSearch = searchQuery.isEmpty() ||
                device.getLabel().toLowerCase().contains(searchQuery) ||
                device.getType().toLowerCase().contains(searchQuery);

            // Type filter
            boolean matchesType = selectedTypes.isEmpty() || selectedTypes.contains(device.getType());

            // Store filter
            boolean matchesStore = selectedStores.isEmpty() || selectedStores.contains(device.getStoreId());

            if (matchesSearch && matchesType && matchesStore) {
                filteredDevices.add(device);
            }
        }

        adapter.updateDevices(filteredDevices);
        updateEmptyState();

        // V1: Ensure filtering performance <100ms
        long duration = System.currentTimeMillis() - startTime;
        if (duration > 100) {
            android.util.Log.w("DevicesActivity", "Filter took " + duration + "ms (should be <100ms)");
        }
    }

    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            binding.rvDevices.setVisibility(View.GONE);
            binding.emptyState.setVisibility(View.VISIBLE);
        } else {
            binding.rvDevices.setVisibility(View.VISIBLE);
            binding.emptyState.setVisibility(View.GONE);
        }
    }

    private void openDeviceDetail(Device device) {
        Intent intent = new Intent(this, DeviceDetailActivity.class);
        intent.putExtra("deviceId", device.getId());
        startActivity(intent);
    }

    private void showMaintenanceDialog(Device device) {
        // V3: Maintenance dialog validates (implement in Phase 9)
        android.widget.Toast.makeText(this, "Maintenance for " + device.getLabel(),
            android.widget.Toast.LENGTH_SHORT).show();
    }

    private void toggleDeviceEnabled(Device device) {
        // V2: Toggle device enabled/disabled state
        String newStatus = device.getStatus().equals("OK") ? "Disabled" : "OK";
        device.setStatus(newStatus);
        adapter.notifyDataSetChanged();
        filterDevices(); // Refresh to exclude/include from aggregates
    }
}
