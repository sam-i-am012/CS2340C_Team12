package com.example.sprintproject.view;

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

    public AccommodationsAdapter() {
        this.accommodations = accommodations;
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
        holder.location.setText(accommodation.getLocation());
        holder.checkInTime.setText("Check-In: " + accommodation.getCheckInTime());
        holder.checkOutTime.setText("Check-Out: " + accommodation.getCheckOutTime());
    }

    @Override
    public int getItemCount() {
        return accommodations != null ? accommodations.size() : 0;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
        notifyDataSetChanged();
    }

    static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        TextView location, checkInTime, checkOutTime;

        AccommodationViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            checkInTime = itemView.findViewById(R.id.checkInTime);
            checkOutTime = itemView.findViewById(R.id.checkOutTime);
        }
    }
}
