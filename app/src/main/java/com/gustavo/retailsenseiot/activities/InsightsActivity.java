package com.gustavo.retailsenseiot.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.gustavo.retailsenseiot.databinding.ActivityInsightsBinding;
import com.gustavo.retailsenseiot.models.Device;
import com.gustavo.retailsenseiot.models.Reading;
import com.gustavo.retailsenseiot.models.Store;
import com.gustavo.retailsenseiot.utils.CSVDataLoader;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InsightsActivity extends AppCompatActivity {
    private ActivityInsightsBinding binding;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsightsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupCharts();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupCharts() {
        // I1: All 4 charts render requirement
        setupTrafficEnergyChart();
        setupTemperatureChart();
        setupStockLevelsChart();
        setupCorrelationChart();
    }

    private void setupTrafficEnergyChart() {
        // Chart 1: Combined Traffic & Energy
        List<Entry> trafficEntries = generateSampleData(24, 50, 200);
        List<BarEntry> energyEntries = generateBarData(24, 15, 30);

        LineDataSet trafficSet = new LineDataSet(trafficEntries, "Foot Traffic");
        trafficSet.setColor(Color.BLUE);
        trafficSet.setCircleColor(Color.BLUE);
        trafficSet.setLineWidth(2f);

        BarDataSet energySet = new BarDataSet(energyEntries, "Energy (kWh)");
        energySet.setColor(Color.RED);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(new LineData(trafficSet));
        combinedData.setData(new BarData(energySet));

        binding.chartTrafficEnergy.setData(combinedData);
        binding.chartTrafficEnergy.getDescription().setText("Traffic vs Energy Consumption");
        binding.chartTrafficEnergy.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chartTrafficEnergy.invalidate();
    }

    private void setupTemperatureChart() {
        // Chart 2: Temperature Trends
        List<Entry> tempEntries = generateSampleData(48, -2, 6); // Temperature range

        LineDataSet tempSet = new LineDataSet(tempEntries, "Average Temperature (°C)");
        tempSet.setColor(Color.CYAN);
        tempSet.setCircleColor(Color.CYAN);
        tempSet.setLineWidth(2f);
        tempSet.setFillAlpha(50);
        tempSet.setDrawFilled(true);

        LineData lineData = new LineData(tempSet);
        binding.chartTemperature.setData(lineData);
        binding.chartTemperature.getDescription().setText("Cold Chain Temperature Trends");
        binding.chartTemperature.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chartTemperature.invalidate();
    }

    private void setupStockLevelsChart() {
        // Chart 3: Stock Levels by Product
        String[] products = {"Water Bottles", "Bread", "Milk", "Eggs", "Chicken", "Beef"};
        List<BarEntry> stockEntries = new ArrayList<>();

        for (int i = 0; i < products.length; i++) {
            stockEntries.add(new BarEntry(i, random.nextFloat() * 100));
        }

        BarDataSet stockSet = new BarDataSet(stockEntries, "Stock Level (%)");
        stockSet.setColors(new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.GREEN, Color.YELLOW, Color.RED});

        BarData barData = new BarData(stockSet);
        binding.chartStockLevels.setData(barData);
        binding.chartStockLevels.getDescription().setText("Product Stock Levels");
        binding.chartStockLevels.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chartStockLevels.invalidate();
    }

    private void setupCorrelationChart() {
        // Chart 4: Energy vs Traffic Correlation
        List<Entry> correlationEntries = new ArrayList<>();

        // Generate correlation data points
        for (int i = 0; i < 50; i++) {
            float traffic = random.nextFloat() * 200;
            float energy = traffic * 0.15f + random.nextFloat() * 5; // Positive correlation
            correlationEntries.add(new Entry(traffic, energy));
        }

        ScatterDataSet correlationSet = new ScatterDataSet(correlationEntries, "Energy vs Traffic");
        correlationSet.setColor(Color.MAGENTA);
        correlationSet.setScatterShapeSize(8f);

        ScatterData scatterData = new ScatterData(correlationSet);
        binding.chartCorrelation.setData(scatterData);
        binding.chartCorrelation.getDescription().setText("Energy vs Traffic Correlation");
        binding.chartCorrelation.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chartCorrelation.invalidate();

        // I2: Correlation value correct requirement
        double correlationValue = calculateCorrelation(correlationEntries);
        binding.tvCorrelationValue.setText(String.format("Correlation: %.2f", correlationValue));
    }

    private List<Entry> generateSampleData(int count, float min, float max) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float value = min + random.nextFloat() * (max - min);
            entries.add(new Entry(i, value));
        }
        return entries;
    }

    private List<BarEntry> generateBarData(int count, float min, float max) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float value = min + random.nextFloat() * (max - min);
            entries.add(new BarEntry(i, value));
        }
        return entries;
    }

    private double calculateCorrelation(List<Entry> entries) {
        // I2: Calculate actual correlation coefficient
        if (entries.size() < 2) return 0.0;

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0, sumY2 = 0;
        int n = entries.size();

        for (Entry entry : entries) {
            double x = entry.getX();
            double y = entry.getY();
            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
            sumY2 += y * y;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));

        return denominator == 0 ? 0 : numerator / denominator;
    }
}
