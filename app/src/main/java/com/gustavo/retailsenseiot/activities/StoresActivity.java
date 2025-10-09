    }

    private void showMaintenanceDialog(Store store) {
        // TODO: Implement in Phase 9
        Toast.makeText(this, "Maintenance dialog for " + store.getName(), Toast.LENGTH_SHORT).show();
    }
}
package com.gustavo.retailsenseiot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.gustavo.retailsenseiot.adapters.StoresAdapter;
import com.gustavo.retailsenseiot.databinding.ActivityStoresBinding;
import com.gustavo.retailsenseiot.models.Store;
import com.gustavo.retailsenseiot.utils.DataManager;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoresActivity extends AppCompatActivity {
    private ActivityStoresBinding binding;
    private StoresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        List<Store> stores = DataManager.getStores();
        // S1: Sort stores by name
        Collections.sort(stores, new Comparator<Store>() {
            @Override
            public int compare(Store s1, Store s2) {
                return s1.getName().compareToIgnoreCase(s2.getName());
            }
        });

        if (stores.isEmpty()) {
            binding.rvStores.setVisibility(View.GONE);
            binding.emptyState.setVisibility(View.VISIBLE);
        } else {
            binding.rvStores.setVisibility(View.VISIBLE);
            binding.emptyState.setVisibility(View.GONE);

            adapter = new StoresAdapter(stores,
                store -> openStoreDashboard(store),
                store -> showMaintenanceDialog(store)
            );
            binding.rvStores.setLayoutManager(new LinearLayoutManager(this));
            binding.rvStores.setAdapter(adapter);
        }
    }

    private void openStoreDashboard(Store store) {
        Intent intent = new Intent(this, StoreDashboardActivity.class);
        intent.putExtra("storeId", store.getId());
        startActivity(intent);

