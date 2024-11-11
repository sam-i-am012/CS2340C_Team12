package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Invitation;
import com.example.sprintproject.viewmodel.LogisticsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {
    private LogisticsViewModel viewModel;
    private PieChart pieChart;
    private int currentPlannedDays = 0;
    private int currentAllocatedDays = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);


        // Observe the toast message live data
        viewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(LogisticsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // observe the planned days
        viewModel.getPlannedDaysLiveData().observe(this, plannedDays -> {
            currentPlannedDays = plannedDays;
        });

        // observe allocated days
        viewModel.getAllocatedLiveData().observe(this, allocatedDays -> {
            currentAllocatedDays = allocatedDays;
        });

        // observe the invitation live data
        viewModel.getInvitationLiveData().observe(this, invitation -> {
            if (invitation != null) {
                // Show the invitation dialog
                showInvitationDialog(invitation);
            }
        });

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        ImageButton addUsersButton = findViewById(R.id.addUsersBtn);
        ImageButton addNoteBtn = findViewById(R.id.addNoteBtn);
        Button viewDataBtn = findViewById(R.id.viewDataButton);
        Button viewCollabAndNotesBtn = findViewById(R.id.viewCollabsAndNotes);
        Spinner locationSpinner = findViewById(R.id.locationSpinner);
        pieChart = findViewById(R.id.pieChart);

        // getting pie chart button working
        pieChart.setVisibility(View.GONE);
        viewDataBtn.setOnClickListener(view -> {
            // check if chart is currently visible
            if (pieChart.getVisibility() == View.VISIBLE) {
                pieChart.setVisibility(View.GONE); // hide if visible
            } else {
                // ff not visible, show the pie chart
                visualizeTripDays(currentPlannedDays, currentAllocatedDays);
            }
        });

        // view collaborator/notes button
        viewCollabAndNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make the spinner visible when the button is clicked
                locationSpinner.setVisibility(View.VISIBLE);

                // populate the spinner with locations after it's made visible
                populateLocationSpinner();
            }
        });



        diningEstablishmentsButton.setOnClickListener(view -> {
            Intent diningEstablishmentsIntent = new Intent(LogisticsActivity.this,
                    DiningEstablishmentsActivity.class);
            startActivity(diningEstablishmentsIntent);
        });

        destinationsButton.setOnClickListener(view -> {
            Intent destinationsIntent = new Intent(LogisticsActivity.this,
                    DestinationsActivity.class);
            startActivity(destinationsIntent);
        });

        accommodationsButton.setOnClickListener(view -> {
            Intent accommodationsIntent = new Intent(LogisticsActivity.this,
                    AccommodationsActivity.class);
            startActivity(accommodationsIntent);
        });

        travelCommunityButton.setOnClickListener(view -> {
            Intent travelCommunityIntent = new Intent(LogisticsActivity.this,
                    TravelCommunityActivity.class);
            startActivity(travelCommunityIntent);
        });

        // Add users button
        addUsersButton.setOnClickListener(view -> showAddUserDialog());

        addNoteBtn.setOnClickListener(view ->
                Toast.makeText(getApplicationContext(), "New note button pressed",
                        Toast.LENGTH_SHORT).show()
        );
    }

    private void visualizeTripDays(int plannedDays, int allottedDays) {
        // Create pie chart entries
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(plannedDays, "Planned Days"));
        entries.add(new PieEntry(allottedDays - plannedDays, "Remaining Allotted Days"));

        // Create dataset
        PieDataSet dataSet = new PieDataSet(entries, "Trip Days");

        // customize pie chart data numbers
        dataSet.setColors(Color.parseColor("#78979c"), Color.parseColor("#b0bdbf"), Color.BLACK);
        dataSet.setSliceSpace(2f);  // Set space between slices
        dataSet.setValueTextSize(24f);  // Set text size for values inside slices
        dataSet.setValueTextColor(Color.BLACK);  // Set value text color


        // Customize pie chart appearance
        pieChart.setHoleRadius(40f);  // Hole in the middle
        pieChart.setTransparentCircleRadius(45f);  // Transparent circle around hole
        pieChart.setCenterText("Trip Days");
        pieChart.setCenterTextSize(16f);  // Text size for center text
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(10f, 0f, 10f, 8f); // to avoid clipping


        // Set label color for each entry (Planned Days, Remaining Allotted Days)
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(15f);

        pieChart.getLegend().setEnabled(false);

        // Animate the chart
        pieChart.animateY(1000);  // Animation for showing chart

        // Create pie data and set it to chart
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();  // Refresh chart

        // Make chart visible
        pieChart.setVisibility(View.VISIBLE);
    }

    // Method for the pop-up dialog
    private void showAddUserDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.invite_user_popup, null);

        final EditText emailInput = dialogView.findViewById(R.id.emailInput);
        final Spinner locationSpinner = dialogView.findViewById(R.id.locationSpinner);

        // Observe locations from the ViewModel and populate the spinner
        viewModel.getUserLocations().observe(this, locations -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, locations);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);
        });

        // Build the dialog
        new AlertDialog.Builder(this)
                .setTitle("Invite User")
                .setMessage("Enter the email and select a location:")
                .setView(dialogView)
                .setPositiveButton("Invite", (dialog, whichButton) -> {
                    String email = emailInput.getText().toString();
                    String selectedLocation = (String) locationSpinner.getSelectedItem();

                    if (!email.isEmpty() && selectedLocation != null) {
                        // Call ViewModel to handle invitation logic
                        viewModel.inviteUserToTrip(email, selectedLocation);
                    } else {
                        Toast.makeText(LogisticsActivity.this,
                                "Please enter a valid email and select a location",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showInvitationDialog(Invitation invitation) {
        new AlertDialog.Builder(this)
                .setTitle("Trip Invitation")
                .setMessage("You have been invited to a trip to " + invitation.getTripLocation()
                + " with " + invitation.getInvitingUserEmail())
                .setPositiveButton("Accept", (dialog, which) -> {
                    viewModel.acceptInvitation(invitation);
                })
                .setNegativeButton("Reject", (dialog, which) -> {
                    viewModel.updateInvitationStatus(invitation.getInvitationId(), "rejected");
                })
                .setCancelable(false) // prevent closing dialog without action
                .show();
    }

    // for view collab/notes button
    private void populateLocationSpinner() {
        viewModel.getUserLocations().observe(LogisticsActivity.this, locations -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(LogisticsActivity.this,
                    android.R.layout.simple_spinner_item, locations);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);
        });

        // Optionally load user locations into ViewModel, if not done elsewhere
        List<String> userLocations = getUserAssociatedLocations();
        viewModel.loadUserLocations(userLocations);
    }
}
