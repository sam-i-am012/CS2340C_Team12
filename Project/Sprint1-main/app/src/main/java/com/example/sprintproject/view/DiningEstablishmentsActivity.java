package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.viewmodel.DiningViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DiningEstablishmentsActivity extends AppCompatActivity {
    private DiningViewModel diningViewModel;
    private DiningsAdapter diningAdapter;
    private Spinner locationSpinner;
    private String selectedDestinationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dining_establishments);

        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        FloatingActionButton reservationDialogButton = findViewById(R.id.fabAddReservation);
        locationSpinner = findViewById(R.id.locationSpinner);

        locationSpinner.setVisibility(View.VISIBLE);

        // populate the spinner with locations after it's made visible
        populateLocationSpinner(locationSpinner);


        // Add reservation button and dialog logic
        reservationDialogButton.setOnClickListener(view -> {
            String selectedLocation = locationSpinner.getSelectedItem().toString();

            AddReservationDialog addReservationDialog = new AddReservationDialog(
                    DiningEstablishmentsActivity.this, diningViewModel, selectedDestinationId);
            addReservationDialog.show();
        });

        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.rvReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diningAdapter = new DiningsAdapter();
        recyclerView.setAdapter(diningAdapter);

        // Fetch dining logs for the current user
        diningViewModel.fetchDiningLogsForCurrentUser();


        // Observe the LiveData for updates to dining logs
        diningViewModel.getDiningLogs().observe(this, new Observer<List<Dining>>() {
            @Override
            public void onChanged(List<Dining> dinings) {
                diningAdapter.setDinings(dinings); // Update the adapter when data changes
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

    }

    private void populateLocationSpinner(Spinner locationSpinner) {
        diningViewModel.getUserLocationsWithIds().observe(DiningEstablishmentsActivity.this, locationsWithIds -> {
            // get location names from the map
            List<String> locationNames = new ArrayList<>(locationsWithIds.keySet());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, locationNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);

            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedLocation = locationNames.get(position);
                    selectedDestinationId = locationsWithIds.get(selectedLocation);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        });
    }

}