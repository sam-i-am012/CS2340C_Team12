package com.example.sprintproject.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.model.ReservationValidator;
import com.example.sprintproject.viewmodel.DiningViewModel;

import java.util.ArrayList;
import java.util.List;

public class DiningsAdapter extends RecyclerView.Adapter<DiningsAdapter.DiningViewHolder> {
    private static final int VIEW_TYPE_ACTIVE = 0;
    private static final int VIEW_TYPE_EXPIRED = 1;
    private List<Dining> dinings;

    public DiningsAdapter() {
        this.dinings = new ArrayList<>(); // Initialize with an empty list if null
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addLog(Dining reservation) {
        this.dinings.add(0, reservation); // Add new log at the start
        updateExpiredDinings();
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLogs(List<Dining> newReservations) {
        this.dinings.clear();
        this.dinings.addAll(newReservations);
        updateExpiredDinings(); // Refresh expired statuses for all items
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_EXPIRED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expired_reservation, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        }
        return new DiningViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return dinings.get(position).isExpired() ? VIEW_TYPE_EXPIRED : VIEW_TYPE_ACTIVE;
    }

    @Override
    public void onBindViewHolder(@NonNull DiningViewHolder holder, int position) {
        Dining dining = dinings.get(position);
        holder.location.setText(dining.getLocation());
        holder.resturantName.setText(dining.getName());
        holder.website.setText(dining.getWebsite());
        holder.time.setText(dining.getTime());
    }

    @Override
    public int getItemCount() {
        return dinings != null ? dinings.size() : 0;
    }

    private void updateExpiredDinings() {
        for (Dining dining : dinings) {
            if(!ReservationValidator.isFutureTime(dining.getTime(), "h:mma")) {
                dining.setExpired(true);
            }
        }
    }

    public void setDinings(List<Dining> dinings) {
        if (dinings != null) {
            this.dinings = dinings;
            updateExpiredDinings();
        } else {
            this.dinings = new ArrayList<>(); // Avoid null pointer
        }
        notifyDataSetChanged();
    }

    static class DiningViewHolder extends RecyclerView.ViewHolder {
        TextView location, resturantName, website, time;

        DiningViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.tvLocation);
            resturantName = itemView.findViewById(R.id.tvRestaurantName);
            website = itemView.findViewById(R.id.tvWebsite);
            time = itemView.findViewById(R.id.tvTime);
        }
    }
}
