package com.example.sprintproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelLog;

import java.util.List;

public class TravelLogAdapter extends RecyclerView.Adapter<TravelLogAdapter.ViewHolder> {
    private List<TravelLog> travelLogs;

    public TravelLogAdapter(List<TravelLog> travelLogs) {
        this.travelLogs = travelLogs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the TravelLog object for this position
        TravelLog log = travelLogs.get(position);
        holder.destinationTextView.setText(log.getDestination());

        // Calculate and display days
        int days = calculateDays(log.getStartDate(), log.getEndDate());
        holder.daysTextView.setText(String.format("%d days", days));
    }

    @Override
    public int getItemCount() {
        return travelLogs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTextView;
        TextView daysTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // Bind the views from the item layout
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            daysTextView = itemView.findViewById(R.id.daysTextView);
        }
    }

    private int calculateDays(String startDate, String endDate) {
        String[] startParts = startDate.split("-");
        String[] endParts = endDate.split("-");

        int startYear = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]);
        int startDay = Integer.parseInt(startParts[2]);

        int endYear = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]);
        int endDay = Integer.parseInt(endParts[2]);

        // Calculate days from start date to the end of that year
        int daysInStartYear = daysInYear(startYear, startMonth, startDay);
        // Calculate days from the start of the end year to the end date
        int daysInEndYear = daysFromStartOfYear(endYear, endMonth, endDay);

        // Calculate total days for complete years in between
        int totalDays = daysInStartYear + daysInEndYear;

        for (int year = startYear + 1; year < endYear; year++) {
            totalDays += isLeapYear(year) ? 366 : 365;
        }

        return totalDays;
    }

    private int daysInYear(int year, int month, int day) {
        int days = 0;
        for (int m = month; m <= 12; m++) {
            if (m == month) {
                days += (isLeapYear(year) && m == 2) ? (29 - day) : (daysInMonth(m) - day);
            } else {
                days += daysInMonth(m);
            }
        }
        return days;
    }

    private int daysFromStartOfYear(int year, int month, int day) {
        int days = 0;
        for (int m = 1; m < month; m++) {
            days += daysInMonth(m);
        }
        days += day;
        return days;
    }

    private int daysInMonth(int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return 28; // Will handle leap year in the leap year check
            default:
                return 0; // Invalid month
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}