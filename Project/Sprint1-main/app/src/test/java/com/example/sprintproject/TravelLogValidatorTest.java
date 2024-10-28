package com.example.sprintproject;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.sprintproject.model.TravelLogValidator;

public class TravelLogValidatorTest {

    @Test
    public void testIsDateFormatInvalid() {
        assertTrue(TravelLogValidator.isDateFormatInvalid("2024-01-32")); // Invalid day
        assertTrue(TravelLogValidator.isDateFormatInvalid("2024-13-01")); // Invalid month
        assertFalse(TravelLogValidator.isDateFormatInvalid("2024-01-01")); // Valid date
    }

    @Test
    public void testCalculateDaysValid() {
        assertEquals(9, TravelLogValidator.calculateDays("2024-01-01", "2024-01-10"));
    }

    @Test
    public void testCalculateDaysInvalidStart() {
        assertEquals(-1, TravelLogValidator.calculateDays("Invalid start", "2024-01-10"));
    }

    @Test
    public void testCalculateDaysInvalidEnd() {
        assertEquals(-1, TravelLogValidator.calculateDays("2024-01-01", "Invalid date")); // Test invalid date
    }
}