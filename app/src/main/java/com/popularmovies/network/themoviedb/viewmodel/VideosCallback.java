package com.popularmovies.network.themoviedb.viewmodel;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.VideosResultPage;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class VideosCallback implements Callback<VideosResultPage> {

    private final List<Movie> movies;

    public VideosCallback(List<Movie> movies) {
        this.movies = movies;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<VideosResultPage> call, Response<VideosResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            VideosResultPage resultPage = response.body();
            Integer movieId = resultPage.getMovieId();
            Movie movie = movies
                    .stream()
                    .filter(m -> m.getId().equals(movieId))
                    .findFirst()
                    .orElse(null);
            movie.setVideos(resultPage.getVideos());
        }
    }

    @Override
    public void onFailure(Call<VideosResultPage> call, Throwable t) {
        //Empty
    }
}
