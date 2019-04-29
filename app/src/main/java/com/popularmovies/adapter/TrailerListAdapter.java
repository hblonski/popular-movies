package com.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RecyclerViewClickListener;
import com.popularmovies.R;
import com.popularmovies.network.youtube.YouTubeController;

import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter {

    //Contains the YouTube video keys
    private List<String> trailerList;

    private int selectedTrailer = 0;

    private RecyclerViewClickListener recyclerViewClickListener;

    public TrailerListAdapter(List<String> trailerList, RecyclerViewClickListener recyclerViewClickListener) {
        this.trailerList = trailerList;
        this.recyclerViewClickListener = recyclerViewClickListener;
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
            TrailerViewHolder trailerViewHolder = (TrailerViewHolder) holder;
            YouTubeController.loadMoviePoster(holder.itemView,
                    trailerViewHolder.thumbnailImageView,
                    trailerKey);

            //All the thumbnails have the "Play" icon, except the one selected (this one will have
            // the "Pause" icon). When a trailer is selected, the states must change to reflect
            // the new trailer being focused.
            if (selectedTrailer == position) {
                trailerViewHolder.playButtonImageView.setImageResource(R.drawable.ic_pause_circle_24dp);
            } else {
                trailerViewHolder.playButtonImageView.setImageResource(R.drawable.ic_play_circle_24dp);
            }

            holder.itemView.setOnClickListener(v -> {
                notifyItemChanged(selectedTrailer);
                selectedTrailer = position;
                notifyItemChanged(selectedTrailer);
                recyclerViewClickListener.onItemClicked(selectedTrailer);
            });
        }
    }

    @Override
    public int getItemCount() {
        return trailerList != null ? trailerList.size() : 0;
    }
}
