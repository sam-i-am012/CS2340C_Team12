package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Post;
import com.example.sprintproject.viewmodel.CommunityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class TravelCommunityActivity extends AppCompatActivity {

    private CommunityViewModel viewModel;
    private PostAdapter travelPostAdapter;
    private RecyclerView recyclerView;

    private EditText startDateET;
    private EditText endDateET;
    private EditText destinationET;
    private EditText accommodationsET;
    private EditText diningReservationET;
    private EditText notesET;
    private FloatingActionButton addTravelPostButton;
    private ImageButton diningEstablishmentsButton;
    private ImageButton destinationsButton;
    private ImageButton accommodationsButton;
    private ImageButton logisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_community);

        initViews();

        viewModel = new ViewModelProvider(this).get(CommunityViewModel.class);

        addTravelPostButton.setOnClickListener(view -> {
            AddPostDialog addPostDialog = new AddPostDialog(
                    TravelCommunityActivity.this, viewModel);
            addPostDialog.show();
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.travelPostRecyclerView);
        travelPostAdapter = new PostAdapter(this);
        recyclerView.setAdapter(travelPostAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Observe the LiveData for updates to logs
        viewModel.getTravelPostsLiveData().observe(this, posts -> {
            // update adapter when data changes
            if (posts != null) {
                travelPostAdapter.setPosts(posts);
            }
        });

        // Navigation button logic
        navButtonsLogic();
    }

    private void initViews() {
        startDateET = findViewById(R.id.editTextStartDate);
        endDateET = findViewById(R.id.editTextEndDate);
        destinationET = findViewById(R.id.editTextDestination);
        accommodationsET = findViewById(R.id.editTextAccommodations);
        diningReservationET = findViewById(R.id.editTextDining);
        notesET = findViewById(R.id.editTextNotes);
        addTravelPostButton = findViewById(R.id.addPostButton);
        diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        destinationsButton = findViewById(R.id.destinationsButton);
        logisticsButton = findViewById(R.id.logisticsButton);
        accommodationsButton = findViewById(R.id.accommodationsButton);
    }

    private void navButtonsLogic() {
        // Handle navigation bar button presses
        diningEstablishmentsButton.setOnClickListener(view -> {
            Intent diningEstablishmentsIntent = new Intent(TravelCommunityActivity.this,
                    DiningEstablishmentsActivity.class);
            startActivity(diningEstablishmentsIntent);
        });

        destinationsButton.setOnClickListener(view -> {
            Intent destinationsIntent = new Intent(TravelCommunityActivity.this,
                    DestinationsActivity.class);
            startActivity(destinationsIntent);
        });

        logisticsButton.setOnClickListener(view -> {
            Intent logisticsIntent = new Intent(TravelCommunityActivity.this,
                    LogisticsActivity.class);
            startActivity(logisticsIntent);
        });

        accommodationsButton.setOnClickListener(view -> {
            Intent accommodationsIntent = new Intent(TravelCommunityActivity.this,
                    AccommodationsActivity.class);
            startActivity(accommodationsIntent);
        });
    }

    private void clearInputFields() {
        startDateET.setText("");
        endDateET.setText("");
        destinationET.setText("");
        accommodationsET.setText("");
        diningReservationET.setText("");
        notesET.setText("");
    }
}