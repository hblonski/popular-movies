package com.popularmovies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.popularmovies.R;
import com.popularmovies.network.youtube.YouTubeController;

import java.util.List;

import static com.popularmovies.network.youtube.YouTubeController.BASE_URL;
import static com.popularmovies.network.youtube.YouTubeController.YOUTUBE_APP_URI;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    //Contains the YouTube video keys
    private final List<String> trailerList;

    public TrailerListAdapter(List<String> trailerList) {
        this.trailerList = trailerList;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        final ImageView thumbnailImageView;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.trailer_thumbnail);
        }
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_trailer, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        if (trailerList != null) {
            String trailerKey = trailerList.get(position);
            YouTubeController.loadVideoThumbnail(holder.itemView,
                    holder.thumbnailImageView,
                    trailerKey);

            holder.itemView.setOnClickListener(v -> {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_URI + trailerKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + trailerKey));
                //If the YouTube app is not installed, it will fail and open on the browser
                try {
                    holder.itemView.getContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    holder.itemView.getContext().startActivity(webIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return trailerList != null ? trailerList.size() : 0;
    }
}
