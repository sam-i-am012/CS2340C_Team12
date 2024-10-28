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
    public void testCalculateDays() {
        assertEquals(9, TravelLogValidator.calculateDays("2024-01-01", "2024-01-10"));
        assertEquals(-1, TravelLogValidator.calculateDays("2024-01-01", "Invalid date")); // Test invalid date
    }
}