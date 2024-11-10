package com.example.sprintproject.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReservationValidator {
    public static Result noMissingEntries(String name, String time, String location, String website) {
        if (name.isEmpty() || time.isEmpty() || location.isEmpty() || website.isEmpty()) {
            return new Result(false, "All entries must be filled out");
        }
        return new Result(true, null);
    }

    public static Result isValidTime(String time) {
        boolean valid = isValidTimeFormat(time, "h:mm a") ||
                isValidTimeFormat(time, "HH:mm");
        if (!valid) {
            return new Result(false, "Time format is invalid");
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

    public static Result isValidWebsite(String website) {
        try {
            new URL(website);
            return new Result(true, null);
        } catch (IOException e) {
            return new Result(false, "Invalid website format");
        }
    }
}
