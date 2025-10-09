package com.gustavo.retailsenseiot.models;

public class MaintenanceTicket {
    private String id;
    private String deviceId;
    private String description;
    private boolean resolved;
    private long timestamp;

    public String getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getDescription() { return description; }
    public boolean isResolved() { return resolved; }
    public long getTimestamp() { return timestamp; }

    public void setId(String id) { this.id = id; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setDescription(String description) { this.description = description; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
