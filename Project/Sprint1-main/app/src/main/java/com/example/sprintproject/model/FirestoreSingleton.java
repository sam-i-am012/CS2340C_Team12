package com.example.sprintproject.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FirestoreSingleton {
    private static FirestoreSingleton instance;
    private FirebaseFirestore firestore;
    private MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> userLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Dining>> diningLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Accommodation>> accommodationLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Post>> travelCommunityLiveData = new MutableLiveData<List<Post>>();
    private FirebaseAuth auth;

    private FirestoreSingleton() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FirestoreSingleton getInstance() {
        if (instance == null) {
            instance = new FirestoreSingleton();
        }
        return instance;
    }

    public String getCurrentUserId() {
        return auth.getCurrentUser().getUid();
    }


    // Fetch duration (allocated days) for the current user
    public LiveData<Integer> getDurationForUser(String userId) {
        MutableLiveData<Integer> durationLiveData = new MutableLiveData<>();

        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();

                        // check if 'duration' field exists
                        if (document.exists() && document.contains("duration")) {
                            Object durationObj = document.get("duration");
                            if (durationObj instanceof Number) {
                                durationLiveData.setValue(((Number) durationObj).intValue());
                            } else if (durationObj instanceof String) {
                                try {
                                    // try parsing duration if it's a string
                                    int parsedDuration = Integer.parseInt((String) durationObj);
                                    durationLiveData.setValue(parsedDuration);
                                } catch (NumberFormatException e) {
                                    // invalid num
                                    durationLiveData.setValue(0);
                                }
                            }
                        }
                    } else {
                        durationLiveData.setValue(0); // failure happened
                    }
                });

        return durationLiveData;
    }

    public LiveData<List<TravelLog>> getTravelLogsByUser(String userId) {
        MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
        firestore.collection("travelLogs")
                // use arrayContains to check if userId is in the associatedUsers array
                .whereArrayContains("associatedUsers", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    List<TravelLog> travelLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        TravelLog log = document.toObject(TravelLog.class);
                        travelLogs.add(log);
                    }
                    travelLogsLiveData.setValue(travelLogs);
                });
        return travelLogsLiveData;
    }

    public LiveData<List<TravelLog>> getLastFiveTravelLogsByUser(String userId) {
        MutableLiveData<List<TravelLog>> travelLogsLiveData = new MutableLiveData<>();
        firestore.collection("travelLogs")
                // use arrayContains to check if userId is in the associatedUsers array
                .whereArrayContains("associatedUsers", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)  // creation date, newest first
                .limit(5)  // limit to the last 5 entries
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }
                    List<TravelLog> travelLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        TravelLog log = document.toObject(TravelLog.class);
                        travelLogs.add(log);
                    }
                    travelLogsLiveData.setValue(travelLogs);
                });
        return travelLogsLiveData;
    }

    public void addTravelLog(TravelLog log, OnCompleteListener<DocumentReference> listener) {
        // Automatically set createdAt
        log.setCreatedAt(Timestamp.now());

        // ensure that the creator's userId is added to associatedUserIds
        if (!log.getAssociatedUsers().contains(log.getUserId())) {
            log.addAssociatedUser(log.getUserId());
        }

        firestore.collection("travelLogs")
                .add(log)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String travelLogId = task.getResult().getId();

                        // Update user's associatedDestinations
                        updateUserAssociatedDestinations(log.getUserId(), travelLogId);

                        // set documentId in the TravelLog object
                        String documentId = task.getResult().getId();

                        // update travel log id
                        firestore.collection("travelLogs").document(documentId)
                                .update("documentId", documentId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }


    // adds new travel log ID to the asssociatedDestinations array field for specific user
    private void updateUserAssociatedDestinations(String userId, String travelLogId) {
        firestore.collection("users").document(userId)
                .update("associatedDestinations", FieldValue.arrayUnion(travelLogId));
    }

    // synchronizes the associatedDestinations field (will be used at the login screen in case
    // destinations were manually removed from database)
    public void syncUserAssociatedDestinationsOnLogin(String userId,
                                                      OnCompleteListener<Void> onCompleteListener) {
        // get all valid destinations from the travelLogs collection associated with the userId
        firestore.collection("travelLogs")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> validDestinations = new ArrayList<>();

                    // collect all destination IDs from travelLogs that match the user
                    for (DocumentSnapshot destinationDoc : querySnapshot) {
                        validDestinations.add(destinationDoc.getId());
                    }

                    // update the user's associated destinations with only the valid destinations
                    firestore.collection("users").document(userId)
                            .update("associatedDestinations", validDestinations)
                            .addOnCompleteListener(onCompleteListener);
                });
    }

    public void prepopulateDatabase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        LiveData<List<TravelLog>> travelLogsLiveData = getTravelLogsByUser(userId);
        travelLogsLiveData.observeForever(new Observer<List<TravelLog>>() {
            @Override
            public void onChanged(List<TravelLog> logs) {
                if (logs.size() < 2) {
                    addTravelLog(new TravelLog(userId, "Paris", "2023-12-01", "2023-12-10",
                            new ArrayList<>(Arrays.asList(userId)), new ArrayList<>()), null);
                    addTravelLog(new TravelLog(userId, "New York", "2023-11-15", "2023-11-20",
                            new ArrayList<>(Arrays.asList(userId)), new ArrayList<>()), null);
                }
                travelLogsLiveData.removeObserver(this);
            }
        });
    }

    // method to check if an email exists in the users collection
    public Task<QuerySnapshot> checkEmailExists(String email) {
        return firestore.collection("users")
                .whereEqualTo("email", email)
                .get();
    }

    public LiveData<User> getUserById(String userId) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();

        firestore.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        User user = task.getResult().toObject(User.class);
                        userLiveData.setValue(user);
                    } else {
                        userLiveData.setValue(null); // Handle the case where the user doesn't exist
                    }
                });

        return userLiveData;
    }

    public LiveData<List<User>> getCollaboratorsForLocation(String location, String currentUserId,
                                                            String documentId) {
        MutableLiveData<List<User>> collaboratorsLiveData = new MutableLiveData<>();

        // travel log matches current user
        firestore.collection("travelLogs")
                .whereEqualTo("destination", location)
                .whereEqualTo("documentId", documentId)
                .whereArrayContains("associatedUsers", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        List<String> userIds = new ArrayList<>();

                        // grab all the user ids
                        for (DocumentSnapshot document : task.getResult()) {
                            List<String> associatedUsers = (List<String>) document
                                    .get("associatedUsers");
                            if (associatedUsers != null) {
                                userIds.addAll(associatedUsers);
                            }
                        }

                        // remove duplicates
                        userIds = new ArrayList<>(new HashSet<>(userIds));

                        if (!userIds.isEmpty()) {
                            firestore.collection("users")
                                    .whereIn("userId", userIds)
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        List<User> users = new ArrayList<>();
                                        for (DocumentSnapshot userDoc : querySnapshot) {
                                            User user = userDoc.toObject(User.class);
                                            users.add(user);
                                        }
                                        collaboratorsLiveData.setValue(users);
                                    })
                                    .addOnFailureListener(e -> collaboratorsLiveData
                                            .setValue(null));
                        } else {
                            collaboratorsLiveData.setValue(new ArrayList<>()); // no user found
                        }
                    } else {
                        collaboratorsLiveData.setValue(new ArrayList<>()); // no matching travel log
                    }
                });

        return collaboratorsLiveData;
    }



    public void addUserToTrip(String invitingUserId, String invitedUserId, String location) {
        // find the travel log by location
        firestore.collection("travelLogs")
                .whereEqualTo("destination", location)
                // so people added as a collaborator can also invite other people
                .whereArrayContains("associatedUsers", invitingUserId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // assuming only one trip with the given location, get the first result
                        String tripId = querySnapshot.getDocuments().get(0).getId();

                        Log.d("Firestore", "Found travel log for location: " + location
                                + ", Trip ID: " + tripId);

                        // add the userId to the associatedUsers array for travel logs
                        firestore.collection("travelLogs")
                                .document(tripId)
                                .update("associatedUsers", FieldValue.arrayUnion(invitedUserId))
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "User added to trip successfully!");

                                    // Update user's associatedDestinations array to include trip
                                    updateUserAssociatedDestinations(invitedUserId, tripId);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error adding user to trip", e);
                                });
                    } else {
                        Log.w("Firestore", "No travel log found for location: " + location
                                + " with inviting user " + invitingUserId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error finding travel log by location", e);
                });
    }

    // Adds startDate, endDate, and duration to their respective locations in a specific user's
    // database entry
    public void addDatesAndDuration(String userId, String startDate, String endDate,
                                    String duration) {
        // Create a map to hold the fields and their values
        Map<String, Object> updates = new HashMap<>();
        updates.put("startDate", startDate);
        updates.put("endDate", endDate);
        updates.put("duration", duration);

        // Perform the update with all fields at once
        firestore.collection("users").document(userId).update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated the document
                    Log.d("Firestore", "DocumentSnapshot successfully updated!");
                })
                .addOnFailureListener(e -> {
                    // Failed to update the document
                    Log.w("Firestore", "Error updating document", e);
                });
    }
    public void addDining(Dining dining, OnCompleteListener<DocumentReference> listener) {
        firestore.collection("dining")
                .add(dining)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String diningId = task.getResult().getId();

                        updateUserAssociatedDestinations(dining.getUserId(), diningId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }

    public LiveData<List<Dining>> getDiningLogsByUserAndLocation(String locationId) {
        MutableLiveData<List<Dining>> diningLogsLiveData = new MutableLiveData<>();

        firestore.collection("dining")
                .whereEqualTo("travelDestination", locationId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Dining> diningLogs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Dining dining = document.toObject(Dining.class);
                            diningLogs.add(dining);
                        }
                        diningLogsLiveData.setValue(diningLogs);
                    } else {
                        Log.e("Firestore", "Error getting dining logs: ", task.getException());
                        diningLogsLiveData.setValue(Collections.emptyList());
                    }
                });
        Log.d("Firestore", "Getting dining log for: " + locationId);
        return diningLogsLiveData;
    }


    public LiveData<List<Dining>> getDiningByUser(String userId) {
        MutableLiveData<List<Dining>> diningLiveData = new MutableLiveData<>();
        firestore.collection("dining")
                .whereEqualTo("userId", userId) // query logs for this user
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return; // to avoid null pointer
                    }
                    List<Dining> diningLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Dining log = document.toObject(Dining.class);
                        diningLogs.add(log);
                    }
                    diningLiveData.setValue(diningLogs);
                });
        return diningLiveData;
    }

    public void addAccommodation(Accommodation accommodation,
                                  OnCompleteListener<DocumentReference> listener) {
        firestore.collection("accommodation")
                .add(accommodation)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String accommodationId = task.getResult().getId();

                        updateUserAssociatedDestinations(accommodation.getUserId(),
                                accommodationId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }
    public LiveData<List<Accommodation>> getAccommodationLogsByUser(String destinationId) {
        MutableLiveData<List<Accommodation>> accommodationLiveData = new MutableLiveData<>();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("accommodation")
                .whereEqualTo("travelDestination", destinationId) // query logs for this user
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return; // to avoid null pointer
                    }
                    Log.d("Accomodatoin", "fetching accomodations for: " + destinationId);

                    List<Accommodation> accommodationLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Accommodation log = document.toObject(Accommodation.class);
                        accommodationLogs.add(log);
                    }

                    // Sort the logs by date (checkoutTime is in "yyyy-MM-dd" format)
                    Collections.sort(accommodationLogs, new Comparator<Accommodation>() {
                        @Override
                        public int compare(Accommodation o1, Accommodation o2) {
                            String date1 = o1.getCheckOutTime();
                            String date2 = o2.getCheckOutTime();

                            // Compare dates as strings (lexicographical order)
                            return date2.compareTo(date1);
                        }
                    });

                    accommodationLiveData.setValue(accommodationLogs);
                });

        return accommodationLiveData;
    }

    public void addNoteToTravelLog(String location, String currentUserId, String documentId,
                                   Note note,
                                   OnCompleteListener<Void> listener) {
        firestore.collection("travelLogs")
                .whereEqualTo("destination", location)
                .whereEqualTo("documentId", documentId)
                .whereArrayContains("associatedUsers", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String travelLogId = document.getId();

                        // reference to the travel log document
                        DocumentReference travelLogRef = firestore
                                .collection("travelLogs").document(travelLogId);

                        // add the new note
                        travelLogRef.update("notes", FieldValue.arrayUnion(note))
                                .addOnCompleteListener(listener);
                    } else {
                        // no travel log match
                        listener.onComplete(Tasks.forResult(null));
                    }
                });
    }

    public LiveData<List<Note>> getNotesForTravelLog(String location, String currentUserId,
                                                     String documentId) {
        MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();

        firestore.collection("travelLogs")
                .whereEqualTo("destination", location)
                .whereEqualTo("documentId", documentId)
                .whereArrayContains("associatedUsers", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // fetch the notes field
                        List<Map<String, Object>> notesData = (List<Map<String, Object>>) document
                                .get("notes");

                        if (notesData != null && !notesData.isEmpty()) {
                            // get unique user IDs from the notes
                            Set<String> userIds = new HashSet<>();
                            for (Map<String, Object> noteData : notesData) {
                                String userId = (String) noteData.get("userId");
                                if (userId != null) {
                                    userIds.add(userId);
                                }
                            }

                            // query users collection to get emails for each user ID
                            firestore.collection("users")
                                    .whereIn("userId", new ArrayList<>(userIds))
                                    .get()
                                    .addOnSuccessListener(querySnapshot -> {
                                        Map<String, String> userIdToEmailMap = new HashMap<>();
                                        for (DocumentSnapshot userDoc : querySnapshot) {
                                            String userId = userDoc.getString("userId");
                                            String email = userDoc.getString("email");
                                            if (userId != null && email != null) {
                                                userIdToEmailMap.put(userId, email);
                                            }
                                        }

                                        List<Note> notes = new ArrayList<>();
                                        for (Map<String, Object> noteData : notesData) {
                                            String noteContent = (String) noteData
                                                    .get("noteContent");
                                            String userId = (String) noteData.get("userId");
                                            String email = userIdToEmailMap.get(userId);

                                            // add email to the Note object
                                            if (email != null) {
                                                Note note = new Note(noteContent, email);
                                                notes.add(note);
                                            }
                                        }

                                        // TODO!: this isn't working very well, try to fix
                                        // Sort notes by timestamp (descending order)
                                        Collections.sort(notes, (note1, note2) -> Long
                                                .compare(note2.getTimestampMillis(),
                                                        note1.getTimestampMillis()));

                                        notesLiveData.setValue(notes);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d("Firestore", "Failed to fetch user emails", e);
                                        notesLiveData.setValue(new ArrayList<>());
                                    });
                        } else {
                            Log.d("Firestore", "No notes found in the document");
                            notesLiveData.setValue(new ArrayList<>());
                        }
                    } else {
                        Log.d("Firestore", "Query failed or no matching travel log found "
                            + "for: " + location);
                        notesLiveData.setValue(new ArrayList<>());
                    }
                });

        return notesLiveData;
    }

    public void addTravelPost(Post travelPost,
                              OnCompleteListener<DocumentReference> listener) {
        firestore.collection("travel_community")
                .add(travelPost)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String travelPostId = task.getResult().getId();

                        updateUserAssociatedDestinations(travelPost.getPostUsername(),
                                travelPostId);
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }

    public LiveData<List<Post>> getTravelPostByUser(String userId) {
        //travelCommunityLiveData
        firestore.collection("travel_community")
                .whereEqualTo("userId", userId) // query logs for this user
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return; // to avoid null pointer
                    }
                    List<Post> postLogs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Post log = document.toObject(Post.class);
                        postLogs.add(log);
                    }
                    travelCommunityLiveData.setValue(postLogs);
                });
        return travelCommunityLiveData;
    }
}