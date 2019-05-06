package com.popularmovies.network.themoviedb.viewmodel;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.util.bus.EventBus;
import com.popularmovies.util.bus.MovieEvent;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCallback implements Callback<Movie> {

    private final EventBus eventBusInstance = EventBus.getInstance();

    private final MoviesViewModel moviesViewModel;

    public MovieCallback(MoviesViewModel moviesViewModel) {
        this.moviesViewModel = moviesViewModel;
    }

    @Override
    public void onResponse(Call<Movie> call, Response<Movie> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            moviesViewModel.addMovieToList(response.body());
            eventBusInstance.publish(new MovieEvent(null, MovieEvent.Type.LOADED_MOVIE_DETAILS, true));
        } else {
            eventBusInstance.publish(new MovieEvent(null, MovieEvent.Type.LOADED_MOVIE_DETAILS, false));
        }
    }

    @Override
    public void onFailure(Call<Movie> call, Throwable t) {
        eventBusInstance.publish(new MovieEvent(null, MovieEvent.Type.LOADED_MOVIE_DETAILS, false));
    }
}
