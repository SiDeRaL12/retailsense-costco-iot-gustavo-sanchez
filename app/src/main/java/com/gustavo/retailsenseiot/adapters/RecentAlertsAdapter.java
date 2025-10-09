package com.gustavo.retailsenseiot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.models.Alert;
import java.util.List;

public class RecentAlertsAdapter extends RecyclerView.Adapter<RecentAlertsAdapter.AlertViewHolder> {
    private final List<Alert> alerts;
    private final OnAlertClickListener onAlertClick;

    public interface OnAlertClickListener {
        void onAlertClick(Alert alert);
    }

    public RecentAlertsAdapter(List<Alert> alerts, OnAlertClickListener onAlertClick) {
        this.alerts = alerts;
        this.onAlertClick = onAlertClick;
    }

    @Override
    public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlertViewHolder holder, int position) {
        Alert alert = alerts.get(position);
        holder.text1.setText(alert.getMessage());
        holder.text2.setText(alert.getSeverity() + " • " + alert.getType());
        holder.itemView.setOnClickListener(v -> onAlertClick.onAlertClick(alert));
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        AlertViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}

