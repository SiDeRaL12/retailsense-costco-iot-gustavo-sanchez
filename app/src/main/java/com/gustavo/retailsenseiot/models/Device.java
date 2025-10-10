package com.gustavo.retailsenseiot.models;

public class Device {
    private String id;
    private String name;
    private String type;
    private String storeId;
    private String label;
    private String status;
    private Config config;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getStoreId() { return storeId; }
    public String getLabel() { return label != null ? label : name; }
    public String getStatus() { return status; }
    public Config getConfig() { return config; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public void setLabel(String label) { this.label = label; }
    public void setStatus(String status) { this.status = status; }
    public void setConfig(Config config) { this.config = config; }

    public static class Config {
        private Double minC;
        private Double maxC;
        private Double minPct;
        private Double baselineKWh;

        public Double getMinC() { return minC; }
        public void setMinC(Double minC) { this.minC = minC; }
        public Double getMaxC() { return maxC; }
        public void setMaxC(Double maxC) { this.maxC = maxC; }
        public Double getMinPct() { return minPct; }
        public void setMinPct(Double minPct) { this.minPct = minPct; }
        public Double getBaselineKWh() { return baselineKWh; }
        public void setBaselineKWh(Double baselineKWh) { this.baselineKWh = baselineKWh; }
    }
}
