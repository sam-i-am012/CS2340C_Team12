package com.example.sprintproject;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.sprintproject.model.InputValidator;

/**
 * Tests to ensure that inputs for account and login creation don't have white space, are not null,
 * and are not empty inputs
 */
public class InputValidatorTest {
    @Test
    public void testIsValidEmail_InvalidEmailWithSpaces() {
        assertFalse(InputValidator.isValidEmail("  "));
    }

    @Test
    public void testIsValidPassword_ValidPassword() {
        assertTrue(InputValidator.isValidPassword("securepassword123"));
    }

    @Test
    public void testIsValidPassword_EmptyPassword() {
        assertFalse(InputValidator.isValidPassword(""));
    }

    @Test
    public void testIsValidPassword_NullPassword() {
        assertFalse(InputValidator.isValidPassword(null));
    }
}