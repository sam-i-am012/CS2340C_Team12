package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;

import java.util.Arrays;

public class DestinationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        Button calcVacationTimeButton = findViewById(R.id.calcVacationTimeButton);
        EditText startDateET = findViewById(R.id.startDateET);
        EditText endDateET = findViewById(R.id.endDateET);
        EditText durationET = findViewById(R.id.durationET);
        Button calculateButton = findViewById(R.id.calculateButton);

        /*
        Code for handling when the Calculate Vacation Time button is pressed
         */
        calcVacationTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDateET.getVisibility() == View.GONE) {
                    // Make dialog elements visible
                    for (EditText editText : Arrays.asList(startDateET, endDateET, durationET)) {
                        editText.setVisibility(View.VISIBLE);
                    }
                    calculateButton.setVisibility(View.VISIBLE);
                } else {
                    // Hide dialog elements
                    for (EditText editText : Arrays.asList(startDateET, endDateET, durationET)) {
                        editText.setVisibility(View.GONE);
                    }
                    calculateButton.setVisibility(View.GONE);
                }
            }
        });

        /*
        Code for handling when buttons are pressed in the navigation bar
         */
        diningEstablishmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diningEstablishmentsIntent = new Intent(DestinationsActivity.this,
                        DiningEstablishmentsActivity.class);
                startActivity(diningEstablishmentsIntent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(DestinationsActivity.this,
                        AccommodationsActivity.class);
                startActivity(accommodationsIntent);
            }
        });

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logisticsIntent = new Intent(DestinationsActivity.this,
                        LogisticsActivity.class);
                startActivity(logisticsIntent);
            }
        });

        travelCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent travelCommunityIntent = new Intent(DestinationsActivity.this,
                        TravelCommunityActivity.class);
                startActivity(travelCommunityIntent);
            }
        });
    }
}