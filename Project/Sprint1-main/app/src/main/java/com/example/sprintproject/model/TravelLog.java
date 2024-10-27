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

    public TravelLog() {}

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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
}