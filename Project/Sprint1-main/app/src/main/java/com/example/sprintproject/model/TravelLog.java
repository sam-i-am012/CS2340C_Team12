package com.example.sprintproject.model;

public class TravelLog {
    private String userId;
    private String destination;
    private String startDate;
    private String endDate;

    // Add a constructor that includes userId
    public TravelLog(String userId, String destination, String startDate, String endDate) {
        this.userId = userId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public String getDestination() {
        return destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}