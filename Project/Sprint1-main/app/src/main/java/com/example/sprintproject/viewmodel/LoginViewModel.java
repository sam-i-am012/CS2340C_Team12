package com.example.sprintproject.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirebaseAuthManager;
import com.example.sprintproject.model.Result;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Result> loginResult = new MutableLiveData<>();
    private final FirebaseAuthManager loginRepository = new FirebaseAuthManager();

    public LiveData<Result> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        // Call the repository and handle the task result
        loginRepository.login(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginResult.setValue(new Result(true, null));
                    } else {
                        loginResult.setValue(new Result(false, task.getException().getMessage()));
                    }
                });
    }
}
