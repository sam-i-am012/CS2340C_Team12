package com.example.sprintproject.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.viewmodel.DiningViewModel;

public class AddReservationDialog extends Dialog {
    private DiningViewModel diningViewModel;
    private LifecycleOwner lifecycleOwner;
    private TextView location;
    private TextView restaurantName;
    private TextView time;
    private TextView website;
    private final FirestoreSingleton firestore = FirestoreSingleton.getInstance();

    public AddReservationDialog(Context context, DiningViewModel diningViewModel) {
        super(context);  // Calls the Dialog constructor
        this.diningViewModel = diningViewModel;
        this.lifecycleOwner = (LifecycleOwner) context;
    }

    @Override
    public void show() {
        super.show();  // Calls the show method of the Dialog class to display the dialog
        setContentView(R.layout.dialog_add_reservation);

        // Set background drawable
        getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog_bg);

        // Find views
        EditText nameET = findViewById(R.id.etName);
        EditText timeET = findViewById(R.id.etTime);
        EditText locationET = findViewById(R.id.etLocation);
        EditText websiteET = findViewById(R.id.etWebsite);
        Button addReservationButton = findViewById(R.id.btnAddReservationDialog);

        // Handle add reservation button click
        addReservationButton.setOnClickListener(view -> {
            String name = nameET.getText().toString().trim();
            String time = timeET.getText().toString().trim();
            String location = locationET.getText().toString().trim();
            String website = websiteET.getText().toString().trim();

            diningViewModel.validateNewReservation(name, time, location, website);

            // Observe reservation result
            diningViewModel.getResValidationResult().observe(lifecycleOwner ,
                    result -> {
                        Toast.makeText(getContext(), result.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        if (result.isSuccess()) {
                            Dining dining = new Dining(location, website, name, time,
                                    firestore.getCurrentUserId());

                            // Add reservation to database
                            diningViewModel.addDining(dining);

                            // Add reservation to recycler
                            diningViewModel.addLog(dining);

                            clearInputFields();
                        }
                    });
        });
    }

    private void clearInputFields() {
        if (location != null) location.setText("");
        if (restaurantName != null) restaurantName.setText("");
        if (time != null) time.setText("");
        if (website != null) website.setText("");
    }
}

