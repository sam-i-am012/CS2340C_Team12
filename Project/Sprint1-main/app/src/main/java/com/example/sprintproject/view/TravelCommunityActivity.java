package com.example.sprintproject.view;

<<<<<<< HEAD
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

public class TravelCommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_community);

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);

        diningEstablishmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diningEstablishmentsIntent = new Intent(TravelCommunityActivity.this,
                        DiningEstablishmentsActivity.class);
                startActivity(diningEstablishmentsIntent);
            }
        });

        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent destinationsIntent = new Intent(TravelCommunityActivity.this,
                        DestinationsActivity.class);
                startActivity(destinationsIntent);
            }
        });

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logisticsIntent = new Intent(TravelCommunityActivity.this,
                        LogisticsActivity.class);
                startActivity(logisticsIntent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(TravelCommunityActivity.this,
                        AccommodationsActivity.class);
                startActivity(accommodationsIntent);
            }
        });
    }
}
=======
import androidx.appcompat.app.AppCompatActivity;

public class TravelCommunityActivity extends AppCompatActivity {
}
>>>>>>> 0f68686e63d3d90bcb9a6532820ea8c3924fc0ec
