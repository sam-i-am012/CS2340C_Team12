package com.example.sprintproject.model;

public class MissingEntryManager {
    public Result getMissingEntry(String startDate, String endDate, String duration) {
        int missingCount = 0;
        String missingEntry = null;
        if (startDate.isEmpty()) {
            missingCount++;
            missingEntry = startDate;
        }
        if (endDate.isEmpty()) {
            missingCount++;
            missingEntry = endDate;
        }
        if (duration.isEmpty()) {
            missingCount++;
            missingEntry = duration;
        }

        if (missingCount > 1) {
            return new Result(false, "Only one missing entry permitted");
        } else {
            return new Result(true, missingEntry);
        }
    }

    public String calculateEntry(String entry1, String entry2) {
        return "";
    }
}
