package com.example.sprintproject.viewmodel;

import static com.example.sprintproject.model.InputValidator.isValidEmail;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Invitation;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.TravelLogValidator;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogisticsViewModel extends ViewModel {
    private FirestoreSingleton firestoreSingleton;
    private MutableLiveData<List<String>> userLocations = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Integer> plannedDaysLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> allocatedLiveData = new MutableLiveData<>();
    private MutableLiveData<Invitation> invitationLiveData = new MutableLiveData<>();


    public LogisticsViewModel() {
        firestoreSingleton = FirestoreSingleton.getInstance();
        loadUserLocations();
        loadTripDays();
        loadDuration();
        listenForInvitations(); // listen for new invitations to collab on a trip
    }

    // live data for invitations
    public LiveData<Invitation> getInvitationLiveData() {
        return invitationLiveData;
    }

    public LiveData<List<String>> getUserLocations() {
        return userLocations;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<Integer> getPlannedDaysLiveData() {
        return plannedDaysLiveData;
    }

    public MutableLiveData<Integer> getAllocatedLiveData() {
        return allocatedLiveData;
    }

    // load user's associated locations and update the LiveData
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
        allocatedLiveData = (MutableLiveData<Integer>) firestoreSingleton
                .getDurationForUser(currentUserId);
    }

    // invite a user to the trip after validating their email
    public void inviteUserToTrip(String email, String location) {
        if (!isValidEmail(email)) {
            toastMessage.setValue("Please enter a valid email address.");
            return;
        }

        // check if user invited is already an existing user
        firestoreSingleton.checkEmailExists(email).addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                // uid of invited user
                String invitedUser = task.getResult().getDocuments().get(0).getId();

                // data for Invitation class
                Map<String, Object> invitationData = new HashMap<>();
                // current user (inviter)
                invitationData.put("invitingUserId", firestoreSingleton.getCurrentUserId());
                invitationData.put("invitedUserId", invitedUser);  // user being invited
                invitationData.put("invitingUserEmail", email);  // inviter's email
                invitationData.put("tripLocation", location);  // location for the trip
                invitationData.put("status", "pending");  // initial status of the invitation
                invitationData.put("timestamp", FieldValue.serverTimestamp());  // timestamp


                // add the invitation to Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("invitations")
                        .add(invitationData)
                        .addOnSuccessListener(documentReference -> {
                            toastMessage.setValue("Invitation sent to " + email + " for location " + location);
                        })
                        .addOnFailureListener(e -> {
                            toastMessage.setValue("Error sending invitation: " + e.getMessage());
                        });
            } else {
                toastMessage.setValue("No account found for this email.");
            }
        });
    }

    // to listen for invitations to accept / deny them
    private void listenForInvitations() {
        String currentUserId = firestoreSingleton.getCurrentUserId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("invitations")
                .whereEqualTo("invitedUserId", currentUserId)
                .whereEqualTo("status", "pending") // only fetch pending invitations
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Error fetching invitations", error);
                        return;
                    }

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            Invitation invitation = doc.toObject(Invitation.class);
                            invitation.setInvitationId(doc.getId());  // store the document ID

                            // notify UI about the new invitation
                            invitationLiveData.setValue(invitation);
                        }
                    }
                });
    }

    // accept or reject invitation
    public void updateInvitationStatus(String invitationId, String status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("invitations")
                .document(invitationId)  // use the invitation ID stored in the Invitation model
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    toastMessage.setValue("Invitation " + status);
                    invitationLiveData.setValue(null);  // stop showing the dialog
                })
                .addOnFailureListener(e -> {
                    toastMessage.setValue("Error updating invitation: " + e.getMessage());
                    Log.d("Firestore", "Failed to update invitation with ID: "
                            + invitationId, e);
                });
    }

    // accept invitation
    public void acceptInvitation(Invitation invitation) {
        updateInvitationStatus(invitation.getInvitationId(), "accepted");
        firestoreSingleton.addUserToTrip(invitation.getInvitingUserId(),
                invitation.getInvitedUserId(), invitation.getTripLocation());
    }
}