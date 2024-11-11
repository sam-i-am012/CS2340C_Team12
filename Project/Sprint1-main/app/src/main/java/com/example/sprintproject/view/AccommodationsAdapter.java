package com.example.sprintproject.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.model.Accommodation;
import com.example.sprintproject.R;

import java.util.List;

public class AccommodationsAdapter extends RecyclerView.Adapter<AccommodationsAdapter.AccommodationViewHolder> {

    private List<Accommodation> accommodations;

    public AccommodationsAdapter(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addLog(Accommodation log) {
        this.accommodations.add(0, log); // Add new log at the start
        notifyItemInserted(0);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLogs(List<Accommodation> newLogs) {
        this.accommodations.clear();
        this.accommodations.addAll(newLogs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accommodation, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);

        // Set location
        holder.locationText.setText(accommodation.getLocation());

        // Set hotel name
        holder.hotelNameText.setText(accommodation.getHotel());

        // Set check-in and check-out times
        holder.checkInOutText.setText("Check-in: " + accommodation.getCheckInTime() +
                ", Check-out: " + accommodation.getCheckOutTime());

        // Set number of rooms
        holder.numRoomsText.setText("Number of Rooms: " + accommodation.getNumRooms());

        // Set room type label text (if available)
        if (accommodation.getRoomType() != null && !accommodation.getRoomType().isEmpty()) {
            holder.roomTypeLabel.setText(accommodation.getRoomType());
            holder.roomTypeLabel.setVisibility(View.VISIBLE);
        } else {
            holder.roomTypeLabel.setVisibility(View.GONE); // Hide if no room type is provided
        }
    }


    @Override
    public int getItemCount() {
        return accommodations != null ? accommodations.size() : 0;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
        notifyDataSetChanged();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        TextView locationText, hotelNameText, checkInOutText, numRoomsText, roomTypeLabel;

        public AccommodationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.locationText);
            hotelNameText = itemView.findViewById(R.id.hotelNameText);
            checkInOutText = itemView.findViewById(R.id.checkInOutText);
            numRoomsText = itemView.findViewById(R.id.numRoomsText);
            roomTypeLabel = itemView.findViewById(R.id.roomTypeLabel);
        }
    }
}
