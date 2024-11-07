package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FirestoreSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AccommodationViewModel {
    private FirestoreSingleton repository;
    private LiveData<List<Accommodation>> accommodationLogs;

    public AccommodationViewModel() {
        repository = FirestoreSingleton.getInstance();
    }

    public void fetchAccommodationLogsForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        accommodationLogs = repository.getAccommodationByUser(userId);
    }

    public void addDiningLog(Accommodation log) {
        repository.addAccommodation(log, null);
    }
}
