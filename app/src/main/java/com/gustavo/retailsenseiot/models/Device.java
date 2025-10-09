package com.gustavo.retailsenseiot.models;

public class Device {
    private String id;
    private String name;
    private String type;
    private String storeId;
    private String label;
    private String status;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getStoreId() { return storeId; }
    public String getLabel() { return label != null ? label : name; }
    public String getStatus() { return status; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public void setLabel(String label) { this.label = label; }
    public void setStatus(String status) { this.status = status; }
}
