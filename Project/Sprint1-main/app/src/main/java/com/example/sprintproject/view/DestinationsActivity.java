package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.sprintproject.R;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.TravelLog;
import com.example.sprintproject.viewmodel.DestinationsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.sprintproject.model.Result;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

public class DestinationsActivity extends AppCompatActivity {

    private ImageButton diningEstablishmentsButton;
    private ImageButton accommodationsButton;
    private ImageButton logisticsButton;
    private ImageButton travelCommunityButton;
    private Button calcVacationTimeButton;
    private EditText startDateET;
    private EditText endDateET;
    private EditText durationET;
    private Button calculateButton;
    private Button logTravelButton;
    private TextView travelLocationTV;
    private EditText travelLocationET;
    private TextView estimatedStartTV;
    private EditText estimatedStartET;
    private TextView estimatedEndTV;
    private EditText estimatedEndET;
    private Button cancelButton;
    private Button submitButton;
    private Button resetButton;

    private RecyclerView recyclerView;
    private TravelLogAdapter adapter;
    private DestinationsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        // Initialize views after setContentView
        diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        accommodationsButton = findViewById(R.id.accommodationsButton);
        logisticsButton = findViewById(R.id.logisticsButton);
        travelCommunityButton = findViewById(R.id.travelCommunityButton);
        calcVacationTimeButton = findViewById(R.id.calcVacationTimeButton);
        startDateET = findViewById(R.id.startDateET);
        endDateET = findViewById(R.id.endDateET);
        durationET = findViewById(R.id.durationET);
        calculateButton = findViewById(R.id.calculateButton);
        logTravelButton = findViewById(R.id.logTravelButton);
        travelLocationTV = findViewById(R.id.travelLocationsTV);
        travelLocationET = findViewById(R.id.travelLocationsET);
        estimatedStartTV = findViewById(R.id.estimatedStartTV);
        estimatedStartET = findViewById(R.id.estimatedStartET);
        estimatedEndTV = findViewById(R.id.estimatedEndTV);
        estimatedEndET = findViewById(R.id.estimatedEndET);
        cancelButton = findViewById(R.id.cancelButton);
        submitButton = findViewById(R.id.submitButton);
        resetButton = findViewById(R.id.resetButton);

        // Call method to populate the database and fetch logs
        FirestoreSingleton firestore = FirestoreSingleton.getInstance();
        firestore.prepopulateDatabase();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);

        // After authentication
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            viewModel.fetchTravelLogsForCurrentUser();
        }

        viewModel.getTravelLogs().observe(this, travelLogs -> {
            adapter = new TravelLogAdapter(travelLogs);
            recyclerView.setAdapter(adapter);
        });

        logTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (travelLocationTV.getVisibility() == View.GONE) {
                    // Make dialog elements visible
                    for (TextView textView : Arrays.asList(travelLocationTV, estimatedStartTV, estimatedEndTV)) {
                        textView.setVisibility(View.VISIBLE);
                    }
                    for (EditText editText : Arrays.asList(travelLocationET, estimatedStartET, estimatedEndET)) {
                        editText.setVisibility(View.VISIBLE);
                    }
                    cancelButton.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                } else {
                    // Hide dialog elements
                    for (TextView textView : Arrays.asList(travelLocationTV, estimatedStartTV, estimatedEndTV)) {
                        textView.setVisibility(View.GONE);
                    }
                    for (EditText editText : Arrays.asList(travelLocationET, estimatedStartET, estimatedEndET)) {
                        editText.setVisibility(View.GONE);
                    }
                    cancelButton.setVisibility(View.GONE);
                    submitButton.setVisibility(View.GONE);
                }
            }
        });

        // Set up the button click listener
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide dialog elements
                for (TextView textView : Arrays.asList(travelLocationTV, estimatedStartTV, estimatedEndTV)) {
                    textView.setVisibility(View.GONE);
                }
                for (EditText editText : Arrays.asList(travelLocationET, estimatedStartET, estimatedEndET)) {
                    editText.setVisibility(View.GONE);
                }
                cancelButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.GONE);
            }
        });

        // Set up the button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTravelLog();
            }
        });

        // Handle Calculate Vacation Time button press
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
         * Code for handling when Calculate button is pressed
         */

        // Set Up Calculate Button Press
        // Get reference to the result layout
        View resultLayout = findViewById(R.id.resultLayout);

        // Initially set result layout to not visible
        resultLayout.setVisibility(View.GONE);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = startDateET.getText().toString();
                String endDate = endDateET.getText().toString();
                String duration = durationET.getText().toString();
                String entry;

                // Set result layout to visible
                resultLayout.setVisibility(View.VISIBLE);
                // Set the result text
                int totalDays = adapter.getTotalDays();
                TextView resultText = findViewById(R.id.resultText);
                resultText.setText(totalDays + "\n" + "days");

                Result missingEntry = viewModel.validateMissingEntry(startDate, endDate, duration);
                if (missingEntry.isSuccess()) {
                    switch (missingEntry.getMessage()) {
                        case "None":
                            Toast.makeText(DestinationsActivity.this,
                                    "All entries already populated", Toast.LENGTH_SHORT).show();
                            break;
                        case "Start Date":
                            entry = viewModel.calculateMissingEntry(duration, endDate);
                            startDateET.setText(entry);
                            break;
                        case "End Date":
                            entry = viewModel.calculateMissingEntry(startDate, duration);
                            endDateET.setText(entry);
                            break;
                        case "Duration":
                            Result dateRangeValid = viewModel.validateDateRange(startDate, endDate);
                            if (dateRangeValid.isSuccess()) {
                                entry = viewModel.calculateMissingEntry(startDate, endDate);
                                durationET.setText(entry);
                                break;
                            } else {
                                Toast.makeText(DestinationsActivity.this,
                                        dateRangeValid.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                }
            }
        });
      
        // Set up the Reset button to hide the result layout when clicked
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLayout.setVisibility(View.GONE);
                TextView resultText = findViewById(R.id.resultText);
                resultText.setText("XX Days");

                startDateET.setText("");
                endDateET.setText("");
                durationET.setText("");
            }
        });

        // Handle navigation bar button presses
        diningEstablishmentsButton.setOnClickListener(view -> {
            Intent diningEstablishmentsIntent = new Intent(DestinationsActivity.this,
                    DiningEstablishmentsActivity.class);
            startActivity(diningEstablishmentsIntent);
        });

        accommodationsButton.setOnClickListener(view -> {
            Intent accommodationsIntent = new Intent(DestinationsActivity.this,
                    AccommodationsActivity.class);
            startActivity(accommodationsIntent);
        });

        logisticsButton.setOnClickListener(view -> {
            Intent logisticsIntent = new Intent(DestinationsActivity.this,
                    LogisticsActivity.class);
            startActivity(logisticsIntent);
        });

        travelCommunityButton.setOnClickListener(view -> {
            Intent travelCommunityIntent = new Intent(DestinationsActivity.this,
                    TravelCommunityActivity.class);
            startActivity(travelCommunityIntent);
        });
    }

    private void addTravelLog() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String userId = user.getUid();
        String destination = travelLocationET.getText().toString().trim();
        String startDate = estimatedStartET.getText().toString().trim();
        String endDate = estimatedEndET.getText().toString().trim();

        if (TextUtils.isEmpty(destination) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            Toast.makeText(getApplicationContext(),
                    "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        } else if (isDateFormatInvalid(startDate) ||
                isDateFormatInvalid(endDate) ||
                calculateDays(startDate, endDate) < 0) {
            Toast.makeText(getApplicationContext(),
                    "Please enter valid dates (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        TravelLog newLog = new TravelLog(userId, destination, startDate, endDate, new ArrayList<>());
        viewModel.addTravelLog(newLog); // Update the ViewModel to add new log
        clearInputFields();
    }

    private boolean isDateFormatInvalid(String date) {
        // Regular expression to match the format YYYY-MM-DD
        String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";

        // Check if the date matches the pattern
        if (!date.matches(datePattern)) {
            return true;
        }

        // Additional validation for valid date values
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        // Check for valid month
        if (month < 1 || month > 12) {
            return true;
        }

        // Check for valid day based on the month
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return day < 1 || day > 31;
            case 4: case 6: case 9: case 11:
                return day < 1 || day > 30;
            case 2:
                // Leap year check
                boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
                return day < 1 || day > (isLeapYear ? 29 : 28);
            default:
                return true;
        }
    }

    private void clearInputFields() {
        travelLocationET.setText("");
        estimatedStartET.setText("");
        estimatedEndET.setText("");
    }

    private int calculateDays(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return (int) ChronoUnit.DAYS.between(start, end);
    }
}
