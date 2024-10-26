package com.example.sprintproject.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreSingleton {
    private static FirestoreSingleton instance;
    private FirebaseFirestore firestore;

    private FirestoreSingleton() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreSingleton getInstance() {
        if (instance == null) {
            instance = new FirestoreSingleton();
        }
        return instance;
    }

    public void addTravelLog(TravelLog log, OnCompleteListener<DocumentReference> listener) {
        firestore.collection("travelLogs").add(log).addOnCompleteListener(listener);
    }

    public LiveData<List<TravelLog>> getTravelLogs() {
        MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
        firestore.collection("travelLogs").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<TravelLog> travelLogs = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    TravelLog log = document.toObject(TravelLog.class);
                    travelLogs.add(log);
                }
                travelLogsLiveData.setValue(travelLogs);
            }
        });
        return travelLogsLiveData;
    }

    public void prepopulateDatabase() {
        getTravelLogs().observeForever(logs -> {
            if (logs.isEmpty()) {
                addTravelLog(new TravelLog("Paris", "2023-12-01", "2023-12-10"), null);
                addTravelLog(new TravelLog("New York", "2023-11-15", "2023-11-20"), null);
            }
        });
    }
}
