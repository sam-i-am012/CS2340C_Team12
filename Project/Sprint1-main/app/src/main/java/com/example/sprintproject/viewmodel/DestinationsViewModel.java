package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.TravelLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DestinationsViewModel extends ViewModel {
    private FirestoreSingleton repository;
    private LiveData<List<TravelLog>> travelLogs;

    public DestinationsViewModel() {
        repository = FirestoreSingleton.getInstance();
    }

    public void fetchTravelLogsForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        travelLogs = repository.getTravelLogsByUser(userId);
    }

    public LiveData<List<TravelLog>> getTravelLogs() {
        return travelLogs;
    }

    public void addTravelLog(TravelLog log) {
        repository.addTravelLog(log, null);
    }
}