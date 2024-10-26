package com.example.sprintproject.model;

import java.util.Map;

public class Trip {

    private String location;
    private String startDate;
    private String endDate;
    private Map<String, Boolean> users;

    // Default constructor for Firebase
    public Trip() {}

    public Trip(String location, String startDate, String endDate, Map<String, Boolean> users) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.users = users;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }
}

