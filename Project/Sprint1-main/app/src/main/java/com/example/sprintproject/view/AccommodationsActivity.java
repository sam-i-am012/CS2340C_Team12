package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Location;
import com.example.sprintproject.viewmodel.AccommodationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {

    private AccommodationViewModel accommodationViewModel;
    private AccommodationsAdapter accommodationsAdapter;
    private RecyclerView recyclerView;

    private EditText locationET;
    private EditText checkInTimeET;
    private EditText checkOutTimeET;
    private EditText hotelET;
    private Spinner rooms;
    private Spinner roomType;
    private ImageButton diningEstablishmentsButton;
    private ImageButton destinationsButton;
    private ImageButton logisticsButton;
    private ImageButton travelCommunityButton;
    private FloatingActionButton addAccommodationButton;
    private Spinner locationSpinner;
    private String selectedDestinationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations); // The main layout

        initViews();

        // ViewModel setup
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        addAccommodationButton.setOnClickListener(view -> {
            AddAccommodationsDialog addAccommodationsDialog = new AddAccommodationsDialog(
                    AccommodationsActivity.this, accommodationViewModel, selectedDestinationId);
            addAccommodationsDialog.show();
        });

        // location spinner setup
        locationSpinner = findViewById(R.id.locationSpinnerAccomodation);
        locationSpinner.setVisibility(View.VISIBLE);
        populateLocationSpinner(locationSpinner); // prepopulate the spinner with locations

        // RecyclerView setup
        recyclerView = findViewById(R.id.accommodationRecyclerView);
        accommodationsAdapter = new AccommodationsAdapter();
        recyclerView.setAdapter(accommodationsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe the LiveData for updates to logs
        accommodationViewModel.getAccommodationLogs().observe(this, accommodations -> {
            // update adapter when data changes
            accommodationsAdapter.setAccommodations(accommodations);
        });

        // Navigation button logic
        navButtonsLogic();
    }

    private void initViews() {
        diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        destinationsButton = findViewById(R.id.destinationsButton);
        logisticsButton = findViewById(R.id.logisticsButton);
        travelCommunityButton = findViewById(R.id.travelCommunityButton);
        addAccommodationButton = findViewById(R.id.addAccommodationButton);
        locationET = findViewById(R.id.editTextAccommodationLocation);
        checkInTimeET = findViewById(R.id.editTextAccommodationCheckInTime);
        checkOutTimeET = findViewById(R.id.editTextAccommodationCheckOutTime);
        hotelET = findViewById(R.id.editTextHotelName);
        rooms = findViewById(R.id.numberOfRoomsSpinner);
        roomType = findViewById(R.id.roomTypeSpinner);
    }

    private void navButtonsLogic() {
        // Handle navigation bar button presses
        diningEstablishmentsButton.setOnClickListener(view -> {
            Intent diningEstablishmentsIntent = new Intent(AccommodationsActivity.this,
                    DiningEstablishmentsActivity.class);
            startActivity(diningEstablishmentsIntent);
        });

        destinationsButton.setOnClickListener(view -> {
            Intent destinationsIntent = new Intent(AccommodationsActivity.this,
                    DestinationsActivity.class);
            startActivity(destinationsIntent);
        });

        logisticsButton.setOnClickListener(view -> {
            Intent logisticsIntent = new Intent(AccommodationsActivity.this,
                    LogisticsActivity.class);
            startActivity(logisticsIntent);
        });

        travelCommunityButton.setOnClickListener(view -> {
            Intent travelCommunityIntent = new Intent(AccommodationsActivity.this,
                    TravelCommunityActivity.class);
            startActivity(travelCommunityIntent);
        });
    }

    private void clearInputFields() {
        locationET.setText("");
        checkInTimeET.setText("");
        checkOutTimeET.setText("");
        hotelET.setText("");
    }


    private void populateLocationSpinner(Spinner locationSpinner) {
        accommodationViewModel.getUserLocations().observe(this, locations -> {
            ArrayAdapter<Location> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, locations);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);

            locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                           int position, long id) {
                    Location selected = (Location) parentView.getItemAtPosition(position);
                    String selectedLocation = selected.getLocationName();
                    selectedDestinationId = selected.getDocumentId();
                    Log.e("dining", "Selected dining ID: " + selectedDestinationId);

                    if (selectedDestinationId != null) {
                        accommodationViewModel.fetchAccommodationLogsForDestination(selectedDestinationId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        });
    }
}