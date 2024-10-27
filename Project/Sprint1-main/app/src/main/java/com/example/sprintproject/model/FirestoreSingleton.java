package com.example.sprintproject.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class FirestoreSingleton {
    private static FirestoreSingleton instance;
    private FirebaseFirestore firestore;
    private MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();

    private FirestoreSingleton() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreSingleton getInstance() {
        if (instance == null) {
            instance = new FirestoreSingleton();
        }
        return instance;
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
        firestore.collection("travelLogs").add(log).addOnCompleteListener(listener);
    }

    public void prepopulateDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        getTravelLogsByUser(userId).observeForever(logs -> {
            if (logs.size() < 2) {
                addTravelLog(new TravelLog(userId, "Paris", "2023-12-01", "2023-12-10"), null);
                addTravelLog(new TravelLog(userId, "New York", "2023-11-15", "2023-11-20"), null);
            }
        });
    }

    // methods to add a user to a trip
    public void addUserToTrip(String tripId, String userId, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference tripRef = firestore.collection("trips").document(tripId);
        tripRef.update("users." + userId, true).addOnCompleteListener(onCompleteListener);
    }

    public void getUsersForTrip(String tripId, OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        firestore.collection("trips").document(tripId).get().addOnCompleteListener(onCompleteListener);
    }
}
