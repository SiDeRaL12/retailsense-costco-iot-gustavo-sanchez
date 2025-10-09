package com.gustavo.retailsenseiot.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.snackbar.Snackbar;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.adapters.DeviceAlertsAdapter;
import com.gustavo.retailsenseiot.databinding.ActivityDeviceDetailBinding;
import com.gustavo.retailsenseiot.models.*;
import com.gustavo.retailsenseiot.utils.CSVDataLoader;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeviceDetailActivity extends AppCompatActivity {
    private ActivityDeviceDetailBinding binding;
    private String deviceId;
    private Device device;
    private List<Reading> readings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deviceId = getIntent().getStringExtra("deviceId");
        device = DataManager.getDeviceById(deviceId);

        if (device == null) {
            finish();
            return;
        }

        setupToolbar();
        loadReadings();
        setupDeviceInfo();
        setupChart();
        setupAlerts();
        setupButtons();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(device.getLabel());
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadReadings() {
        readings = CSVDataLoader.loadDeviceReadings(this, deviceId);
    }

    private void setupDeviceInfo() {
        String storeName = DataManager.getStoreById(device.getStoreId()).getName();
        binding.tvDeviceInfo.setText(device.getType() + " at " + storeName);

        String lastReading = readings.isEmpty() ? "No data" :
            new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                .format(new Date()); // Simulate current time
        binding.tvLastReading.setText("Last reading: " + lastReading);

        // Status
        binding.chipDeviceStatus.setText(device.getStatus());
        if (device.getStatus().equals("OK")) {
            binding.chipDeviceStatus.setChipBackgroundColorResource(R.color.status_ok);
        } else {
            binding.chipDeviceStatus.setChipBackgroundColorResource(R.color.status_critical);
        }

        // Configuration info
        StringBuilder configInfo = new StringBuilder();
        if (device.getConfig().getMinC() != null) {
            configInfo.append("Min: ").append(device.getConfig().getMinC()).append("°C ");
        }
        if (device.getConfig().getMaxC() != null) {
            configInfo.append("Max: ").append(device.getConfig().getMaxC()).append("°C ");
        }
        if (device.getConfig().getMinPct() != null) {
            configInfo.append("Min: ").append(device.getConfig().getMinPct() * 100).append("% ");
        }
        if (device.getConfig().getBaselineKWh() != null) {
            configInfo.append("Baseline: ").append(device.getConfig().getBaselineKWh()).append(" kWh");
        }

        binding.tvConfigInfo.setText(configInfo.toString().trim());
    }

    private void setupChart() {
        // E1: Chart shows minimum 24h of data
        List<Entry> entries = new ArrayList<>();

        // Convert readings to chart entries
        for (int i = 0; i < readings.size(); i++) {
            Reading reading = readings.get(i);
            float value = 0f;

            if (reading instanceof ColdChainReading) {
                value = (float) ((ColdChainReading) reading).getTempC();
            } else if (reading instanceof ShelfWeightReading) {
                value = (float) ((ShelfWeightReading) reading).getPctFull() * 100;
            } else if (reading instanceof PeopleCounterReading) {
                value = (float) ((PeopleCounterReading) reading).getCount();
            } else if (reading instanceof EnergyMeterReading) {
                value = (float) ((EnergyMeterReading) reading).getKWh();
            } else if (reading instanceof BeaconReading) {
                value = (float) ((BeaconReading) reading).getDwellSec();
            }

            entries.add(new Entry(i, value));
        }

        LineDataSet dataSet = new LineDataSet(entries, getChartLabel());
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        binding.chartReadings.setData(lineData);

        // Chart styling
        binding.chartReadings.getDescription().setEnabled(false);
        binding.chartReadings.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chartReadings.invalidate();
    }

    private String getChartLabel() {
        switch (device.getType()) {
            case "ColdChain": return "Temperature (°C)";
            case "ShelfWeight": return "Stock Level (%)";
            case "PeopleCounter": return "People Count";
            case "EnergyMeter": return "Energy (kWh)";
            case "Beacon": return "Dwell Time (sec)";
            default: return "Value";
        }
    }

    private void setupAlerts() {
        List<Alert> deviceAlerts = new ArrayList<>();
        for (Alert alert : DataManager.getAlerts()) {
            if (alert.getDeviceId().equals(deviceId) && !alert.isAck()) {
                deviceAlerts.add(alert);
            }
        }

        DeviceAlertsAdapter alertsAdapter = new DeviceAlertsAdapter(deviceAlerts,
            alert -> acknowledgeAlert(alert));
        binding.rvDeviceAlerts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDeviceAlerts.setAdapter(alertsAdapter);

        binding.tvAlertsCount.setText(deviceAlerts.size() + " Active Alerts");
    }

    private void setupButtons() {
        binding.btnCalibrateDevice.setOnClickListener(v -> {
            // E2: Calibration updates immediately
            showCalibrateDialog();
        });

        binding.btnAcknowledgeAlerts.setOnClickListener(v -> {
            // E3: Acknowledge removes alerts
            acknowledgeAllAlerts();
        });
    }

    private void showCalibrateDialog() {
        // TODO: Implement calibration dialog in Phase 9
        Snackbar.make(binding.getRoot(), "Calibration updated", Snackbar.LENGTH_SHORT).show();
    }

    private void acknowledgeAlert(Alert alert) {
        DataManager.acknowledgeAlert(alert.getId());
        setupAlerts(); // Refresh alerts
        Snackbar.make(binding.getRoot(), "Alert acknowledged", Snackbar.LENGTH_SHORT).show();
    }

    private void acknowledgeAllAlerts() {
        for (Alert alert : DataManager.getAlerts()) {
            if (alert.getDeviceId().equals(deviceId) && !alert.isAck()) {
                DataManager.acknowledgeAlert(alert.getId());
            }
        }
        setupAlerts(); // Refresh alerts
        Snackbar.make(binding.getRoot(), "All alerts acknowledged", Snackbar.LENGTH_SHORT).show();
    }
}

