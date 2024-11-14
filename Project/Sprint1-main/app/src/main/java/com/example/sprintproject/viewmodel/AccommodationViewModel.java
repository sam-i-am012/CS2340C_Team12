package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.view.AccommodationsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccommodationViewModel extends AndroidViewModel {
    private AccommodationsAdapter accommodationsAdapter = new AccommodationsAdapter();

    private FirestoreSingleton repository;
    private MutableLiveData<List<Accommodation>> accommodationLogs;
    private MutableLiveData<Result> resValidationResult = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> userLocationsWithIds = new MutableLiveData<>();

    public AccommodationViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreSingleton.getInstance();
        accommodationLogs = new MutableLiveData<>();
        loadUserLocations();
    }

    // Fetch accommodations for the current destination selected
    public void fetchAccommodationLogsForDestination(String travelId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            repository.getAccommodationLogsByUser(travelId).observeForever(accommodations -> {
                accommodationLogs.setValue(accommodations);
                Log.d("Accommodation", "travel id: " + travelId);
            });
        }
    }

    // Getter for accommodation logs
    public LiveData<List<Accommodation>> getAccommodationLogs() {
        return accommodationLogs;
    }

    // getter for user locations (used for spinner)
    public LiveData<Map<String, String>> getUserLocationsWithIds() {
        return userLocationsWithIds;
    }

    // Add an accommodation to the repository
    public void addAccommodation(Accommodation accommodation) {
        if (accommodationsAdapter != null) {
            repository.addAccommodation(accommodation, null);
            resValidationResult = new MutableLiveData<>();
        }
    }

    // load user locations (used for spinner)
    private void loadUserLocations() {
        String currentUserId = repository.getCurrentUserId();

        repository.getTravelLogsByUser(currentUserId).observeForever(travelLogs -> {
            if (travelLogs != null) {
                Map<String, String> locationsWithIds = new HashMap<>();
                for (TravelLog log : travelLogs) {
                    String destination = log.getDestination();
                    String documentId = log.getDocumentId();
                    locationsWithIds.put(destination, documentId);
                }
                userLocationsWithIds.setValue(locationsWithIds);
            }
        });
    }

    public AccommodationsAdapter getAccommodationsAdapter() {
        return accommodationsAdapter;
    }

    public MutableLiveData<Result> getResValidationResult() {
        return resValidationResult;
    }

}
