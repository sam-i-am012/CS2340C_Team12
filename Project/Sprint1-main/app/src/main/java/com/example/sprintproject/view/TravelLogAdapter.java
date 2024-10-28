
package com.example.sprintproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.TravelLog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        holder.daysTextView.setText(String.format("%d days planned", days));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        return (int) ChronoUnit.DAYS.between(start, end);
    }

    public int getTotalDays() {
        int totalDays = 0;
        for (TravelLog log : travelLogs) {
            int days = calculateDays(log.getStartDate(), log.getEndDate());
            totalDays += days;
        }
        return totalDays;
    }
}