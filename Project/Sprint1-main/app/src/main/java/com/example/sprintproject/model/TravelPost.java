package com.example.sprintproject.model;

public class TravelPost {
    private String start_date;
    private String end_date;
    private String destination;
    private String accommodations;
    private String reservations;
    private String notes;
    private String userId;

    public TravelPost() {}

    public TravelPost(String start_date, String end_date, String destination, String accommodations,
                      String reservations, String notes, String userId) {
        this.start_date = start_date;
        this.end_date = end_date;
        this.destination = destination;
        this.accommodations = accommodations;
        this.reservations = reservations;
        this.notes = notes;
        this.userId = userId;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(String accommodations) {
        this.accommodations = accommodations;
    }

    public String getReservations() {
        return reservations;
    }

    public void setReservations(String reservations) {
        this.reservations = reservations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
