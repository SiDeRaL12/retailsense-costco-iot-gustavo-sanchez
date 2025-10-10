package com.gustavo.retailsenseiot.models;

public class Alert {
    private String id;
    private String deviceId;
    private String message;
    private boolean ack;
    private long timestamp;
    private String severity;
    private String type;
    private boolean suppressed;
    private String suppressedUntil;

    public String getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getMessage() { return message; }
    public boolean isAck() { return ack; }
    public long getTimestamp() { return timestamp; }
    public String getSeverity() { return severity; }
    public String getType() { return type; }
    public boolean isSuppressed() { return suppressed; }
    public String getSuppressedUntil() { return suppressedUntil; }

    public void setId(String id) { this.id = id; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setMessage(String message) { this.message = message; }
    public void setAck(boolean ack) { this.ack = ack; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setType(String type) { this.type = type; }
    public void setSuppressed(boolean suppressed) { this.suppressed = suppressed; }
    public void setSuppressedUntil(String suppressedUntil) { this.suppressedUntil = suppressedUntil; }
}
