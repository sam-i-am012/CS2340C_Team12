package com.example.sprintproject.model;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservationValidator {
    public static Result noMissingEntries(String name, String time, String location,
                                          String website) {
        if (name.isEmpty() || time.isEmpty() || location.isEmpty() || website.isEmpty()) {
            return new Result(false, "All entries must be filled out");
        }
        return new Result(true, null);
    }

    public static Result isValidTime(String time) {
        boolean valid = isValidTimeFormat(time, "h:mma");
        if (!valid) {
            return new Result(false, "Time format is invalid");
        }
        boolean isFuture = isFutureTime(time, "h:mma");
        if (!isFuture) {
            return new Result(false, "Time must be in the future");
        }
        return new Result(true, null);
    }

    private static boolean isValidTimeFormat(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setLenient(false);
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isFutureTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setLenient(false);

        try {
            Date inputTime = sdf.parse(time);

            Calendar inputCalendar = Calendar.getInstance();
            Calendar now = Calendar.getInstance();

            inputCalendar.setTime(inputTime);
            inputCalendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));

            return inputCalendar.after(now);

        } catch (ParseException e) {
            return false;
        }
    }

    public static Result isValidWebsite(String website) {
        try {
            new URL(website);
            return new Result(true, null);
        } catch (IOException e) {
            return new Result(false, "Invalid website format");
        }
    }

    public static Result isNotADuplicate(String name, String time, String location, String website) {
        FirestoreSingleton.getInstance().diningExists(name, location, time, website)
                .addOnSuccessListener(exists -> {
                    if (exists) {
                        // duplicate exists
                        new Result(false, "Sorry, reservation already exists for these fields");
                    } else {
                        new Result(true, null);
                    }
                })
                .addOnFailureListener(e -> {
                    // handling errors
                    Log.e("Dining", "Error checking for existing reservation: " + e.getMessage());
                });

        return new Result(true, null);
    }
}
