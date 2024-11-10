package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.model.TravelLogValidator;
import com.example.sprintproject.viewmodel.AccommodationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AccommodationsActivity extends AppCompatActivity {

    private EditText locationET;
    private EditText checkInTimeET;
    private EditText checkOutTimeET;
    private ImageButton diningEstablishmentsButton;
    private ImageButton destinationsButton;
    private ImageButton logisticsButton;
    private ImageButton travelCommunityButton;
    private FloatingActionButton addAccommodationButton;
    private Button submitButton;
    private AccommodationViewModel viewModel;
    private AccommodationsAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations);

        initViews();

        recyclerView = findViewById(R.id.accommodationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        // after auth
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            viewModel.fetchAccommodationLogsForCurrentUser();
        }

        viewModelObserver();

        addAccommodationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AddAccommodationDialog to add new accommodation
                AddAccommodationsDialog dialog = new AddAccommodationsDialog();
                dialog.show(getSupportFragmentManager(), "AddAccommodationDialog");
            }
        });

        submitButton.setOnClickListener(v -> {
            // Create a new Accomodation object and validate it before adding
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                return;
            }

            String userId = user.getUid();
            String location = locationET.getText().toString().trim();
            String checkInTime = locationET.getText().toString().trim();
            String checkOutTime = locationET.getText().toString().trim();

            if (location.isEmpty() || checkInTime.isEmpty() || checkOutTime.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Accommodation newLog = new Accommodation(null, location, checkInTime, checkOutTime, 0, null, userId);

            // Add the new log directly to the adapter and update total days
            adapter.addLog(newLog);

            // Clear the input fields
            clearInputFields();

            // Add the log to the ViewModel/database asynchronously
            viewModel.addAccommodation(newLog);
        });

        navButtonsLogic();
    }

    private void initViews() {
        diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        destinationsButton = findViewById(R.id.destinationsButton);
        logisticsButton = findViewById(R.id.logisticsButton);
        travelCommunityButton = findViewById(R.id.travelCommunityButton);
        addAccommodationButton = findViewById(R.id.addAccommodationButton);
        //submitButton = 
        locationET = findViewById(R.id.editTextAccommodationLocation);
        checkOutTimeET = findViewById(R.id.editTextAccommodationCheckInTime);
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
}