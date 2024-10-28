package com.example.sprintproject.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirestoreSingleton {
    private static FirestoreSingleton instance;
    private FirebaseFirestore firestore;
    private MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
    private FirebaseAuth auth;

    private FirestoreSingleton() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FirestoreSingleton getInstance() {
        if (instance == null) {
            instance = new FirestoreSingleton();
        }
        return instance;
    }

    public String getCurrentUserId() {
        return auth.getCurrentUser().getUid();
    }

    public LiveData<List<TravelLog>> getTravelLogsByUser(String userId) {
        MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
        firestore.collection("travelLogs")
                .whereEqualTo("userId", userId) // Query logs for this user
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            return; // Handle error
                        }
                        List<TravelLog> travelLogs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            TravelLog log = document.toObject(TravelLog.class);
                            travelLogs.add(log);
                        }
                        travelLogsLiveData.setValue(travelLogs);
                    }
                });
        return travelLogsLiveData;
    }

    public void addTravelLog(TravelLog log, OnCompleteListener<DocumentReference> listener) {
        // When creating a new travel log, ensure that the creator's userId is added to associatedUserIds
        if (!log.getAssociatedUsers().contains(log.getUserId())) {
            log.addAssociatedUser(log.getUserId());
        }

        firestore.collection("travelLogs")
                .add(log)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the ID of the newly created travel log
                        String travelLogId = task.getResult().getId();

                        // Update the user's associatedDestinations
                        updateUserAssociatedDestinations(log.getUserId(), travelLogId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }

    private void updateUserAssociatedDestinations(String userId, String travelLogId) {
        firestore.collection("users").document(userId)
                .update("associatedDestinations", FieldValue.arrayUnion(travelLogId));
    }

    public void prepopulateDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        getTravelLogsByUser(userId).observeForever(logs -> {
            if (logs.size() < 2) {
                addTravelLog(new TravelLog(userId, "Paris", "2023-12-01", "2023-12-10",
                        new ArrayList<>(Arrays.asList(userId))), null);
                addTravelLog(new TravelLog(userId, "New York", "2023-11-15", "2023-11-20",
                        new ArrayList<>(Arrays.asList(userId))), null);
            }
        });
    }

    // method to check if an email exists in the users collection
    public Task<QuerySnapshot> checkEmailExists(String email) {
        return firestore.collection("users")
                .whereEqualTo("email", email)
                .get();
    }

    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        firestore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        User user = task.getResult().toObject(User.class);
                        userLiveData.setValue(user);
                    } else {
                        userLiveData.setValue(null); // Handle the case where the user doesn't exist
                    }
                });

        return userLiveData;
    }

    public void addUserToTrip(String uid, String location) {
        // TODO: Add logic to add user to the trip in Firestore
    }

    // Adds startDate, endDate, and duration to their respective locations in a specific user's
    // database entry
    public void addDatesAndDuration(String userId, String startDate, String endDate, String duration) {
        firestore.collection("users").document(userId).update("startDate", startDate);
        firestore.collection("users").document(userId).update("endDate", endDate);
        firestore.collection("users").document(userId).update("duration", duration);
    }
}