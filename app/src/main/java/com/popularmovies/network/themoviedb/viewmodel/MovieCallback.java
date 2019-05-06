package com.popularmovies.network.themoviedb.viewmodel;

import android.widget.Toast;

import com.popularmovies.R;
import com.popularmovies.network.themoviedb.model.Movie;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCallback implements Callback<Movie> {

    private final MoviesViewModel moviesViewModel;

    public MovieCallback(MoviesViewModel moviesViewModel) {
        this.moviesViewModel = moviesViewModel;
    }

    @Override
    public void onResponse(Call<Movie> call, Response<Movie> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            moviesViewModel.addMovieToList(response.body());
        } else {
            handleFailure();
        }
    }

    @Override
    public void onFailure(Call<Movie> call, Throwable t) {
        handleFailure();
    }

    private void handleFailure() {
        Toast.makeText(moviesViewModel.getApplication(),
                R.string.connection_error_the_movie_db,
                Toast.LENGTH_SHORT).show();
    }
}
