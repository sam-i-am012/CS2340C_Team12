package com.example.sprintproject.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.LoginCallback;
import com.example.sprintproject.model.LoginRepository;
import com.example.sprintproject.model.LoginResult;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository = new LoginRepository();

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        // Add validation for email and password
        if (TextUtils.isEmpty(email)) {
            loginResult.setValue(new LoginResult(false, "Please enter an email address"));
            return;
        } else if (TextUtils.isEmpty(password)) {
            loginResult.setValue(new LoginResult(false, "Please enter a password"));
            return;
        }

        loginRepository.login(email, password, new LoginCallback() {
            @Override
            public void onSuccess() {
                loginResult.setValue(new LoginResult(true, null));
            }

            @Override
            public void onFailure(String errorMessage) {
                loginResult.setValue(new LoginResult(false, errorMessage));
            }
        });
    }
}
