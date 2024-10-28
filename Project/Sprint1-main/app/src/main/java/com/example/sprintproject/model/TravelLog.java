package com.example.sprintproject.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class TravelLog {
    private String userId;
    private String destination;
    private String startDate;
    private String endDate;
    private List<String> associatedUsers;  // optional list of user IDs
    private Timestamp createdAt; // to be able to know which are the most recent ones

    public TravelLog(String userId, String destination, String startDate, String endDate,
                     List<String> associatedUsers) {
        this.userId = userId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.associatedUsers = associatedUsers;
        this.createdAt = Timestamp.now(); // auto set to current time

    }

    public TravelLog() { }

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

    public List<String> getAssociatedUsers() {
        return associatedUsers;
    }

    public void setAssociatedUsers(List<String> associatedUsers) {
        this.associatedUsers = associatedUsers;
    }

    // add a user to the associated users list
    public void addAssociatedUser(String userId) {
        if (!associatedUsers.contains(userId)) {
            associatedUsers.add(userId);
        }
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
