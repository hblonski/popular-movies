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

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder> {

    //Contains the YouTube video keys
    private final List<String> trailerList;

    private int selectedTrailer = 0;

    private final RecyclerViewClickListener recyclerViewClickListener;

    public TrailerListAdapter(List<String> trailerList, RecyclerViewClickListener recyclerViewClickListener) {
        this.trailerList = trailerList;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        final ImageView thumbnailImageView;

        final ImageView playButtonImageView;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.trailer_thumbnail);
            playButtonImageView = itemView.findViewById(R.id.play_button);
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
            YouTubeController.loadMoviePoster(holder.itemView,
                    holder.thumbnailImageView,
                    trailerKey);

            //All the thumbnails have the "Play" icon, except the one selected (this one will have
            // the "Pause" icon). When a trailer is selected, the states must change to reflect
            // the new trailer being focused.
            if (selectedTrailer == position) {
                holder.playButtonImageView.setImageResource(R.drawable.ic_pause_circle_24dp);
            } else {
                holder.playButtonImageView.setImageResource(R.drawable.ic_play_circle_24dp);
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
