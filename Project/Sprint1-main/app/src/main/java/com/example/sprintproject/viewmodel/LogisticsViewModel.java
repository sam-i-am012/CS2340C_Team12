package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to manage user access to trips
 */
public class LogisticsViewModel extends ViewModel {
       private FirestoreSingleton repository;
       private MutableLiveData<List<String>> usersLiveData;

       public LogisticsViewModel() {
           repository = FirestoreSingleton.getInstance();
           usersLiveData = new MutableLiveData<>();
       }

       public LiveData<List<String>> getUsersLIveData() {
           return usersLiveData;
       }

       // functionality to add users to a specific trip
       public void addUsersToTrip(String tripId, String userId) {
           repository.addUserToTrip(tripId, userId, task -> {
               if (task.isSuccessful()) {
                   // handle success
               } else {
                   // handle failure
               }
           });
       }

       public void fetchUsersForTrip(String tripId) {
           repository.getUsersForTrip(tripId, task -> {
               if (task.isSuccessful()) {

                   DocumentSnapshot document = task.getResult();
                   if (document.exists()) {
                       Map<String, Boolean> usersMap = (Map<String, Boolean>) document.get("users");
                       List<String> userIds = new ArrayList<>(usersMap.keySet());
                       usersLiveData.setValue(userIds);
                   }
               }
           });
       }





}
