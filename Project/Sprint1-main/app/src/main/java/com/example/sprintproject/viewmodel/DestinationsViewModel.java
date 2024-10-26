package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.MissingEntryManager;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.VacationTimeCalculator;

import java.util.List;

public class DestinationsViewModel extends ViewModel {
    private FirestoreSingleton repository;
    private VacationTimeCalculator vtCalculator;
    private MissingEntryManager missingEntryManager = new MissingEntryManager();

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

    public Result getMissingEntry(String startDate, String endDate, String duration) {
        return missingEntryManager.getMissingEntry(startDate, endDate, duration);
    }

    public String calculateMissingEntry(String entry1, String entry2) {
        return missingEntryManager.calculateEntry(entry1, entry2);
    }
}