package com.gustavo.retailsenseiot.models;

public class MaintenanceTicket {
    private String id;
    private String deviceId;
    private String date;
    private String time;
    private String assignee;
    private String notes;
    private String description;
    private boolean resolved;
    private long timestamp;

    public MaintenanceTicket(String deviceId, String date, String time, String assignee, String notes) {
        this.deviceId = deviceId;
        this.date = date;
        this.time = time;
        this.assignee = assignee;
        this.notes = notes;
        this.id = java.util.UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.resolved = false;
        this.description = "Maintenance scheduled for " + date + " at " + time;
    }

    public String getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getAssignee() { return assignee; }
    public String getNotes() { return notes; }
    public String getDescription() { return description; }
    public boolean isResolved() { return resolved; }
    public long getTimestamp() { return timestamp; }

    public void setId(String id) { this.id = id; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setAssignee(String assignee) { this.assignee = assignee; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setDescription(String description) { this.description = description; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
