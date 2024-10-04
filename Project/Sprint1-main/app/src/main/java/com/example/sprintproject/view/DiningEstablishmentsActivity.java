package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.sprintproject.R;

public class DiningEstablishmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_establishments);

        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);

        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent destinationsIntent = new Intent(DiningEstablishmentsActivity.this,
                        DestinationsActivity.class);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(DiningEstablishmentsActivity.this,
                        AccommodationsActivity.class);
            }
        });

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logisticsIntent = new Intent(DiningEstablishmentsActivity.this,
                        LogisticsActivity.class);
            }
        });

        travelCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent travelCommunityIntent = new Intent(DiningEstablishmentsActivity.this,
                        TravelCommunityActivity.class);
            }
        });
    }
}