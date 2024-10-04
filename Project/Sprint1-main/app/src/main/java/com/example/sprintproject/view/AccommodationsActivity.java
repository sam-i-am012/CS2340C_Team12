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

public class AccommodationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_accommodations);
=======
        setContentView(R.layout.activity_destinations);
>>>>>>> bec322b (creating Accommodations_Activity class and adding the navigation bar to it)

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);

        diningEstablishmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diningEstablishmentsIntent = new Intent(AccommodationsActivity.this,
                        DiningEstablishmentsActivity.class);
<<<<<<< HEAD
                startActivity(diningEstablishmentsIntent);
=======
>>>>>>> bec322b (creating Accommodations_Activity class and adding the navigation bar to it)
            }
        });

        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                Intent destinationsIntent = new Intent(AccommodationsActivity.this,
                        DestinationsActivity.class);
                startActivity(destinationsIntent);
=======
                Intent accommodationsIntent = new Intent(AccommodationsActivity.this,
                        AccommodationsActivity.class);
>>>>>>> bec322b (creating Accommodations_Activity class and adding the navigation bar to it)
            }
        });

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logisticsIntent = new Intent(AccommodationsActivity.this,
                        LogisticsActivity.class);
<<<<<<< HEAD
                startActivity(logisticsIntent);
=======
>>>>>>> bec322b (creating Accommodations_Activity class and adding the navigation bar to it)
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