package com.example.sprintproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FirestoreSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AccommodationViewModel extends AndroidViewModel {

    private FirestoreSingleton repository;
    private MutableLiveData<List<Accommodation>> accommodationLogs;

    public AccommodationViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreSingleton.getInstance();
        accommodationLogs = new MutableLiveData<>();
    }

    public void fetchAccommodationLogsForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            repository.getAccommodationByUser(userId).observeForever(accommodations -> {
                // Update LiveData with fetched accommodations
                accommodationLogs.setValue(accommodations);
            });
        }
    }

    public LiveData<List<Accommodation>> getAccommodationLogs() {
        return accommodationLogs;
    }

    public void addAccommodation(Accommodation accommodation) {
        repository.addAccommodation(accommodation, null);
    }

    public void addAccommodationLog(Accommodation log) {
        repository.addAccommodation(log, null);
    }
}
