package com.example.sprintproject;

import com.example.sprintproject.model.VacationTimeCalculator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class VacationTimeCalculatorTest {

    private VacationTimeCalculator calculator;

    @Before
    public void setUp() {
        calculator = new VacationTimeCalculator();
    }

    @Test
    public void testCalculateEntry_BothDates() {
        String entry1 = "2024-01-01";
        String entry2 = "2024-01-07";
        String result = calculator.calculateEntry(entry1, entry2);
        assertEquals("6", result);  // 6 days between Jan 1 and Jan 7
    }

    @Test
    public void testCalculateEntry_EndDateMissing() {
        String entry1 = "2024-01-01";  // Start date
        String entry2 = "5";            // Add 5 days
        String result = calculator.calculateEntry(entry1, entry2);
        assertEquals("2024-01-06", result);  // Should return Jan 6
    }

    @Test
    public void testCalculateEntry_StartDateMissing() {
        String entry1 = "5";            // Subtract 5 days
        String entry2 = "2024-01-07";   // End date
        String result = calculator.calculateEntry(entry1, entry2);
        assertNull(result);
    }
}