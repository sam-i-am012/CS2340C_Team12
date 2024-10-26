package com.example.sprintproject.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;

/**
 * Class to manage user access to trips
 */
public class UserAccessViewModel extends ViewModel {
       private FirestoreSingleton repository;
       public UserAccessViewModel() {
           repository = FirestoreSingleton.getInstance();
       }

       // functionality to add users to a specific trip
       public void addUsersToTrip(String tripId, String userId) {
           repository.addUsersToTrip(tripId, userId, task -> {
               if (task.isSuccessful()) {
                   // handle success
               } else {
                   // handle failure
               }
           });
       }


}
