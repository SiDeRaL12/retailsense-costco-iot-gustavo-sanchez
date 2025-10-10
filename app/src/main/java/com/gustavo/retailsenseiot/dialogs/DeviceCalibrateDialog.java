package com.gustavo.retailsenseiot.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.models.Device;

public class DeviceCalibrateDialog {

    public interface OnDeviceCalibratedListener {
        void onDeviceCalibrated(Device device);
    }

    public static void show(Context context, Device device, OnDeviceCalibratedListener listener) {
        android.view.View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_device_calibrate, null);

        TextInputEditText etMinValue = dialogView.findViewById(R.id.etMinValue);
        TextInputEditText etMaxValue = dialogView.findViewById(R.id.etMaxValue);
        TextInputEditText etBaselineValue = dialogView.findViewById(R.id.etBaselineValue);

        TextInputLayout tilMinValue = dialogView.findViewById(R.id.tilMinValue);
        TextInputLayout tilMaxValue = dialogView.findViewById(R.id.tilMaxValue);
        TextInputLayout tilBaselineValue = dialogView.findViewById(R.id.tilBaselineValue);

        // Pre-populate with default values - simplified for now since Device model needs config support
        // TODO: Add proper config support to Device model

        // Set labels based on device type
        String minLabel, maxLabel, baselineLabel;
        switch (device.getType()) {
            case "ColdChain":
                minLabel = "Min Temperature (°C)";
                maxLabel = "Max Temperature (°C)";
                baselineLabel = "Baseline kWh";
                break;
            case "ShelfWeight":
                minLabel = "Min Stock Level (%)";
                maxLabel = "Max Stock Level (%)";
                baselineLabel = "Not applicable";
                tilBaselineValue.setEnabled(false);
                break;
            case "EnergyMeter":
                minLabel = "Min kWh";
                maxLabel = "Max kWh";
                baselineLabel = "Baseline kWh";
                break;
            default:
                minLabel = "Min Value";
                maxLabel = "Max Value";
                baselineLabel = "Baseline Value";
        }

        tilMinValue.setHint(minLabel);
        tilMaxValue.setHint(maxLabel);
        tilBaselineValue.setHint(baselineLabel);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Calibrate Device: " + device.getLabel())
                .setView(dialogView)
                .setPositiveButton("Calibrate", null) // Override to add validation
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                String minStr = etMinValue.getText().toString().trim();
                String maxStr = etMaxValue.getText().toString().trim();
                String baselineStr = etBaselineValue.getText().toString().trim();

                // E2: Calibration updates immediately - validation required
                boolean isValid = true;

                // Validate min value
                if (!minStr.isEmpty()) {
                    try {
                        double minValue = Double.parseDouble(minStr);

                        // Device-specific validation
                        if (device.getType().equals("ColdChain") && (minValue < -30 || minValue > 20)) {
                            etMinValue.setError("Temperature must be between -30°C and 20°C");
                            isValid = false;
                        } else if (device.getType().equals("ShelfWeight") && (minValue < 0 || minValue > 100)) {
                            etMinValue.setError("Stock level must be between 0% and 100%");
                            isValid = false;
                        }
                    } catch (NumberFormatException e) {
                        etMinValue.setError("Invalid number format");
                        isValid = false;
                    }
                }

                // Validate max value
                if (!maxStr.isEmpty()) {
                    try {
                        double maxValue = Double.parseDouble(maxStr);

                        if (device.getType().equals("ColdChain") && (maxValue < -30 || maxValue > 20)) {
                            etMaxValue.setError("Temperature must be between -30°C and 20°C");
                            isValid = false;
                        } else if (device.getType().equals("ShelfWeight") && (maxValue < 0 || maxValue > 100)) {
                            etMaxValue.setError("Stock level must be between 0% and 100%");
                            isValid = false;
                        }

                        // Check min < max
                        if (!minStr.isEmpty()) {
                            double minValue = Double.parseDouble(minStr);
                            if (minValue >= maxValue) {
                                etMaxValue.setError("Max value must be greater than min value");
                                isValid = false;
                            }
                        }
                    } catch (NumberFormatException e) {
                        etMaxValue.setError("Invalid number format");
                        isValid = false;
                    }
                }

                if (!isValid) return;

                // For now, just simulate successful calibration
                // TODO: Update device configuration when Device model supports config

                listener.onDeviceCalibrated(device);
                dialog.dismiss();

                Toast.makeText(context, "Device calibrated successfully", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.show();
    }
}
