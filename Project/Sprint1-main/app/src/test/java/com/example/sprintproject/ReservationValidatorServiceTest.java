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

    @Before
    public void setUp() {
        reservationValidatorService = new ReservationValidatorService();
    }

    @Test
    public void testValidReservation() {
        String name = "John";
        String time = "12:00 PM";
        String location = "Restaurant";
        String website = "https://www.restaurant.com";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("Reservation created successfully!", result.getMessage());
    }

    @Test
    public void testMissingName() {
        String name = "";
        String time = "12:00 PM";
        String location = "Restaurant";
        String website = "https://www.restaurant.com";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("All entries must be filled out", result.getMessage());
    }

    @Test
    public void testMissingTime() {
        String name = "John";
        String time = "";
        String location = "Restaurant";
        String website = "https://www.restaurant.com";

        Result result = reservationValidatorService.validate(name, time, location, website);

        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("All entries must be filled out", result.getMessage());
    }
}
