package com.example.sprintproject.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.DiningViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiningEstablishmentsActivity extends AppCompatActivity {
    private DiningViewModel diningViewModel;
    //private ReservationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_establishments);

        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        FloatingActionButton reservationDialogButton = findViewById(R.id.fabAddReservation);

        Dialog dialog = new Dialog(this);
        EditText timeET = dialog.findViewById(R.id.etTime);
        EditText locationET = dialog.findViewById(R.id.etLocation);
        EditText websiteET = dialog.findViewById(R.id.etWebsite);
        Button addReservationButton = dialog.findViewById(R.id.btnAddReservationDialog);

        // Add reservation button and dialog logic
        reservationDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_bg);
                dialog.setContentView(R.layout.dialog_add_reservation);
                dialog.show();
            }
        });



        // Navigation bar logic
        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent destinationsIntent = new Intent(DiningEstablishmentsActivity.this,
                        DestinationsActivity.class);
                startActivity(destinationsIntent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(DiningEstablishmentsActivity.this,
                        AccommodationsActivity.class);
                startActivity(accommodationsIntent);
            }
        });

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logisticsIntent = new Intent(DiningEstablishmentsActivity.this,
                        LogisticsActivity.class);
                startActivity(logisticsIntent);
            }
        });

        travelCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent travelCommunityIntent = new Intent(DiningEstablishmentsActivity.this,
                        TravelCommunityActivity.class);
                startActivity(travelCommunityIntent);
            }
        });

        // Sprint 3


    }
}