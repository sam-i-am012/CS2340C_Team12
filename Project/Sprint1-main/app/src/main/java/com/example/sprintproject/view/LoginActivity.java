package com.example.sprintproject.view;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        editTextEmail = findViewById(R.id.email_login);
        editTextPassword = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progressBar);
        Button loginBtn = findViewById(R.id.loginButton);
        ImageButton quitButton = findViewById(R.id.exitButton);
        TextView createAccount = findViewById(R.id.accountCreationPage);

        // Initialize ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observe ViewModel changes
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            progressBar.setVisibility(View.GONE);
            if (loginResult.isSuccess()) {
                // Navigate to next screen
                startActivity(new Intent(getApplicationContext(), LogisticsActivity.class));
                finish();
            } else {
                // Show error message
                Toast.makeText(LoginActivity.this, "Login failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Login button click
        loginBtn.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            loginViewModel.login(email, password);
        });

        // Quit button click
        quitButton.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });

        // Navigate to account creation
        createAccount.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            finish();
        });
    }
}


