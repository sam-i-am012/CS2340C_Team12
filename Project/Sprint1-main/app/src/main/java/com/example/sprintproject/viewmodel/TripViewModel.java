package com.example.sprintproject.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.MutableCreationExtras;

import com.example.sprintproject.model.Trip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class TripViewModel extends ViewModel {
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("trips");

    private MutableLiveData<List<Trip>> tripsLiveData = new MutableLiveData<>();
    public LiveData<List<Trip>> getTripsLiveData() {
        return tripsLiveData;
    }

    // fetch trips from the Firebase database
    public void fetchTrips() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Trip> tripList = new ArrayList<>();
                for (DataSnapshot tripSnapshot : snapshot.getChildren()) {
                    Trip trip = tripSnapshot.getValue(Trip.class);
                    if (trip != null) {
                        tripList.add(trip);
                    }
                }
                tripsLiveData.setValue(tripList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    // Function to add a new trip
    public void addTrip(Trip trip) {
        String newTripId = databaseRef.push().getKey();
        if (newTripId != null) {
            databaseRef.child(newTripId).setValue(trip);
        }
    }

    // to check if a user has access to a trip
    public void hasUserAccessToTrip(String tripId, String userId, ValueEventListener listener) {
        DatabaseReference userAccessRef = databaseRef.child(tripId).child("users").child(userId);
        userAccessRef.addListenerForSingleValueEvent(listener);
    }

}
