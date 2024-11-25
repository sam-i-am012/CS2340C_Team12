package com.example.sprintproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Post;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.view.AccommodationsAdapter;
import com.example.sprintproject.view.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class CommunityViewModel extends AndroidViewModel {

    private PostAdapter postAdapter = new PostAdapter();
    private FirestoreSingleton repository;
    private LiveData<List<Post>> posts;
    private MutableLiveData<Result> resValidationResult = new MutableLiveData<>();

    public CommunityViewModel(@NonNull Application application) {
        super(application);
        repository = FirestoreSingleton.getInstance();

        // Fetch posts from firestore
        posts = repository.getTravelPosts();
    }

    // Returns the LiveData object holding the list of travel posts
    public LiveData<List<Post>> getTravelPostsLiveData() {
        return posts;
    }

    // Adds a new travel post to the repository
    public void addTravelPost(Post travelPost, OnCompleteListener<DocumentReference> listener) {
        repository.addTravelPost(travelPost, task -> {
            if (listener != null) {
                listener.onComplete(task);
            }
        });
    }

    public PostAdapter getPostAdapter() {
        return postAdapter;
    }

    public MutableLiveData<Result> getResValidationResult() {
        return resValidationResult;
    }
}
