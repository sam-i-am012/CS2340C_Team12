package com.example.sprintproject.model;

public interface LoginCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
