package com.gustavo.retailsenseiot.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RadioGroup;
import androidx.appcompat.app.AlertDialog;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.models.Alert;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlertSuppressDialog {

    public interface OnAlertSuppressedListener {
        void onAlertSuppressed(Alert alert);
    }

    public static void show(Context context, Alert alert, OnAlertSuppressedListener listener) {
        android.view.View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_alert_suppress, null);

        RadioGroup rgDuration = dialogView.findViewById(R.id.rgDuration);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Suppress Alert")
                .setMessage("How long should this alert be suppressed?")
                .setView(dialogView)
                .setPositiveButton("Suppress", (d, which) -> {
                    int selectedId = rgDuration.getCheckedRadioButtonId();
                    int hours = 1; // Default

                    if (selectedId == R.id.rb30Minutes) {
                        hours = 0; // 30 minutes = 0.5 hours, handle separately
                    } else if (selectedId == R.id.rb1Hour) {
                        hours = 1;
                    } else if (selectedId == R.id.rb4Hours) {
                        hours = 4;
                    } else if (selectedId == R.id.rb24Hours) {
                        hours = 24;
                    }

                    // Calculate suppression end time
                    Calendar calendar = Calendar.getInstance();
                    if (selectedId == R.id.rb30Minutes) {
                        calendar.add(Calendar.MINUTE, 30);
                    } else {
                        calendar.add(Calendar.HOUR_OF_DAY, hours);
                    }

                    SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                    String suppressUntil = iso8601Format.format(calendar.getTime());

                    DataManager.suppressAlert(alert.getId(), suppressUntil);
                    listener.onAlertSuppressed(alert);
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }
}
