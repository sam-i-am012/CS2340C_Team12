package com.example.sprintproject.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

        final EditText editTextName = dialogView.findViewById(R.id.editTextAccommodationName);
        final EditText editTextLocation = dialogView.findViewById(R.id.editTextAccommodationLocation);

        builder.setView(dialogView)
                .setTitle("Add Accommodation")
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    String name = editTextName.getText().toString().trim();
                    String location = editTextLocation.getText().toString().trim();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(location)) {
                        Accommodation accommodation = new Accommodation(name, location);
                        accommodationViewModel.addAccommodation(accommodation);
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        return builder.create();
    }
}
