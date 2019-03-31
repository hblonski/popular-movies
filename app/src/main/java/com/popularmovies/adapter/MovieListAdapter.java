package com.popularmovies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popularmovies.R;
import com.popularmovies.model.Movie;
import com.popularmovies.network.MovieController;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> movieList;

    private MovieController movieController;

    public MovieListAdapter() {
        movieController = new MovieController();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView movieCard = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(movieCard);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String moviePosterPath = movieList.get(position).getPosterPath();
        movieController.loadMoviePoster(holder.imageViewPoster, moviePosterPath);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.image_view_poster);
        }
    }

    public List<Movie> getMovieList() {
        return this.movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
