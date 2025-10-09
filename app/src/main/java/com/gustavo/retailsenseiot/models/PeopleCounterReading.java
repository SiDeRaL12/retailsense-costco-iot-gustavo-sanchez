package com.gustavo.retailsenseiot.models;

public class PeopleCounterReading extends Reading {
    private long timestamp;
    private int count;

    public PeopleCounterReading(long timestamp, int count) {
        this.timestamp = timestamp;
        this.count = count;
    }

    @Override
    public long getTimestamp() { return timestamp; }
    public int getCount() { return count; }
}
