package com.example.sprintproject.viewmodel;

import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.TravelLogValidator;
import com.example.sprintproject.model.User;

import java.util.ArrayList;
import java.util.List;

public class LogisticsViewModel extends ViewModel {
    private FirestoreSingleton firestoreSingleton;
    private MutableLiveData<List<String>> userLocations = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Integer> plannedDaysLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> allocatedLiveData = new MutableLiveData<>();

    public LogisticsViewModel() {
        firestoreSingleton = FirestoreSingleton.getInstance();
        loadUserLocations();
        loadTripDays();
        loadDuration();
    }

    // Method to get user-associated locations from Firestore
    public LiveData<List<String>> getUserLocations() {
        return userLocations;
    }

    // getter for the toast message Live data
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<Integer> getPlannedDaysLiveData() {
        return plannedDaysLiveData;
    }

    public MutableLiveData<Integer> getAllocatedLiveData() {
        return allocatedLiveData;
    }

    // Load the user's associated locations and update the LiveData
    private void loadUserLocations() {
        String currentUserId = firestoreSingleton.getCurrentUserId();
        firestoreSingleton.getTravelLogsByUser(currentUserId).observeForever(travelLogs -> {
            List<String> locations = new ArrayList<>();
            for (TravelLog log : travelLogs) {
                locations.add(log.getDestination());
            }
            userLocations.setValue(locations);
        });
    }

    // for planned days
    public void loadTripDays() {
        String currentUserId = firestoreSingleton.getCurrentUserId();
        // Fetch trip data from Firestore and update LiveData accordingly
        firestoreSingleton.getTravelLogsByUser(currentUserId).observeForever(travelLogs -> {
            int totalDays = 0;
            for (TravelLog log : travelLogs) {
                totalDays += TravelLogValidator.calculateDays(log.getStartDate(), log.getEndDate());
            }
            plannedDaysLiveData.setValue(totalDays);
        });
    }

    // for allocated days
    public void loadDuration() {
        String currentUserId = firestoreSingleton.getCurrentUserId();
        allocatedLiveData = (MutableLiveData<Integer>) firestoreSingleton.getDurationForUser(currentUserId);
    }

    // Invite a user to the trip after validating their email
    public void inviteUserToTrip(String email, String location) {
        if (!isValidEmail(email)) {
            toastMessage.setValue("Please enter a valid email address.");
            return;
        }

        firestoreSingleton.checkEmailExists(email).addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                String uid = task.getResult().getDocuments().get(0).getId(); // Get the UID
                firestoreSingleton.addUserToTrip(uid, location);
                toastMessage.setValue("Invitation sent to " + email + " for location " + location);
            } else {
                toastMessage.setValue("No account found for this email.");
            }
        });
    }

    // Helper to check valid email format
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
