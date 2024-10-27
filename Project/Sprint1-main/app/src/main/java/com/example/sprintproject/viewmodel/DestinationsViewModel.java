package com.example.sprintproject.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.FirestoreSingleton;
import com.example.sprintproject.model.ValidationManager;
import com.example.sprintproject.model.Result;
import com.example.sprintproject.model.VacationTimeCalculator;

import java.text.ParseException;

public class DestinationsViewModel extends ViewModel {
    private FirestoreSingleton repository;
    private VacationTimeCalculator vtCalculator = new VacationTimeCalculator();
    private ValidationManager ValidationManager = new ValidationManager();

    public DestinationsViewModel() {
        repository = FirestoreSingleton.getInstance();
    }

    public Result validateMissingEntry(String startDate, String endDate, String duration) {
        return ValidationManager.validateMissingEntry(startDate, endDate, duration);
    }

    public Result validateDateRange(String startDate, String endDate) {
        return ValidationManager.validateDateRange(startDate, endDate);
    }

    public String calculateMissingEntry(String entry1, String entry2) {
        return vtCalculator.calculateEntry(entry1, entry2);
    }
}