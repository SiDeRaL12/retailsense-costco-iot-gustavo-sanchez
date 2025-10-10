package com.gustavo.retailsenseiot.models;

public class BeaconReading extends Reading {
    private long timestamp;
    private int dwellSec;

    public BeaconReading(long timestamp, int dwellSec) {
        this.timestamp = timestamp;
        this.dwellSec = dwellSec;
    }

    @Override
    public long getTimestamp() { return timestamp; }
    public int getDwellSec() { return dwellSec; }
}
