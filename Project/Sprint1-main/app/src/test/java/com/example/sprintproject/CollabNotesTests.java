package com.example.sprintproject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.CollabNotesValidator;
import com.example.sprintproject.model.InputValidator;

import org.junit.Test;

// tests for functions needed for collaboration functionality
public class CollabNotesTests {

    @Test
    public void testValidEmail_ValidInputs() {
        // valid email
        assertTrue(CollabNotesValidator.isValidEmail("test@example.com"));
        assertTrue(CollabNotesValidator.isValidEmail("user.name@domain.com"));
        assertTrue(CollabNotesValidator.isValidEmail("user+tag@domain.com"));
    }

    @Test
    public void testInvalidEmail() {
        // invalid email
        assertFalse(CollabNotesValidator.isValidEmail("plainaddress"));
        assertFalse(CollabNotesValidator.isValidEmail("user@domain,com"));
    }

    @Test
    public void testValidEmail_differentDomains() {
        // valid email
        assertTrue(CollabNotesValidator.isValidEmail("plainaddress@gmail.com"));
        assertTrue(CollabNotesValidator.isValidEmail("plainaddress@gmail.edu"));
        assertTrue(CollabNotesValidator.isValidEmail("plainaddress@gmail.org"));
        assertFalse(CollabNotesValidator.isValidEmail("plainaddress@gmail.um"));
    }

    @Test
    public void testValidEmail_WhitespaceInputs() {
        // email with whitespace
        assertFalse(CollabNotesValidator.isValidEmail("   "));
        assertTrue(CollabNotesValidator.isValidEmail("   user@domain.com"));
        assertTrue(CollabNotesValidator.isValidEmail("user@domain.com   "));
    }

}
