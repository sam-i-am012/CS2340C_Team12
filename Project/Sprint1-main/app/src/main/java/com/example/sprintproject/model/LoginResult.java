package com.example.sprintproject.model;

public class LoginResult {
    private boolean success;
    private String errorMessage;

    public LoginResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
