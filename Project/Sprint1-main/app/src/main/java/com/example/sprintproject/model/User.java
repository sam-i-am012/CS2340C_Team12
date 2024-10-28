package com.example.sprintproject.model;

import java.util.List;

public class User {
    private String email;
    private String userId;
    private List<String> associatedDestinations;  // list of destination IDs
    public User() {} // required no arg constructor

    public User(String userId, String email, List<String> associatedDestinations) {
        this.userId = userId;
        this.email = email;
        this.associatedDestinations = associatedDestinations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getAssociatedDestinations() {
        return associatedDestinations;
    }

    public void setAssociatedDestinations(List<String> associatedDestinations) {
        this.associatedDestinations = associatedDestinations;
    }
}
