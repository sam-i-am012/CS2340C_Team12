package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.User;
import com.example.sprintproject.model.VacationTimeCalculator;
import com.example.sprintproject.model.ValidationManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DestinationsViewModel extends ViewModel {
    private FirestoreSingleton repository;
    private LiveData<List<TravelLog>> travelLogs;
    private VacationTimeCalculator vtCalculator = new VacationTimeCalculator();

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

    public Result validateMissingEntry(String startDate, String endDate, String duration) {
        return ValidationManager.validateMissingEntry(startDate, endDate, duration);
    }

    public Result validateDateRange(String startDate, String endDate) {
        return ValidationManager.validateDateRange(startDate, endDate);
    }

    public String calculateMissingEntry(String entry1, String entry2) {
        return vtCalculator.calculateEntry(entry1, entry2);
    }

    public void addDatesAndDuration(String userId, String startDate, String endDate, String duration) {
        repository.addDatesAndDuration(userId, startDate, endDate, duration);
    }

    public LiveData<User> getCurrentUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            return repository.getUserById(firebaseUser.getUid());
        }
        return null; // Handle user not logged in
    }
}