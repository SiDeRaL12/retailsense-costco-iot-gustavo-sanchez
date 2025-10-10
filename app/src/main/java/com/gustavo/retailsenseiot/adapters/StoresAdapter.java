package com.gustavo.retailsenseiot.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.gustavo.retailsenseiot.R;
import com.gustavo.retailsenseiot.databinding.ItemStoreBinding;
import com.gustavo.retailsenseiot.models.Store;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoreViewHolder> {
    private final List<Store> stores;
    private final OnStoreClickListener onStoreClick;
    private final OnScheduleMaintenanceListener onScheduleMaintenance;

    public interface OnStoreClickListener {
        void onStoreClick(Store store);
    }

    public interface OnScheduleMaintenanceListener {
        void onScheduleMaintenance(Store store);
    }

    public StoresAdapter(List<Store> stores, OnStoreClickListener onStoreClick,
                        OnScheduleMaintenanceListener onScheduleMaintenance) {
        this.stores = stores;
        this.onStoreClick = onStoreClick;
        this.onScheduleMaintenance = onScheduleMaintenance;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemStoreBinding binding = ItemStoreBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new StoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        Store store = stores.get(position);
        int activeAlerts = DataManager.getActiveAlertsForStore(store.getId()).size();

        holder.binding.tvStoreName.setText(store.getName());

        // Current time simulation
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        holder.binding.tvStoreSubtitle.setText(store.getCity() + " • Last updated " + currentTime);

        // S2: Status chip matches alert count
        if (activeAlerts == 0) {
            holder.binding.chipStatus.setText("OK");
            holder.binding.chipStatus.setChipBackgroundColorResource(R.color.status_ok);
        } else {
            holder.binding.chipStatus.setText(activeAlerts + " Issues");
            holder.binding.chipStatus.setChipBackgroundColorResource(
                activeAlerts >= 3 ? R.color.status_critical : R.color.status_warning);
        }

        // Card click
        holder.binding.getRoot().setOnClickListener(v -> onStoreClick.onStoreClick(store));

        // Overflow menu
        holder.binding.btnOverflow.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(R.menu.store_overflow_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_schedule_maintenance) {
                    onScheduleMaintenance.onScheduleMaintenance(store);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        final ItemStoreBinding binding;

        StoreViewHolder(ItemStoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

