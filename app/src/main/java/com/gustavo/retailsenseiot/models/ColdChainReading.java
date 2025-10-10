package com.gustavo.retailsenseiot.models;

public class ColdChainReading extends Reading {
    private long timestamp;
    private double tempC;
    private int alertCode;

    public ColdChainReading(long timestamp, double tempC, int alertCode) {
        this.timestamp = timestamp;
        this.tempC = tempC;
        this.alertCode = alertCode;
    }

    @Override
    public long getTimestamp() { return timestamp; }
    public double getTempC() { return tempC; }
    public int getAlertCode() { return alertCode; }
}
