package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button createAccountBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    private static final String TAG = "CreateAccountActivity"; // defining tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();

        // finding IDs
        editTextEmail = findViewById(R.id.email_createAccount); // email from text
        editTextPassword = findViewById(R.id.password_createAccount); // password from text
        TextView returnToLogin = findViewById(R.id.returnToLogin); // return to login clickable text
        createAccountBtn = findViewById(R.id.createAccountButton); // create account button
        progressBar = findViewById(R.id.progressBar);

        // create account button
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) { // nested to make sure both messages don't pop up at the same time
                    Toast.makeText(getApplicationContext(), "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }

                // create user with email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE); // remove progress bar

                                if (task.isSuccessful()) {
                                    // successful sign in
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(CreateAccountActivity.this, "Account created",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        // Email already in use
                                        Log.w(TAG, "createUserWithEmail:emailAlreadyInUse");
                                        Toast.makeText(CreateAccountActivity.this, "This email is already registered.",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        // Generic failure
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(CreateAccountActivity.this, "Failed to create an account",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        // return to login page button
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}