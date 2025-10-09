package com.gustavo.retailsenseiot.models;

public class Device {
    private String id;
    private String name;
    private String type;
    private String storeId;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getStoreId() { return storeId; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
}
