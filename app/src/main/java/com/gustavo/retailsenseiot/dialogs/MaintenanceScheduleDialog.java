package com.gustavo.retailsenseiot.dialogs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.models.MaintenanceTicket;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MaintenanceScheduleDialog {

    public interface OnMaintenanceScheduledListener {
        void onMaintenanceScheduled(MaintenanceTicket ticket);
    }

    public static void show(Context context, String deviceId, OnMaintenanceScheduledListener listener) {
        android.view.View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_maintenance_schedule, null);

        TextInputEditText etDate = dialogView.findViewById(R.id.etDate);
        TextInputEditText etTime = dialogView.findViewById(R.id.etTime);
        TextInputEditText etAssignee = dialogView.findViewById(R.id.etAssignee);
        TextInputEditText etNotes = dialogView.findViewById(R.id.etNotes);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Date picker
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    etDate.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            // V3: Maintenance dialog validates - cannot schedule in past
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Time picker
        etTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    etTime.setText(timeFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            );
            timePickerDialog.show();
        });

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Schedule Maintenance")
                .setView(dialogView)
                .setPositiveButton("Schedule", null) // Set null initially to override
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                // V3: Validation requirements
                String date = etDate.getText().toString().trim();
                String time = etTime.getText().toString().trim();
                String assignee = etAssignee.getText().toString().trim();
                String notes = etNotes.getText().toString().trim();

                // Validation
                if (date.isEmpty()) {
                    etDate.setError("Date is required");
                    return;
                }
                if (time.isEmpty()) {
                    etTime.setError("Time is required");
                    return;
                }
                if (assignee.isEmpty()) {
                    etAssignee.setError("Assignee is required");
                    return;
                }
                if (notes.isEmpty()) {
                    etNotes.setError("Notes are required");
                    return;
                }

                // Create maintenance ticket
                MaintenanceTicket ticket = new MaintenanceTicket(deviceId, date, time, assignee, notes);
                DataManager.addMaintenanceTicket(ticket);

                listener.onMaintenanceScheduled(ticket);
                dialog.dismiss();

                Toast.makeText(context, "Maintenance scheduled for " + date + " at " + time,
                    Toast.LENGTH_LONG).show();
            });
        });

        dialog.show();
    }
}
