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
import com.example.sprintproject.viewmodel.AccommodationViewModel;

public class AddAccommodationsDialog extends Dialog {
    private AccommodationViewModel accommodationViewModel;
    private LifecycleOwner lifecycleOwner;
    private final FirestoreSingleton firestore = FirestoreSingleton.getInstance();

    public AddAccommodationsDialog(Context context, AccommodationViewModel accommodationViewModel) {
        super(context);  // Calls the Dialog constructor
        this.accommodationViewModel = accommodationViewModel;
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    @Override
    public void show() {
        super.show();  // Calls the show method of the Dialog class to display the dialog
        setContentView(R.layout.dialog_add_accommodation);

        // Set background drawable
        getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_bg);

        // Find views
        EditText locationET = findViewById(R.id.editTextAccommodationLocation);
        EditText checkInTimeET = findViewById(R.id.editTextAccommodationCheckInTime);
        EditText checkOutTimeET = findViewById(R.id.editTextAccommodationCheckOutTime);
        EditText hotelNameET = findViewById(R.id.editTextHotelName);
        Spinner numberOfRoomsSpinner = findViewById(R.id.numberOfRoomsSpinner);
        Spinner roomTypeSpinner = findViewById(R.id.roomTypeSpinner);
        Button addAccommodationButton = findViewById(R.id.btnAddAccommodationDialog);

        // Handle add accommodation button click
        addAccommodationButton.setOnClickListener(view -> {
            String location = locationET.getText().toString().trim();
            String checkInTime = checkInTimeET.getText().toString().trim();
            String checkOutTime = checkOutTimeET.getText().toString().trim();
            String hotelName = hotelNameET.getText().toString().trim();
            String numRoomsStr = numberOfRoomsSpinner.getSelectedItem().toString();
            int numRooms = Integer.parseInt(numRoomsStr);
            String roomType = roomTypeSpinner.getSelectedItem().toString().trim();

            // Validate inputs
            if (validateInputs(locationET, checkInTimeET, checkOutTimeET, hotelNameET)) {
                // Call to the view model to validate and add accommodation

                // Observe validation result
                        // Create a new Accommodation object
                        Accommodation newAccommodation = new Accommodation(
                                hotelName,
                                location,
                                checkInTime,
                                checkOutTime,
                                numRooms,
                                roomType,
                                firestore.getCurrentUserId()
                        );

                        // Add the accommodation to the database
                        accommodationViewModel.addAccommodation(newAccommodation);

                        // Update the UI and clear input fields
                        clearInputFields(locationET, checkInTimeET, checkOutTimeET, hotelNameET);

                        // Show success message
                        Toast.makeText(getContext(), "Accommodation added successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Validate input fields
    private boolean validateInputs(EditText locationField, EditText checkInField, EditText checkOutField, EditText hotelField) {
        boolean isValid = true;

        // Check if any field is empty and show an error message
        if (TextUtils.isEmpty(locationField.getText())) {
            locationField.setError("Location is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(checkInField.getText())) {
            checkInField.setError("Check-in time is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(checkOutField.getText())) {
            checkOutField.setError("Check-out time is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(hotelField.getText())) {
            hotelField.setError("Hotel name is required");
            isValid = false;
        }

        return isValid;
    }

    // Clear input fields after submission
    private void clearInputFields(EditText locationET, EditText checkInET, EditText checkOutET, EditText hotelET) {
        locationET.setText("");
        checkInET.setText("");
        checkOutET.setText("");
        hotelET.setText("");
    }
}
