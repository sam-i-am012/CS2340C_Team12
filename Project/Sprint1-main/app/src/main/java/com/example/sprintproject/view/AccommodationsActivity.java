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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.viewmodel.AccommodationViewModel;
import com.example.sprintproject.viewmodel.DiningViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodations); // The main layout

        initViews();

        // ViewModel setup
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        addAccommodationButton.setOnClickListener(view -> {
            AddAccommodationsDialog addAccommodationsDialog = new AddAccommodationsDialog(
                    AccommodationsActivity.this, accommodationViewModel);
            addAccommodationsDialog.show();
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.accommodationRecyclerView);
        accommodationsAdapter = new AccommodationsAdapter();
        recyclerView.setAdapter(accommodationsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch dining logs for the current user
        accommodationViewModel.fetchAccommodationLogsForCurrentUser(); // Ensure data is fetched

        // Observe the LiveData for updates to dining logs
        accommodationViewModel.getAccommodationLogs().observe(this, new Observer<List<Accommodation>>() {
            @Override
            public void onChanged(List<Accommodation> accommodations) {
                accommodationsAdapter.setAccommodations(accommodations); // Update the adapter when data changes
            }
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
}
