package com.example.sprintproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FirestoreSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DiningViewModel extends AndroidViewModel {
    private FirestoreSingleton repository;
    private MutableLiveData<List<Dining>> diningLogs;

    public DiningViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreSingleton.getInstance();
        diningLogs = new MutableLiveData<>();
    }

    public void fetchDiningLogsForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            repository.getDiningByUser(userId).observeForever(dinings -> {
                // Update LiveData with fetched reservations
                diningLogs.setValue(dinings);
            });
        }
    }

    public LiveData<List<Dining>> getDiningLogs() {
        return diningLogs;
    }

    public void addDining(Dining dining) {
        repository.addDining(dining, null);
    }

    public void addDiningLog(Dining log) {
        repository.addDining(log, null);
    }

}
