package com.example.sprintproject.model;

public class InputValidator {

    // Method to check if email is valid
    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Method to check if password is valid
    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }
}
