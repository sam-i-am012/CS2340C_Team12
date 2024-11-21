package com.example.sprintproject.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.Post;
import com.example.sprintproject.viewmodel.AccommodationViewModel;
import com.example.sprintproject.viewmodel.CommunityViewModel;

public class AddPostDialog extends Dialog {
    private CommunityViewModel viewModel;
    private LifecycleOwner lifecycleOwner;
    private final FirestoreSingleton firestore = FirestoreSingleton.getInstance();

    public AddPostDialog(Context context, CommunityViewModel viewModel) {
        super(context);  // Calls the Dialog constructor
        this.viewModel = viewModel;
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    @Override
    public void show() {
        super.show();  // Calls the show method of the Dialog class to display the dialog
        setContentView(R.layout.dialog_add_post);

        // Set background drawable
        getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_bg);

        // Find views
        EditText startDateET = findViewById(R.id.editTextStartDate);
        EditText endDateET = findViewById(R.id.editTextEndDate);
        EditText destinationET = findViewById(R.id.editTextDesination);
        EditText accommodationsET = findViewById(R.id.editTextAccommodations);
        EditText diningET = findViewById(R.id.editTextDining);
        EditText notesET = findViewById(R.id.editTextNotes);
        Button addPostBtn = findViewById(R.id.btnAddPostDialog);

        // Handle add accommodation button click
        addPostBtn.setOnClickListener(view -> {
            String startDate = startDateET.getText().toString().trim();
            String endDate = endDateET.getText().toString().trim();
            String destination = destinationET.getText().toString().trim();
            String accommodations = accommodationsET.getText().toString().trim();
            String dining = diningET.getText().toString().trim();
            String notes = notesET.getText().toString().trim();

            // Validate inputs
            if (validateInputs(startDateET, endDateET, destinationET, accommodationsET, diningET, notesET)) {
                // Create a new Accommodation object
                Post newPost = new Post(
                        firestore.getCurrentUserId(),
                        startDate,
                        endDate,
                        destination,
                        accommodations,
                        dining,
                        notes
                );

                // Add the accommodation to the database
                viewModel.addTravelPost(newPost, null);

                // Update the UI and clear input fields
                clearInputFields(startDateET, endDateET, destinationET, accommodationsET, diningET, notesET);

                // Show success message
                Toast.makeText(getContext(), "Post added successfully",
                        Toast.LENGTH_SHORT).show();

                // Dismiss the dialog after adding the accommodation
                dismiss();
            }
        });
    }


    // Validate input fields
    private boolean validateInputs(EditText startDateET, EditText endDateET, EditText destinationET,
                                   EditText accommodationsET, EditText diningET, EditText notesET) {
        boolean isValid = true;

        // Check if any field is empty and show an error message
        if (TextUtils.isEmpty(startDateET.getText())) {
            startDateET.setError("Start Date is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(endDateET.getText())) {
            endDateET.setError("End Date is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(destinationET.getText())) {
            destinationET.setError("Destination is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(accommodationsET.getText())) {
            accommodationsET.setError("Accommodations are required");
            isValid = false;
        }
        if (TextUtils.isEmpty(diningET.getText())) {
            diningET.setError("Dining is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(notesET.getText())) {
            notesET.setError("Notes are required");
            isValid = false;
        }
        if (isDateFormatInvalid(startDateET.getText().toString().trim())) {
            startDateET.setError("Enter YYYY-MM-DD");
            isValid = false;
        }
        if (isDateFormatInvalid(endDateET.getText().toString().trim())) {
            endDateET.setError("Enter YYYY-MM-DD");
            isValid = false;
        }

        return isValid;
    }

    // Clear input fields after submission
    private void clearInputFields(EditText startDateET, EditText endDateET, EditText destinationET,
                                  EditText accommodationsET, EditText diningET, EditText notesET) {
        startDateET.setText("");
        endDateET.setText("");
        destinationET.setText("");
        accommodationsET.setText("");
        diningET.setText("");
        notesET.setText("");
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
}
