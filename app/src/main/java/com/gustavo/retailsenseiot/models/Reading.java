    public double getValue() { return value; }
    public long getTimestamp() { return timestamp; }

    public void setId(String id) { this.id = id; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setType(String type) { this.type = type; }
    public void setValue(double value) { this.value = value; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
package com.gustavo.retailsenseiot.models;

public class Reading {
    private String id;
    private String deviceId;
    private String type;
    private double value;
    private long timestamp;

    public String getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getType() { return type; }
