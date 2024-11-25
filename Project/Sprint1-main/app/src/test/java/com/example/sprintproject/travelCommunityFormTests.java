package com.example.sprintproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.FormValidator;

import org.junit.Test;

import java.time.LocalDate;

public class travelCommunityFormTests {
    @Test
    public void testValidTravelLog_ValidCase() {
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);
        String destination = "Tokyo";

        assertTrue(FormValidator.isValidTravelLog(destination, startDate, endDate));
    }

    @Test
    public void testValidTravelLog_InvalidCases() {
        // Invalid destination
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);
        assertFalse(FormValidator.isValidTravelLog("", startDate, endDate));

        // Invalid date range
        LocalDate invalidEndDate = LocalDate.of(2024, 10, 25);
        assertFalse(FormValidator.isValidTravelLog("Paris", startDate, invalidEndDate));

        // Both invalid
        assertFalse(FormValidator.isValidTravelLog("", startDate, invalidEndDate));
    }

    @Test
    public void testValidDateRange_SameStartAndEndDate() {
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 1);
        assertFalse(FormValidator.isValidDateRange(startDate, endDate));
    }

    @Test
    public void testValidDestination_WithLeadingAndTrailingSpaces() {
        String destination = "   London  ";
        assertTrue(FormValidator.isValidDestination(destination));
    }
    
    @Test
    public void testValidDateRange_ValidCases() {
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);
        assertTrue(FormValidator.isValidDateRange(startDate, endDate));
    }

    @Test
    public void testValidDateRange_InvalidCases() {
        LocalDate startDate = LocalDate.of(2024, 11, 10);
        LocalDate endDate = LocalDate.of(2024, 11, 1);
        assertFalse(FormValidator.isValidDateRange(startDate, endDate));

        assertFalse(FormValidator.isValidDateRange(null, LocalDate.of(2024, 11, 10)));
        assertFalse(FormValidator.isValidDateRange(LocalDate.of(2024, 11, 10), null));
    }

    @Test
    public void testValidDestination_ValidCases() {
        assertTrue(FormValidator.isValidDestination("Paris"));
        assertTrue(FormValidator.isValidDestination(" New York "));
    }

    @Test
    public void testValidDestination_InvalidCases() {
        assertFalse(FormValidator.isValidDestination(null));
        assertFalse(FormValidator.isValidDestination(""));
        assertFalse(FormValidator.isValidDestination("   "));
    }
}
