package com.gustavo.retailsenseiot.models;

public class Store {
    private String id;
    private String name;
    private String location;
    private String manager;
    private String city;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getManager() { return manager; }
    public String getCity() { return city; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }
    public void setManager(String manager) { this.manager = manager; }
    public void setCity(String city) { this.city = city; }
}
