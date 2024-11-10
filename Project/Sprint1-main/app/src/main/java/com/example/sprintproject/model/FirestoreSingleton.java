package com.example.sprintproject.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreSingleton {
    private static FirestoreSingleton instance;
    private FirebaseFirestore firestore;
    private MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Dining>> diningLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Accommodation>> accommodationLiveData = new MutableLiveData<>();
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


    // fetch duration (allocated days) for the current user
    public LiveData<Integer> getDurationForUser(String userId) {
        MutableLiveData<Integer> durationLiveData = new MutableLiveData<>();

        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();

                        // check if 'duration' field exists
                        if (document.exists() && document.contains("duration")) {
                            Object durationObj = document.get("duration");
                            if (durationObj instanceof Number) {
                                durationLiveData.setValue(((Number) durationObj).intValue());
                            } else if (durationObj instanceof String) {
                                try {
                                    // try parsing duration if it's a string
                                    int parsedDuration = Integer.parseInt((String) durationObj);
                                    durationLiveData.setValue(parsedDuration);
                                } catch (NumberFormatException e) {
                                    // invalid num
                                    durationLiveData.setValue(0);
                                }
                            } else {
                                durationLiveData.setValue(0); // default if type is unexpected
                            }
                        } else {
                            durationLiveData.setValue(0); // when document doesn't exist
                        }
                    } else {
                        durationLiveData.setValue(0); // failure happened
                    }
                });

        return durationLiveData; // LiveData to be observed
    }

    public LiveData<List<TravelLog>> getTravelLogsByUser(String userId) {
        MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
        firestore.collection("travelLogs")
                .whereEqualTo("userId", userId) // query logs for this user
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return; // to avoid null pointer
                    }
                    List<TravelLog> travelLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        TravelLog log = document.toObject(TravelLog.class);
                        travelLogs.add(log);
                    }
                    travelLogsLiveData.setValue(travelLogs);
                });
        return travelLogsLiveData;
    }

    public LiveData<List<TravelLog>> getLastFiveTravelLogsByUser(String userId) {
        MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
        firestore.collection("travelLogs")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(5) // Limit to the last 5 entries
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
        // Automatically set createdAt
        log.setCreatedAt(Timestamp.now());

        // When creating a new travel log, ensure that the creator's userId is added to
        // associatedUserIds
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

    // adds new travel log ID to the asssociatedDestinations array field for specific uer
    private void updateUserAssociatedDestinations(String userId, String travelLogId) {
        firestore.collection("users").document(userId)
                .update("associatedDestinations", FieldValue.arrayUnion(travelLogId));
    }

    // synchronizes the associatedDestinations field (will be used at the login screen in case
    // destinations were manually removed from database)
    public void syncUserAssociatedDestinationsOnLogin(String userId, OnCompleteListener<Void> onCompleteListener) {
        // get all valid destinations from the travelLogs collection associated with the userId
        firestore.collection("travelLogs")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> validDestinations = new ArrayList<>();

                    // collect all destination IDs from travelLogs that match the user
                    for (DocumentSnapshot destinationDoc : querySnapshot) {
                        validDestinations.add(destinationDoc.getId());
                    }

                    // update the user's associated destinations with only the valid destinations
                    firestore.collection("users").document(userId)
                            .update("associatedDestinations", validDestinations)
                            .addOnCompleteListener(onCompleteListener);
                });
    }

    public void prepopulateDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        LiveData<List<TravelLog>> travelLogsLiveData = getTravelLogsByUser(userId);
        travelLogsLiveData.observeForever(new Observer<List<TravelLog>>() {
            @Override
            public void onChanged(List<TravelLog> logs) {
                if (logs.size() < 2) {
                    addTravelLog(new TravelLog(userId, "Paris", "2023-12-01", "2023-12-10",
                            new ArrayList<>(Arrays.asList(userId))), null);
                    addTravelLog(new TravelLog(userId, "New York", "2023-11-15", "2023-11-20",
                            new ArrayList<>(Arrays.asList(userId))), null);
                }
                travelLogsLiveData.removeObserver(this);
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
        // todo: Add logic to add user to the trip in Firestore
    }

    // Adds startDate, endDate, and duration to their respective locations in a specific user's
    // database entry
    public void addDatesAndDuration(String userId, String startDate, String endDate,
                                    String duration) {
        // Create a map to hold the fields and their values
        Map<String, Object> updates = new HashMap<>();
        updates.put("startDate", startDate);
        updates.put("endDate", endDate);
        updates.put("duration", duration);

        // Perform the update with all fields at once
        firestore.collection("users").document(userId).update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated the document
                    Log.d("Firestore", "DocumentSnapshot successfully updated!");
                })
                .addOnFailureListener(e -> {
                    // Failed to update the document
                    Log.w("Firestore", "Error updating document", e);
                });
    }

    public void addDining (Dining dining, OnCompleteListener<DocumentReference> listener){
        firestore.collection("dining")
                .add(dining)
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        String diningId = task.getResult().getId();

                        updateUserAssociatedDestinations(dining.getUserId(), diningId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }

    public LiveData<List<Dining>> getDiningByUser(String userId) {
        MutableLiveData<List<Dining>> diningLiveData = new MutableLiveData<>();
        firestore.collection("dining")
                .whereEqualTo("userId", userId) // query logs for this user
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return; // to avoid null pointer
                    }
                    List<Dining> diningLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        //TravelLog log = document.toObject(TravelLog.class);
                        Dining log = document.toObject(Dining.class);
                        diningLogs.add(log);
                        //travelLogs.add(log);
                    }
                    diningLiveData.setValue(diningLogs);
                });
        return diningLiveData;
    }

    public void addAccommodation (Accommodation accommodation, OnCompleteListener<DocumentReference> listener){
        firestore.collection("accommodation")
                .add(accommodation)
                .addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        String accommodationId = task.getResult().getId();

                        updateUserAssociatedDestinations(accommodation.getUserId(), accommodationId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }

    public LiveData<List<Accommodation>> getAccommodationByUser(String userId) {
        MutableLiveData<List<Accommodation>> accommodationLiveData = new MutableLiveData<>();
        firestore.collection("accommodation")
                .whereEqualTo("userId", userId) // query logs for this user
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return; // to avoid null pointer
                    }
                    List<Accommodation> accommodationLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Accommodation log = document.toObject(Accommodation.class);
                        accommodationLogs.add(log);
                    }
                    accommodationLiveData.setValue(accommodationLogs);
                });
        return accommodationLiveData;
    }

}