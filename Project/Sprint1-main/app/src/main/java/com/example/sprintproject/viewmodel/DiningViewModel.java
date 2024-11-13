package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.ReservationValidator;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.view.DiningsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiningViewModel extends AndroidViewModel {
    DiningsAdapter diningAdapter = new DiningsAdapter();

    private MutableLiveData<List<String>> userLocations = new MutableLiveData<>();
    private FirestoreSingleton repository;
    private MutableLiveData<List<Dining>> diningLogs;
    private MutableLiveData<List<Dining>> diningLogsByLocation;
    private MutableLiveData<Result> resValidationResult = new MutableLiveData<>();
    private MutableLiveData<Map<String, String>> userLocationsWithIds = new MutableLiveData<>();


    public DiningViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreSingleton.getInstance();
        diningLogs = new MutableLiveData<>();
        diningLogsByLocation = new MutableLiveData<>();
        loadUserLocations();
    }

    public LiveData<List<String>> getUserLocations() {
        return userLocations;
    }

    // load user's associated locations and update the LiveData

    private void loadUserLocations() {
        String currentUserId = repository.getCurrentUserId();

        repository.getTravelLogsByUser2(currentUserId).observeForever(querySnapshot -> {
            if (querySnapshot != null) {
                Map<String, String> locationsWithIds = new HashMap<>();
                for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                    String destination = doc.getString("destination"); // Assuming "destination" is the field name
                    String documentId = doc.getId(); // Get the Firestore document ID
                    locationsWithIds.put(destination, documentId);
                }
                userLocationsWithIds.setValue(locationsWithIds);
            }
        });
    }

    public LiveData<Map<String, String>> getUserLocationsWithIds() {
        return userLocationsWithIds;
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

    public void fetchDiningLogsForLocation(String locationId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || locationId.isEmpty()) {
            return;
        }
        String userId = user.getUid();
        Log.e("Dining", "fetching dining logs for location: " + locationId);
        repository.getDiningLogsByUserAndLocation(userId, locationId).observeForever(dinings -> {
            // Update LiveData with fetched reservations
            diningLogsByLocation.setValue(dinings);
        });
    }

    public LiveData<List<Dining>> getDiningLogs() {
        return diningLogs;
    }

    public LiveData<List<Dining>> getDiningLogsByLocation() {
        return diningLogsByLocation;
    }

    public void addDining(Dining dining) {
        if (diningAdapter != null) {
            repository.addDining(dining, null);
            resValidationResult = new MutableLiveData<>();

            fetchDiningLogsForLocation(dining.getTravelDestination());
            Log.d("DINING", "travel id: " + dining.getTravelDestination());
        }
    }

    public void addLog(Dining dining) {
        diningAdapter.addLog(dining);
    }

    public LiveData<Result> getResValidationResult() { return resValidationResult; }

    public void validateNewReservation(String name, String time, String location, String website) {
        Result finalResult;
        Result noMissingEntries = ReservationValidator.noMissingEntries(name, time, location, website);
        if (noMissingEntries.isSuccess()) {
            Result isValidTime = ReservationValidator.isValidTime(time);
            Result isValidWebsite = ReservationValidator.isValidWebsite(website);
            if (!isValidTime.isSuccess() && !isValidWebsite.isSuccess()) {
                finalResult = new Result(true, "Time and website entries are both invalid");
            } else if (!isValidTime.isSuccess()) {
                finalResult = isValidTime;
            } else if (!isValidWebsite.isSuccess()) {
                finalResult = isValidWebsite;
            } else {
                finalResult = new Result(true, "Reservation created successfully!");
            }
        } else {
            finalResult = noMissingEntries;
        }
        resValidationResult.setValue(finalResult);
    }

    public void resetResult() {
        resValidationResult = new MutableLiveData<>();
    }
}
