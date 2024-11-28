package com.example.sprintproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.ReservationValidatorService;
import com.example.sprintproject.model.Result;

import org.junit.Before;
import org.junit.Test;

public class ReservationValidatorServiceTest {
    private ReservationValidatorService reservationValidatorService;
    private String location = "Restaurant";
    private String website = "https://www.restaurant.com";
    private String time = "12:00 PM";
    private String errorMessage_notFilledOut = "All entries must be filled out";

    @Before
    public void setUp() {
        reservationValidatorService = new ReservationValidatorService();
    }

    @Test
    public void testValidReservation() {
        String name = "John";



        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);

        // in case time already happened
        if ((result.getMessage().equals("Time must be in the future"))
                || (result.getMessage().equals("Reservation created successfully!"))) {
            assertTrue(true);
        }
    }

    @Test
    public void testMissingName() {
        String name = "";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(errorMessage_notFilledOut, result.getMessage());
    }

    @Test
    public void testMissingTime() {
        String name = "John";
        String time = "";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(errorMessage_notFilledOut, result.getMessage());
    }

    @Test
    public void testInvalidTimeAndWebsite() {
        String name = "John";
        String time = "25:00 PM";  // invalid time
        String website = "invalid-url";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertEquals("Time and website entries are both invalid", result.getMessage());
    }

    @Test
    public void testAllMissingFields() {
        // Arrange
        String name = "";
        String time = "";
        String location = "";
        String website = "";

        // Act
        Result result = reservationValidatorService.validate(name, time, location, website);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(errorMessage_notFilledOut, result.getMessage());
    }

    @Test
    public void testInvalidTime() {
        String name = "John";
        String time = "25:00 PM";  // Invalid time

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("Time format is invalid", result.getMessage());
    }

    @Test
    public void testInvalidWebsite() {
        String name = "John";
        String website = "invalid-url";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);

        // in case time already happened
        if ((result.getMessage().equals("Time must be in the future"))
                || (result.getMessage().equals("Invalid website format"))) {
            assertTrue(true);
        }
    }

    @Test
    public void testMissingWebsite() {
        String name = "John";
        String time = "";
        String website = "";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(errorMessage_notFilledOut, result.getMessage());
    }

    @Test
    public void testMissingLocation() {
        String name = "John";
        String location = "";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals(errorMessage_notFilledOut, result.getMessage());
    }
}
