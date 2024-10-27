package com.example.sprintproject.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Import TextView

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
        // Inflate the item layout instead of the activity layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_destinations, parent, false); // Change this line
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
    }//

    private int calculateDays(String startDate, String endDate) {
        // Implement your date calculation logic here
        return 0; // Replace with actual implementation
    }
}