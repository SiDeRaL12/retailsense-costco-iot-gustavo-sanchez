package com.gustavo.retailsenseiot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.gustavo.retailsenseiot.models.Device;
import java.util.List;

public class StoreDevicesAdapter extends RecyclerView.Adapter<StoreDevicesAdapter.DeviceViewHolder> {
    private final List<Device> devices;
    private final OnDeviceClickListener onDeviceClick;

    public interface OnDeviceClickListener {
        void onDeviceClick(Device device);
    }

    public StoreDevicesAdapter(List<Device> devices, OnDeviceClickListener onDeviceClick) {
        this.devices = devices;
        this.onDeviceClick = onDeviceClick;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.text1.setText(device.getLabel());
        holder.text2.setText(device.getType() + " • " + device.getStatus());
        holder.itemView.setOnClickListener(v -> onDeviceClick.onDeviceClick(device));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        DeviceViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}

