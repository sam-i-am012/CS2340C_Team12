package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;

public class LogisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);

        diningEstablishmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diningEstablishmentsIntent = new Intent(AccommodationsActivity.this,
                        DiningEstablishmentsActivity.class);
                startActivity(diningEstablishmentsIntent);
            }
        });

        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent destinationsIntent = new Intent(AccommodationsActivity.this,
                        DestinationsActivity.class);
                startActivity(destinationsIntent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(LogisticsActivity.this,
                        AccommodationsActivity.class);
                startActivity(accommodationsIntent);
            }
        });

        travelCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent travelCommunityIntent = new Intent(AccommodationsActivity.this,
                        TravelCommunityActivity.class);
                startActivity(travelCommunityIntent);
            }
        });
    }
}