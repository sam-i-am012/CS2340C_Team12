package com.example.sprintproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.ReservationValidator;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.view.AccommodationsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class AccommodationViewModel extends AndroidViewModel {
    AccommodationsAdapter accommodationsAdapter = new AccommodationsAdapter();

    private FirestoreSingleton repository;
    private MutableLiveData<List<Accommodation>> accommodationLogs;
    private MutableLiveData<Result> resValidationResult = new MutableLiveData<>();

    public AccommodationViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreSingleton.getInstance();
        accommodationLogs = new MutableLiveData<>();
    }

    // Fetch accommodations for the current user from Firestore
    public void fetchAccommodationLogsForCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            repository.getAccommodationLogsByUser(userId).observeForever(accommodations -> {
                accommodationLogs.setValue(accommodations);
            });
        }
    }

    // Getter for accommodation logs
    public LiveData<List<Accommodation>> getAccommodationLogs() {
        return accommodationLogs;
    }

    // Add an accommodation to the repository
    public void addAccommodation(Accommodation accommodation) {
        if (accommodationsAdapter != null) {
            repository.addAccommodation(accommodation, null);
            resValidationResult = new MutableLiveData<>();
        }
    }
}
