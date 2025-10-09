package com.gustavo.retailsenseiot.models;

public class ShelfWeightReading extends Reading {
    private long timestamp;
    private double pctFull;

    public ShelfWeightReading(long timestamp, double pctFull) {
        this.timestamp = timestamp;
        this.pctFull = pctFull;
    }

    @Override
    public long getTimestamp() { return timestamp; }
    public double getPctFull() { return pctFull; }
}
