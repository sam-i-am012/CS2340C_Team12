package com.example.sprintproject.model;

import com.google.firebase.auth.FirebaseAuth;

public class LoginRepository {
    private FirebaseAuth mAuth;

    public LoginRepository() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, LoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getMessage());
                    }
                });
    }
}
