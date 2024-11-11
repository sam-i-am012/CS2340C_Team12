package com.example.sprintproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.AccommodationValidator;

import org.junit.Before;
import org.junit.Test;

public class AccommodationValidatorTest {
    private String checkIn;
    private String checkOut;
    private String location;
    private String hotel;

    @Before
    public void setUp() {
        checkIn = "";
        checkOut = "";
        location = "";
        hotel = "";
    }
    @Test
    public void emptyCheckOut() {
        // check out is empty
        checkIn = "5:00";
        checkOut = "";
        location = "New York";
        hotel = "Grand Hotel";

        boolean result = AccommodationValidator.validateInputs(checkIn, checkOut, location, hotel);

        assertFalse(result);
    }

    @Test
    public void allFieldsEmpty() {
        // all fields are empty
        checkIn = "";
        checkOut = "";
        location = "";
        hotel = "";

        boolean result = AccommodationValidator.validateInputs(checkIn, checkOut, location, hotel);

        assertFalse(result);
    }


}
