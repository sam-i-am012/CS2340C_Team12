package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.google.android.material.textfield.TextInputEditText;

public class CreateAccountActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // setting variables
        editTextEmail = findViewById(R.id.email_createAccount); // email from text
        editTextPassword = findViewById(R.id.password_createAccount); // password from text
        TextView returnToLogin = findViewById(R.id.returnToLogin); // return to login clickable text
        createAccountBtn = findViewById(R.id.createAccountButton); // create account button

        // create account button
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

//                if (TextUtils.isEmpty())

                Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();


            }
        });
        // return to login page button
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}