package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Post;
import com.example.sprintproject.model.FirestoreSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class CommunityViewModel extends ViewModel {

    private final FirestoreSingleton repository;
    private final LiveData<List<Post>> travelPostsLiveData;

    public CommunityViewModel() {
        repository = FirestoreSingleton.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Fetch posts by the current user
        if (currentUser != null) {
            String userId = currentUser.getUid();
            travelPostsLiveData = repository.getTravelPostByUser(userId);
        } else {
            travelPostsLiveData = null; // No data if no user is logged in
        }
    }

    // Returns the LiveData object holding the list of travel posts
    public LiveData<List<Post>> getTravelPostsLiveData() {
        return travelPostsLiveData;
    }

    // Adds a new travel post to the repository
    public void addTravelPost(Post travelPost, OnCompleteListener<DocumentReference> listener) {
        repository.addTravelPost(travelPost, task -> {
            if (listener != null) {
                listener.onComplete(task);
            }
        });
    }
}
