package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.sprintproject.R;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.viewmodel.DestinationsViewModel;

import java.text.ParseException;
import java.util.Arrays;

public class DestinationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DestinationsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(DestinationsViewModel.class);

        // Call method to populate the database and fetch logs
        FirestoreSingleton firestore = FirestoreSingleton.getInstance();

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        //calculate vacation time ----
        Button calcVacationTimeButton = findViewById(R.id.calcVacationTimeButton);
        EditText startDateET = findViewById(R.id.startDateET);
        EditText endDateET = findViewById(R.id.endDateET);
        EditText durationET = findViewById(R.id.durationET);
        Button calculateButton = findViewById(R.id.calculateButton);
        Button submitTimeButton = findViewById(R.id.submitTimeButton);
        //log travel ----
        Button logTravelButton = findViewById(R.id.logTravelButton);
        TextView travelLocationTV = findViewById(R.id.travelLocationsTV);
        EditText travelLocationET = findViewById(R.id.travelLocationsET);
        TextView estimatedStartTV = findViewById(R.id.estimatedStartTV);
        EditText estimatedStartET = findViewById(R.id.estimatedStartET);
        TextView estimatedEndTV = findViewById(R.id.estimatedEndTV);
        EditText estimatedEndET = findViewById(R.id.estimatedEndET);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button submitButton = findViewById(R.id.submitButton);

        /*
         * Code for handling when Log Travel button is pressed
         */
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
                }
            }
        });
        /*
         * Code for handling when the Calculate Vacation Time button is pressed
         */
        calcVacationTimeButton.setOnClickListener(v -> {
            if (startDateET.getVisibility() == View.GONE) {
                // Make dialog elements visible
                for (EditText editText : Arrays.asList(startDateET, endDateET, durationET)) {
                    editText.setVisibility(View.VISIBLE);
                }
                calculateButton.setVisibility(View.VISIBLE);
                submitTimeButton.setVisibility(View.VISIBLE);
            } else {
                // Hide dialog elements
                for (EditText editText : Arrays.asList(startDateET, endDateET, durationET)) {
                    editText.setVisibility(View.GONE);
                }
                calculateButton.setVisibility(View.GONE);
                submitTimeButton.setVisibility(View.GONE);

            }
        });

        /*
         * Code for when Calculate button is pressed
         */
        calculateButton.setOnClickListener(v -> {
            String startDate = startDateET.getText().toString();
            String endDate = endDateET.getText().toString();
            String duration = durationET.getText().toString();
            String entry;

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
            } else {
                Toast.makeText(DestinationsActivity.this, missingEntry.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        /*
         * Code for handling when buttons are pressed in the navigation bar
         */
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
}