package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.sprintproject.R;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.TravelLog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {
    private FirestoreSingleton firestoreSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

        // initialize the FirestoreSingleton instance
        firestoreSingleton = FirestoreSingleton.getInstance();

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        ImageButton addUsersButton = findViewById(R.id.addUsersBtn);

        diningEstablishmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diningEstablishmentsIntent = new Intent(LogisticsActivity.this,
                        DiningEstablishmentsActivity.class);
                startActivity(diningEstablishmentsIntent);
            }
        });

        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent destinationsIntent = new Intent(LogisticsActivity.this,
                        DestinationsActivity.class);
                startActivity(destinationsIntent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(LogisticsActivity.this,
                        AccommodationsActivity.class);
                startActivity(accommodationsIntent);
            }
        });

        travelCommunityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent travelCommunityIntent = new Intent(LogisticsActivity.this,
                        TravelCommunityActivity.class);
                startActivity(travelCommunityIntent);
            }
        });

        // add users button
        addUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // will give a pop up to allow user to enter email of the person they want to invite
                showAddUserDialog();
            }
        });
    }

    // method for the pop up
    private void showAddUserDialog() {
        // inflate the custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.invite_user_popup, null);

        // get references to the email input and the spinner
        final EditText emailInput = dialogView.findViewById(R.id.emailInput);
        final Spinner locationSpinner = dialogView.findViewById(R.id.locationSpinner);

        // populate the spinner with a list of locations that are in the user's database
        getUserAssociatedLocations(locationSpinner);

        // Build the dialog
        new AlertDialog.Builder(this)
                .setTitle("Invite User")
                .setMessage("Enter the email and select a location:")
                .setView(dialogView)
                .setPositiveButton("Invite", (dialog, whichButton) -> {
                    String email = emailInput.getText().toString();
                    String selectedLocation = (String) locationSpinner.getSelectedItem();
                    if (!email.isEmpty() && selectedLocation != null) {
                        // handle the invite logic, including the selected location
                        Toast.makeText(LogisticsActivity.this, "Invitation sent to: " + email + " for location: " + selectedLocation, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogisticsActivity.this, "Please enter a valid email and select a location", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // method to get user's associated locations from Firestore and populate the spinner
    private void getUserAssociatedLocations(final Spinner locationSpinner) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // fetch the travel logs for the current user from Firestore
        firestoreSingleton.getTravelLogsByUser(currentUserId).observe(this, travelLogs -> {
            List<String> locations = new ArrayList<>();
            for (TravelLog log : travelLogs) {
                locations.add(log.getDestination()); // Extract locations from each TravelLog
            }

            // populate the spinner with the list of locations
            ArrayAdapter<String> adapter = new ArrayAdapter<>(LogisticsActivity.this, android.R.layout.simple_spinner_item, locations);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);
        });
    }

    // Function to handle the invite logic, like adding the invited user to Firestore or sending an invite
    private void inviteUserToTrip(String email, String location) {
        // TODO: Implement Firestore logic for inviting a user (e.g., add the user to a trip collection)
        Toast.makeText(this, "Invitation sent to " + email + " for location " + location, Toast.LENGTH_SHORT).show();
    }
}