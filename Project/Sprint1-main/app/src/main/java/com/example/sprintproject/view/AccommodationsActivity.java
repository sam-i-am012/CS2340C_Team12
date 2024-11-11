package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.viewmodel.AccommodationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AccommodationsActivity extends AppCompatActivity implements AddAccommodationsDialog.OnAccommodationAddedListener {

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
    private AccommodationViewModel viewModel;
    private AccommodationsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations); // The main layout

        initViews();

        recyclerView = findViewById(R.id.accommodationRecyclerView);
        adapter = new AccommodationsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        // After authentication
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            viewModel.fetchAccommodationLogsForCurrentUser();
        }

        viewModelObserver();

        addAccommodationButton.setOnClickListener(v -> {
            // Inflate the dialog layout (item_add_accommodation.xml)
            LayoutInflater inflater = LayoutInflater.from(AccommodationsActivity.this);
            View dialogView = inflater.inflate(R.layout.dialog_add_accommodation, null);

            // Find the button inside the dialog view
            Button btnAddAccommodationDialog = dialogView.findViewById(R.id.btnAddAccommodationDialog);

            // Set the click listener for the button inside the dialog
            btnAddAccommodationDialog.setOnClickListener(v1 -> {
                // Create a new Accommodation object and validate it before adding
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }

                String userId = user.getUid();
                String location = locationET.getText().toString().trim();
                String checkInTime = checkInTimeET.getText().toString().trim();
                String checkOutTime = checkOutTimeET.getText().toString().trim();
                String hotel = hotelET.getText().toString().trim();
                String room = rooms.getSelectedItem().toString().trim();
                int numRooms = Integer.parseInt(room);
                String roomTypes = roomType.getSelectedItem().toString().trim();

                if (location.isEmpty() || checkInTime.isEmpty() || checkOutTime.isEmpty()  || hotel.isEmpty() || room.isEmpty() || roomTypes.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Accommodation newLog = new Accommodation(hotel, location, checkInTime, checkOutTime, numRooms, roomTypes, userId);

                // Add the accommodation to the ViewModel (not the adapter directly)
                viewModel.addAccommodation(newLog);

                // Clear the input fields
                clearInputFields();
            });

            // Create and show the dialog
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AccommodationsActivity.this);
            builder.setView(dialogView);
            builder.create().show();
        });

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
    }

    private void viewModelObserver() {
        viewModel.getAccommodations().observe(this, accommodations -> {
            if (adapter == null) {
                adapter = new AccommodationsAdapter(accommodations);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateLogs(accommodations); // Add a method to update the adapter data
            }
        });
    }

    @Override
    public void onAccommodationAdded(Accommodation accommodation) {
        viewModel.addAccommodation(accommodation);
    }
}