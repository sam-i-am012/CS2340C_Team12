package com.example.sprintproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.ReservationValidator;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.view.DiningsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class DiningViewModel extends AndroidViewModel {
    DiningsAdapter diningAdapter = new DiningsAdapter();

    private FirestoreSingleton repository;
    private MutableLiveData<List<Dining>> diningLogs;
    private MutableLiveData<Result> resValidationResult = new MutableLiveData<>();

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
        if (diningAdapter != null) {
            repository.addDining(dining, null);
            resValidationResult = new MutableLiveData<>();
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
