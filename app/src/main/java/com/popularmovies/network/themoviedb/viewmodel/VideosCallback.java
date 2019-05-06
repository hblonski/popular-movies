package com.popularmovies.network.themoviedb.viewmodel;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.VideosResultPage;
import com.popularmovies.util.bus.EventBus;
import com.popularmovies.util.bus.MovieEvent;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class VideosCallback implements Callback<VideosResultPage> {

    private final Movie movie;

    private final EventBus eventBusInstance = EventBus.getInstance();

    public VideosCallback(Movie movie) {
        this.movie = movie;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<VideosResultPage> call, Response<VideosResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            VideosResultPage resultPage = response.body();
            movie.setVideos(resultPage.getVideos());
            eventBusInstance.publish(new MovieEvent(movie.getId(), MovieEvent.Type.LOADED_VIDEOS, true));
        } else {
            eventBusInstance.publish(new MovieEvent(movie.getId(), MovieEvent.Type.LOADED_VIDEOS, false));
        }
    }

    @Override
    public void onFailure(Call<VideosResultPage> call, Throwable t) {
        eventBusInstance.publish(new MovieEvent(movie.getId(), MovieEvent.Type.LOADED_VIDEOS, false));
    }
}
