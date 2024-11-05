package com.example.sprintproject.model;

public class Dining {
    private String location;
    private String website;
    private int review;
    private String time;
    private String userId;

    public Dining() {}

    public Dining(String location, String website, int review, String time, String userId) {
        this.location = location;
        this.website = website;
        this.review = review;
        this.time = time;
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
