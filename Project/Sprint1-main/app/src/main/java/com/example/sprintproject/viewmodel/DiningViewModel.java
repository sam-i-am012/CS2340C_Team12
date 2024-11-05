package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FirestoreSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DiningViewModel {
    private FirestoreSingleton repository;
    private LiveData<List<Dining>> diningLogs;

    public DiningViewModel() {
        repository = FirestoreSingleton.getInstance();
    }

    public void fetchDiningLogsForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        diningLogs = repository.getDiningByUser(userId);
    }

    public void addDiningLog(Dining log) {
        repository.addDining(log, null);
    }
}
