package com.example.sprintproject;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.sprintproject.model.Accommodation;
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

    @Test
    public void allFieldsFilled() {
        //  all fields are filled
        checkIn = "2024-12-01";
        checkOut = "2024-12-07";
        location = "New York";
        hotel = "Grand Hotel";

        boolean result = AccommodationValidator.validateInputs(checkIn, checkOut, location, hotel);

        assertTrue(result);
    }

    @Test
    public void emptyCheckIn() {
        // check in is empty
        checkIn = "";
        checkOut = "2024-12-07";
        location = "New York";
        hotel = "Grand Hotel";

        boolean result = AccommodationValidator.validateInputs(checkIn, checkOut, location, hotel);

        assertFalse(result);
    }

//    @Test
//    public void testDefaultConstructor() {
//        Accommodation accommodation = new Accommodation();
//        assertNotNull(accommodation);
//    }

    @Test
    public void testParameterizedConstructor() {
        Accommodation accommodation = new Accommodation(
                "Hotel Paradise",
                "New York",
                "2024-11-27 14:00",
                "2024-11-30 11:00",
                2,
                "Deluxe",
                "user123"
        );

        assertEquals("Hotel Paradise", accommodation.getHotel());
        assertEquals("New York", accommodation.getLocation());
        assertEquals("2024-11-27 14:00", accommodation.getCheckInTime());
        assertEquals("2024-11-30 11:00", accommodation.getCheckOutTime());
        assertEquals(2, accommodation.getNumRooms());
        assertEquals("Deluxe", accommodation.getRoomType());
        assertEquals("user123", accommodation.getUserId());
    }

    @Test
    public void testGettersAndSetters() {
        Accommodation accommodation = new Accommodation();

        // Test hotel
        accommodation.setHotel("Hotel Paradise");
        assertEquals("Hotel Paradise", accommodation.getHotel());

        // Test location
        accommodation.setLocation("New York");
        assertEquals("New York", accommodation.getLocation());

        // Test check-in time
        accommodation.setCheckInTime("2024-11-27 14:00");
        assertEquals("2024-11-27 14:00", accommodation.getCheckInTime());

        // Test check-out time
        accommodation.setCheckOutTime("2024-11-30 11:00");
        assertEquals("2024-11-30 11:00", accommodation.getCheckOutTime());

        // Test number of rooms
        accommodation.setNumRooms(2);
        assertEquals(2, accommodation.getNumRooms());

        // Test room type
        accommodation.setRoomType("Deluxe");
        assertEquals("Deluxe", accommodation.getRoomType());

        // Test user ID
        accommodation.setUserId("user123");
        assertEquals("user123", accommodation.getUserId());

        // Test travel destination
        accommodation.setTravelDestination("California");
        assertEquals("California", accommodation.getTravelDestination());
    }
}
