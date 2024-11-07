package com.example.sprintproject.model;

import java.util.List;

public class Accommodation {
    private String location;
    private String checkInTime;
    private String checkOutTime;
    private int numRooms;
    private List<String> roomType;
    private String userId;

    public Accommodation() {}
    public Accommodation(String location, String checkInTime, String checkOutTime, int numRooms, List<String> roomType, String userId) {
        this.location = location;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.numRooms = numRooms;
        this.roomType = roomType;
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public List<String> getRoomType() {
        return roomType;
    }

    public void setRoomType(List<String> roomType) {
        this.roomType = roomType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
