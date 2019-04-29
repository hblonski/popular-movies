package com.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.popularmovies.R;
import com.popularmovies.network.youtube.YouTubeController;

import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter {

    //Contains the YouTube video keys
    private List<String> trailerList;

    public TrailerListAdapter(List<String> trailerList) {
        this.trailerList = trailerList;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailImageView;

        ImageView playButtonImageView;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.trailer_thumbnail);
            playButtonImageView = itemView.findViewById(R.id.play_button);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.trailer_card, parent, false);
        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (trailerList != null) {
            String trailerKey = trailerList.get(position);
            YouTubeController.loadMoviePoster(holder.itemView,
                    ((TrailerViewHolder) holder).thumbnailImageView,
                    trailerKey);
        }
    }

    @Override
    public int getItemCount() {
        return trailerList != null ? trailerList.size() : 0;
    }
}
