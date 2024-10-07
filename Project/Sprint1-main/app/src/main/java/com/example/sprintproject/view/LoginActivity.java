package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.LoginViewModel;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
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
            if (loginResult.isSuccess()) {
                // Navigate to the next screen
                Toast.makeText(LoginActivity.this,
                        "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LogisticsActivity.class));
                finish();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),
                        "Login failed", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });

        // Login button click
        loginBtn.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Validation logic
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter an email address", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter a password", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(email, password);
            }
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
