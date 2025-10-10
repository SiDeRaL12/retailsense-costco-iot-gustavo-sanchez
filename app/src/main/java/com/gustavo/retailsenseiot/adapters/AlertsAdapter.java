package com.gustavo.retailsenseiot.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.databinding.ItemAlertBinding;
import com.gustavo.retailsenseiot.models.Alert;
import com.gustavo.retailsenseiot.models.Device;
import com.gustavo.retailsenseiot.models.Store;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertViewHolder> {
    private List<Alert> alerts;
    private final OnAlertClickListener onAcknowledge;
    private final OnAlertClickListener onSuppress;
    private final OnAlertClickListener onOpenDevice;

    public interface OnAlertClickListener {
        void onAlertClick(Alert alert);
    }

    public AlertsAdapter(List<Alert> alerts, OnAlertClickListener onAcknowledge,
                        OnAlertClickListener onSuppress, OnAlertClickListener onOpenDevice) {
        this.alerts = alerts;
        this.onAcknowledge = onAcknowledge;
        this.onSuppress = onSuppress;
        this.onOpenDevice = onOpenDevice;
    }

    public void updateAlerts(List<Alert> newAlerts) {
        this.alerts = newAlerts;
        notifyDataSetChanged();
    }

    public List<Alert> getCurrentAlerts() {
        return alerts;
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAlertBinding binding = ItemAlertBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new AlertViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        Device device = DataManager.getDeviceById(alert.getDeviceId());
        Store store = device != null ? DataManager.getStoreById(device.getStoreId()) : null;

        holder.binding.tvAlertMessage.setText(alert.getMessage());

        String deviceInfo = device != null ? device.getLabel() : "Unknown Device";
        String storeInfo = store != null ? store.getName() : "Unknown Store";
        holder.binding.tvAlertInfo.setText(deviceInfo + " • " + storeInfo);

        // Time formatting
        String timeAgo = "Just now"; // Simplified for demo
        holder.binding.tvAlertTime.setText(timeAgo);

        // Severity chip
        holder.binding.chipSeverity.setText(alert.getSeverity());
        if (alert.getSeverity().equals("Critical")) {
            holder.binding.chipSeverity.setChipBackgroundColorResource(R.color.status_critical);
        } else if (alert.getSeverity().equals("Warning")) {
            holder.binding.chipSeverity.setChipBackgroundColorResource(R.color.status_warning);
        } else {
            holder.binding.chipSeverity.setChipBackgroundColorResource(R.color.status_ok);
        }

        // Card click - open device
        holder.binding.getRoot().setOnClickListener(v -> onOpenDevice.onAlertClick(alert));

        // Overflow menu
        holder.binding.btnOverflow.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.alert_overflow_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_acknowledge) {
                    onAcknowledge.onAlertClick(alert);
                    return true;
                } else if (item.getItemId() == R.id.action_suppress) {
                    onSuppress.onAlertClick(alert);
                    return true;
                } else if (item.getItemId() == R.id.action_open_device) {
                    onOpenDevice.onAlertClick(alert);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        final ItemAlertBinding binding;

        AlertViewHolder(ItemAlertBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
