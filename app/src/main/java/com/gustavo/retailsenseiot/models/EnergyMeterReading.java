package com.gustavo.retailsenseiot.models;

public class EnergyMeterReading extends Reading {
    private long timestamp;
    private double kWh;

    public EnergyMeterReading(long timestamp, double kWh) {
        this.timestamp = timestamp;
        this.kWh = kWh;
    }

    @Override
    public long getTimestamp() { return timestamp; }
    public double getKWh() { return kWh; }
}
