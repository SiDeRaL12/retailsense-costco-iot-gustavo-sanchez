package com.gustavo.retailsenseiot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.gustavo.retailsenseiot.models.Alert;
import com.gustavo.retailsenseiot.R;
import java.util.List;

public class DeviceAlertsAdapter extends RecyclerView.Adapter<DeviceAlertsAdapter.AlertViewHolder> {
    private final List<Alert> alerts;
    private final OnAlertClickListener onAlertClick;

    public interface OnAlertClickListener {
        void onAlertClick(Alert alert);
    }

    public DeviceAlertsAdapter(List<Alert> alerts, OnAlertClickListener onAlertClick) {
        this.alerts = alerts;
        this.onAlertClick = onAlertClick;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        holder.text1.setText(alert.getMessage());
        String alertDetails = holder.itemView.getContext().getString(R.string.alert_details, alert.getSeverity(), alert.getType());
        holder.text2.setText(alertDetails);
        holder.itemView.setOnClickListener(v -> onAlertClick.onAlertClick(alert));
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        AlertViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
