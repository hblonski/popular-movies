package com.popularmovies.network.themoviedb.viewmodel;

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
        }
    }

    @Override
    public void onFailure(Call<Movie> call, Throwable t) {
        //Empty
    }
}
