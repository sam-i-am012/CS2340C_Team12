package com.example.sprintproject.view;

import android.app.Dialog;
import android.content.DialogInterface;
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

    private AccommodationViewModel accommodationViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        accommodationViewModel = new ViewModelProvider(requireActivity()).get(AccommodationViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_accommodation, null);

        EditText editTextCheckInAccommodation = dialogView.findViewById(R.id.editTextAccommodationCheckInTime);
        EditText editTextCheckOutAccommodation = dialogView.findViewById(R.id.editTextAccommodationCheckOutTime);
        EditText editTextAccommodationLocation = dialogView.findViewById(R.id.editTextAccommodationLocation);
        EditText editTextHotelName = dialogView.findViewById(R.id.editTextHotelName);
        Spinner numberOfRoomsSpinner = dialogView.findViewById(R.id.numberOfRoomsSpinner);
        Spinner roomTypeSpinner = dialogView.findViewById(R.id.roomTypeSpinner);
        Button btnAddAccommodationDialog = dialogView.findViewById(R.id.btnAddAccommodationDialog);

        builder.setView(dialogView)
                .setTitle("Add Accommodation")
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String name = editTextHotelName.getText().toString().trim();  // editTextName
                    String location = editTextAccommodationLocation.getText().toString().trim();  // editTextLocation

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(location)) {
                        Accommodation accommodation = new Accommodation(name, location);
                        accommodationViewModel.addAccommodation(accommodation);
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        return builder.create();
    }
}
