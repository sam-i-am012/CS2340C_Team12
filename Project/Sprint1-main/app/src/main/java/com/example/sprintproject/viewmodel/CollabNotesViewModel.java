package com.example.sprintproject.viewmodel;

import static com.example.sprintproject.model.InputValidator.isValidEmail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Invitation;
import com.example.sprintproject.model.Note;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.User;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollabNotesViewModel extends ViewModel {
    private FirestoreSingleton firestoreSingleton;
    private MutableLiveData<List<String>> userLocations = new MutableLiveData<>();
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();  // display notes


    public CollabNotesViewModel() {
        firestoreSingleton = FirestoreSingleton.getInstance();
        loadUserLocations();
        notesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<String>> getUserLocations() {
        return userLocations;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }


    // fetch notes for a specific location and current user
    public LiveData<List<Note>> getNotesForTravelLog(String location) {
        String currentUserId = firestoreSingleton.getCurrentUserId();
        firestoreSingleton.getNotesForTravelLog(location, currentUserId).observeForever(notes -> {
            // update the live data
            notesLiveData.setValue(notes);
        });
        return notesLiveData;
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

    public LiveData<List<User>> getCollaboratorsForLocation(String location) {
        return firestoreSingleton.getCollaboratorsForLocation(location,
                firestoreSingleton.getCurrentUserId());
    }

    public void addNoteToTravelLog(String location, String noteContent) {
        String userId = firestoreSingleton.getCurrentUserId();

        Note newNote = new Note(noteContent, userId);

        firestoreSingleton.addNoteToTravelLog(location, userId, newNote, task -> {
            if (task.isSuccessful()) {
                toastMessage.setValue("Note added successfully!");
            } else {
                toastMessage.setValue("Failed to add note.");
            }
        });
    }
}