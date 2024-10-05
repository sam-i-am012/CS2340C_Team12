package com.example.sprintproject.view;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;


import com.example.sprintproject.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton quitButton = findViewById(R.id.exitButton);

        // quit application
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish activity
                LoginActivity.this.finish();

                // exit activity
                System.exit(0);
            }
        });
    }
}


