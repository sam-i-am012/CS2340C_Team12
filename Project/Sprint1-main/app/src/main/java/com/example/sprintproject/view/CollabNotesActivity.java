package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sprintproject.R;
import com.example.sprintproject.model.User;
import com.example.sprintproject.viewmodel.LogisticsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CollabNotesActivity extends AppCompatActivity {
    private LogisticsViewModel viewModel;
    private CollaboratorsAdapter collaboratorsAdapter;
    private Spinner locationSpinner;
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;

    private String selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_notes);


        // yes ik it uses the logistics view model (oopsie)
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        // Observe the toast message live data
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(CollabNotesActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // initialize RecyclerView for notes display
        notesRecyclerView = findViewById(R.id.recyclerViewNotes);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(new ArrayList<>());
        notesRecyclerView.setAdapter(notesAdapter);




        RecyclerView collaboratorsRecyclerView = findViewById(R.id.collaboratorsRecyclerView);
        collaboratorsAdapter = new CollaboratorsAdapter(new ArrayList<>());
        collaboratorsRecyclerView.setAdapter(collaboratorsAdapter);
        collaboratorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton travelCommunityButton = findViewById(R.id.travelCommunityButton);
        ImageButton addUsersButton = findViewById(R.id.addUsersBtn);
        ImageButton addNoteBtn = findViewById(R.id.addNoteBtn);
        ImageButton backBtn = findViewById(R.id.back);
        locationSpinner = findViewById(R.id.locationSpinner);

        locationSpinner.setVisibility(View.VISIBLE);

        // populate the spinner with locations after it's made visible
        populateLocationSpinner(locationSpinner);


        // on location that is selected
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLocation = (String) parent.getItemAtPosition(position);
                fetchCollaboratorsForLocation(selectedLocation);

                viewModel.getNotesForTravelLog(selectedLocation).observe(CollabNotesActivity.this, notes -> {
                    if (notes != null) {
                        notesAdapter.updateNotes(notes);
                        Log.d("Notes", "Fetched notes: " + notes.size());
                    } else {
                        notesAdapter.updateNotes(new ArrayList<>());
                        Log.d("Notes", "No notes found.");
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        diningEstablishmentsButton.setOnClickListener(view -> {
            Intent diningEstablishmentsIntent = new Intent(CollabNotesActivity.this,
                    DiningEstablishmentsActivity.class);
            startActivity(diningEstablishmentsIntent);
        });

        destinationsButton.setOnClickListener(view -> {
            Intent destinationsIntent = new Intent(CollabNotesActivity.this,
                    DestinationsActivity.class);
            startActivity(destinationsIntent);
        });

        accommodationsButton.setOnClickListener(view -> {
            Intent accommodationsIntent = new Intent(CollabNotesActivity.this,
                    AccommodationsActivity.class);
            startActivity(accommodationsIntent);
        });

        travelCommunityButton.setOnClickListener(view -> {
            Intent travelCommunityIntent = new Intent(CollabNotesActivity.this,
                    TravelCommunityActivity.class);
            startActivity(travelCommunityIntent);
        });

        // Add users button
        addUsersButton.setOnClickListener(view -> showAddUserDialog());

        // add note button
        addNoteBtn.setOnClickListener(view -> showAddNoteDialog());

        // go back button
        backBtn.setOnClickListener(view -> {
            Intent mainActivityLogistics = new Intent(CollabNotesActivity.this,
                    LogisticsActivity.class);
            startActivity(mainActivityLogistics);
        });
    }


    // for the pop up dialog for add note
    private void showAddNoteDialog() {
        final EditText noteEditText = new EditText(this);
        noteEditText.setHint("Enter your note");

        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note")
                .setView(noteEditText)
                .setPositiveButton("Add", (dialog, which) -> {
                    String noteContent = noteEditText.getText().toString().trim();

                    if (!noteContent.isEmpty()) {
                        viewModel.addNoteToTravelLog(selectedLocation, noteContent);

                        Toast.makeText(this, "Note sent", Toast.LENGTH_SHORT).show();

                        // manually set the selection back to the previous location
                        int position = ((ArrayAdapter<String>) locationSpinner.getAdapter()).getPosition(selectedLocation);
                        locationSpinner.setSelection(position);


                    } else {
                        Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    // for the pop up dialog for add collaborator
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
                        Toast.makeText(CollabNotesActivity.this,
                                "Please enter a valid email and select a location",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void populateLocationSpinner(Spinner locationSpinner) {
        viewModel.getUserLocations().observe(CollabNotesActivity.this, locations -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(CollabNotesActivity.this,
                    android.R.layout.simple_spinner_item, locations);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationSpinner.setAdapter(adapter);

            // After population, set initial selected location
            if (selectedLocation != null) {
                int position = adapter.getPosition(selectedLocation);
                locationSpinner.setSelection(position);
            }
        });
    }

    private void fetchCollaboratorsForLocation(String location) {
        viewModel.getCollaboratorsForLocation(location).observe(this, collaborators -> {
            if (collaborators != null && !collaborators.isEmpty()) {
                List<String> collaboratorEmails = new ArrayList<>();
                for (User user : collaborators) {
                    collaboratorEmails.add(user.getEmail());
                }
                collaboratorsAdapter.updateCollaborators(collaboratorEmails);
                Log.d("CollabNotesActivity", "Fetched emails: " + collaboratorEmails);
            } else {
                Log.d("CollabNotesActivity", "No collaborators found for location: " + location);
            }
        });
    }
}