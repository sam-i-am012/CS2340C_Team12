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
    private EditText destinationET;
    private EditText startDateET;
    private EditText endDateET;
    private EditText accommodationsET;
    private EditText diningReservation;
    private FloatingActionButton addTravelPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_community);

        destinationET = findViewById(R.id.editTextDesination);
        startDateET = findViewById(R.id.editTextStartDate);
        endDateET = findViewById(R.id.editTextEndDate);
        accommodationsET = findViewById(R.id.editTextAccommodations);
        diningReservation = findViewById(R.id.editTextDining);
        addTravelPostButton = findViewById(R.id.addPostButton);

        ImageButton diningEstablishmentsButton = findViewById(R.id.diningEstablishmentsButton);
        ImageButton destinationsButton = findViewById(R.id.destinationsButton);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);

        viewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        addTravelPostButton.setOnClickListener(view -> {
            AddPostDialog addPostDialog = new AddPostDialog(
                    TravelCommunityActivity.this, viewModel);
            addPostDialog.show();
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.travelPostRecyclerView);
        travelPostAdapter = new PostAdapter();
        recyclerView.setAdapter(travelPostAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        // Observe the LiveData for updates to logs
        viewModel.getTravelPostsLiveData().observe(this, posts -> {
            // update adapter when data changes
            travelPostAdapter.setPosts(posts);
        });

        diningEstablishmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent diningEstablishmentsIntent = new Intent(TravelCommunityActivity.this,
                        DiningEstablishmentsActivity.class);
                startActivity(diningEstablishmentsIntent);
            }
        });

        destinationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent destinationsIntent = new Intent(TravelCommunityActivity.this,
                        DestinationsActivity.class);
                startActivity(destinationsIntent);
            }
        });

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logisticsIntent = new Intent(TravelCommunityActivity.this,
                        LogisticsActivity.class);
                startActivity(logisticsIntent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accommodationsIntent = new Intent(TravelCommunityActivity.this,
                        AccommodationsActivity.class);
                startActivity(accommodationsIntent);
            }
        });
    }
}