package com.gustavo.retailsenseiot.utils;

import android.content.Context;
import com.gustavo.retailsenseiot.models.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CSVDataLoader {

    private static final SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    static {
        ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static List<Reading> loadDeviceReadings(Context context, String deviceId) {
        List<Reading> readings = new ArrayList<>();
        Device device = DataManager.getDeviceById(deviceId);

        if (device == null) {
            return readings;
        }

        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open("readings_" + deviceId + ".csv")));

            String header = reader.readLine(); // Skip header
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");

                    // Parse timestamp from ISO format to milliseconds
                    long timestamp;
                    try {
                        timestamp = ISO_FORMAT.parse(parts[0].trim()).getTime();
                    } catch (Exception e) {
                        // Fallback: try parsing as long (in case some files have numeric timestamps)
                        timestamp = Long.parseLong(parts[0].trim());
                    }

                    switch (device.getType()) {
                        case "ColdChain":
                            if (parts.length >= 3) {
                                readings.add(new ColdChainReading(
                                    timestamp,
                                    Double.parseDouble(parts[1].trim()),
                                    Integer.parseInt(parts[2].trim())
                                ));
                            }
                            break;
                        case "ShelfWeight":
                            if (parts.length >= 2) {
                                readings.add(new ShelfWeightReading(
                                    timestamp,
                                    Double.parseDouble(parts[1].trim())
                                ));
                            }
                            break;
                        case "PeopleCounter":
                            if (parts.length >= 2) {
                                readings.add(new PeopleCounterReading(
                                    timestamp,
                                    Integer.parseInt(parts[1].trim())
                                ));
                            }
                            break;
                        case "EnergyMeter":
                            if (parts.length >= 2) {
                                readings.add(new EnergyMeterReading(
                                    timestamp,
                                    Double.parseDouble(parts[1].trim())
                                ));
                            }
                            break;
                        case "Beacon":
                            if (parts.length >= 2) {
                                readings.add(new BeaconReading(
                                    timestamp,
                                    Integer.parseInt(parts[1].trim())
                                ));
                            }
                            break;
                    }
                } catch (Exception e) {
                    android.util.Log.w("CSVDataLoader", "Skipping malformed row: " + line);
                }
            }
            reader.close();
        } catch (Exception e) {
            android.util.Log.e("CSVDataLoader", "Error loading readings for device " + deviceId, e);
        }

        return readings;
    }
}
