package com.example.sprintproject.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.viewmodel.AccommodationViewModel;

public class AddAccommodationsDialog extends DialogFragment {

    // Define a callback interface
    public interface OnAccommodationAddedListener {
        void onAccommodationAdded(Accommodation accommodation);
    }

    private OnAccommodationAddedListener callback;
    private AccommodationViewModel accommodationViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        accommodationViewModel = new ViewModelProvider(requireActivity()).get(AccommodationViewModel.class);

        // Attach the parent activity as the callback
        if (getActivity() instanceof OnAccommodationAddedListener) {
            callback = (OnAccommodationAddedListener) getActivity();
        } else {
            throw new RuntimeException("Activity must implement OnAccommodationAddedListener");
        }

        // Inflate the dialog view
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_accommodation, null);

        // Input fields and button references
        EditText editTextCheckInAccommodation = dialogView.findViewById(R.id.editTextAccommodationCheckInTime);
        EditText editTextCheckOutAccommodation = dialogView.findViewById(R.id.editTextAccommodationCheckOutTime);
        EditText editTextAccommodationLocation = dialogView.findViewById(R.id.editTextAccommodationLocation);
        EditText editTextHotelName = dialogView.findViewById(R.id.editTextHotelName);
        Spinner numberOfRoomsSpinner = dialogView.findViewById(R.id.numberOfRoomsSpinner);
        Spinner roomTypeSpinner = dialogView.findViewById(R.id.roomTypeSpinner);
        Button btnAddAccommodationDialog = dialogView.findViewById(R.id.btnAddAccommodationDialog);

        // Set up the button to save accommodation
        btnAddAccommodationDialog.setOnClickListener(view -> {
            String checkInTime = editTextCheckInAccommodation.getText().toString();
            String checkOutTime = editTextCheckOutAccommodation.getText().toString();
            String location = editTextAccommodationLocation.getText().toString();
            String hotelName = editTextHotelName.getText().toString();
            String numRoomsStr = numberOfRoomsSpinner.getSelectedItem().toString();
            int numRooms = Integer.parseInt(numRoomsStr);
            String roomType = roomTypeSpinner.getSelectedItem().toString();

            if (validateInputs(editTextCheckInAccommodation, editTextCheckOutAccommodation, editTextAccommodationLocation, editTextHotelName)) {
                Accommodation newAccommodation = new Accommodation(
                        hotelName,
                        location,
                        checkInTime,
                        checkOutTime,
                        numRooms,
                        roomType,
                        accommodationViewModel.getCurrentUserId()
                );

                // Pass the new accommodation back to the activity
                if (callback != null) {
                    callback.onAccommodationAdded(newAccommodation);
                }

                // Dismiss the dialog
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView);

        return builder.create();
    }

    private boolean validateInputs(EditText checkInField, EditText checkOutField, EditText locationField, EditText hotelField) {
        boolean isValid = true;
        if (TextUtils.isEmpty(checkInField.getText())) {
            checkInField.setError("Check-in time required");
            isValid = false;
        }
        if (TextUtils.isEmpty(checkOutField.getText())) {
            checkOutField.setError("Check-out time required");
            isValid = false;
        }
        if (TextUtils.isEmpty(locationField.getText())) {
            locationField.setError("Location required");
            isValid = false;
        }
        if (TextUtils.isEmpty(hotelField.getText())) {
            hotelField.setError("Hotel name required");
            isValid = false;
        }
        return isValid;
    }
}
