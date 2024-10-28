package com.example.sprintproject.model;

import com.google.android.gms.tasks.Continuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FirebaseAuthManager {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore firestore;

    public FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public Task<AuthResult> login(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password)
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        // save user's email and UID to firestone after successful login
                        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        String userEmail = mAuth.getCurrentUser().getEmail();

                        // Create a User object to store
                        User user = new User(userEmail, userId, new ArrayList<>());

                        // Save the user data in Firestore and return the original AuthResult
                        return firestore.collection("users")
                                .document(userId)
                                .set(user)
                                .continueWith(t -> task.getResult()); // Return the original AuthResult
                    } else {
                        // if login failed, propagate the error
                        throw Objects.requireNonNull(task.getException());
                    }
                });
    }

    public Task<AuthResult> createAccount(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }
}

