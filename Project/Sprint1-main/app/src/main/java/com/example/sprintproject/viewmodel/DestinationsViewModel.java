package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirebaseAuthManager;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.model.TravelLog;

import java.util.List;

public class DestinationsViewModel extends ViewModel {
    private FirestoreSingleton repository;
    private LiveData<List<TravelLog>> travelLogs;

    public DestinationsViewModel() {
        repository = FirestoreSingleton.getInstance();
        travelLogs = repository.getTravelLogs();
    }

    public LiveData<List<TravelLog>> getTravelLogs() {
        return travelLogs;
    }

    public void addTravelLog(TravelLog log) {
        repository.addTravelLog(log, task -> {
            // Handle success or error here if needed
        });
    }
}