package com.example.sprintproject.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Post;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        TextView tvDestination = findViewById(R.id.tvDestination);
        TextView tvStartDate = findViewById(R.id.tvStartDate);
        TextView tvEndDate = findViewById(R.id.tvEndDate);
        TextView tvAccommodations = findViewById(R.id.tvAccommodations);
        TextView tvDiningReservations = findViewById(R.id.tvDiningReservations);
        TextView tvNotes = findViewById(R.id.tvNotes);

        Post post = (Post) getIntent().getSerializableExtra("POST_DATA");

        tvDestination.setText(post.getPostDestination());
        tvStartDate.setText(post.getPostStartDate());
        tvEndDate.setText(post.getPostEndDate());
        tvAccommodations.setText(post.getPostAccommodations());
        tvDiningReservations.setText(post.getDiningReservations());
        tvNotes.setText(post.getPostNotes());
    }
}
