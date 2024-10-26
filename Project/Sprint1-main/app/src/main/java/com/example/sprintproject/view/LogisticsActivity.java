package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;

public class LogisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);

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
                showAddUserDialog();
            }
        });
    }

    private void showAddUserDialog() {
        // create an EditText for user input
        final EditText emailInput = new EditText(this);
        emailInput.setHint("Enter email");

        // build the dialog
        new AlertDialog.Builder(this)
                .setTitle("Invite User")
                .setMessage("Enter the email of the user you want to invite:")
                .setView(emailInput)
                .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String email = emailInput.getText().toString();
                        if (!email.isEmpty()) {
                            // TODO: pass to view model
                            Toast.makeText(LogisticsActivity.this, "Invitation sent to: " + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogisticsActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}