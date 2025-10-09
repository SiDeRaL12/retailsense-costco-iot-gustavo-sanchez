package com.gustavo.retailsenseiot.models;

public class Alert {
    private String id;
    private String deviceId;
    private String message;
    private boolean ack;
    private long timestamp;

    public String getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getMessage() { return message; }
    public boolean isAck() { return ack; }
    public long getTimestamp() { return timestamp; }

    public void setId(String id) { this.id = id; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setMessage(String message) { this.message = message; }
    public void setAck(boolean ack) { this.ack = ack; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
