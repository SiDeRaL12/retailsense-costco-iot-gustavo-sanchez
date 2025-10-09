package com.gustavo.retailsenseiot.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.snackbar.Snackbar;
import com.gustavo.retailsenseiot.adapters.RecentAlertsAdapter;
import com.gustavo.retailsenseiot.adapters.StoreDevicesAdapter;
import com.gustavo.retailsenseiot.databinding.ActivityStoreDashboardBinding;
import com.gustavo.retailsenseiot.models.*;
import com.gustavo.retailsenseiot.utils.CSVDataLoader;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.*;

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
        setupCharts();
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
    }

    private void setupCharts() {
        setupTrafficEnergyChart();
        setupColdChainChart();
        setupStockOutChart();
    }

    private void setupTrafficEnergyChart() {
        CombinedChart chart = binding.chartTrafficEnergy;

        // Generate sample traffic and energy data
        List<Entry> trafficEntries = new ArrayList<>();
        List<Entry> energyEntries = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            trafficEntries.add(new Entry(i, (float) (Math.random() * 50 + 10))); // 10-60 people
            energyEntries.add(new Entry(i, (float) (Math.random() * 30 + 20))); // 20-50 kW
        }

        LineDataSet trafficDataSet = new LineDataSet(trafficEntries, "Traffic");
        trafficDataSet.setColor(Color.BLUE);
        trafficDataSet.setLineWidth(2f);

        LineDataSet energyDataSet = new LineDataSet(energyEntries, "Energy (kW)");
        energyDataSet.setColor(Color.RED);
        energyDataSet.setLineWidth(2f);

        LineData lineData = new LineData(trafficDataSet, energyDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);

        chart.setData(combinedData);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }

    private void setupColdChainChart() {
        LineChart chart = binding.chartColdChain;

        // Get cold chain devices for this store and load their data
        List<Device> coldChainDevices = DataManager.getDevicesByStore(storeId).stream()
                .filter(device -> "ColdChain".equals(device.getType()))
                .collect(java.util.stream.Collectors.toList());

        List<ILineDataSet> dataSets = new ArrayList<>();

        for (Device device : coldChainDevices) {
            List<Reading> readings = CSVDataLoader.loadDeviceReadings(this, device.getId());
            if (!readings.isEmpty()) {
                List<Entry> entries = new ArrayList<>();
                for (int i = 0; i < Math.min(readings.size(), 24); i++) {
                    Reading reading = readings.get(i);
                    if (reading instanceof ColdChainReading) {
                        ColdChainReading ccReading = (ColdChainReading) reading;
                        entries.add(new Entry(i, (float) ccReading.getTempC()));
                    }
                }

                if (!entries.isEmpty()) {
                    LineDataSet dataSet = new LineDataSet(entries, device.getLabel());
                    dataSet.setColor(Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255)));
                    dataSet.setLineWidth(2f);
                    dataSets.add(dataSet);
                }
            }
        }

        if (dataSets.isEmpty()) {
            // Fallback: create sample data
            List<Entry> sampleEntries = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                sampleEntries.add(new Entry(i, (float) (Math.random() * 2 + 2))); // 2-4°C
            }
            LineDataSet sampleDataSet = new LineDataSet(sampleEntries, "Sample Cold Chain");
            sampleDataSet.setColor(Color.CYAN);
            sampleDataSet.setLineWidth(2f);
            dataSets.add(sampleDataSet);
        }

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }

    private void setupStockOutChart() {
        HorizontalBarChart chart = binding.chartStockOut;

        // Generate sample stock out data
        List<BarEntry> entries = new ArrayList<>();
        String[] labels = {"Dairy", "Frozen", "Produce", "Bakery", "Meat"};

        for (int i = 0; i < labels.length; i++) {
            entries.add(new BarEntry(i, (float) (Math.random() * 10 + 1))); // 1-11 items
        }

        BarDataSet dataSet = new BarDataSet(entries, "Stock Out Items");
        dataSet.setColor(Color.MAGENTA);

        BarData barData = new BarData(dataSet);
        chart.setData(barData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);

        chart.getDescription().setEnabled(false);
        chart.invalidate();
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
