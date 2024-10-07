package com.example.sprintproject.model;

import android.widget.Toast;

import com.example.sprintproject.view.CreateAccountActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;

public class FirebaseAuthManager {
    private final FirebaseAuth mAuth;

    public FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> createAccount(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
}

