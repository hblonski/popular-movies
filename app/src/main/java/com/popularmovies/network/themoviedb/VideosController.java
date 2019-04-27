package com.popularmovies.network.themoviedb;

import com.popularmovies.model.Movie;
import com.popularmovies.model.VideosResultPage;
import com.popularmovies.util.RetrofitServiceGenerator;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosController implements Callback<VideosResultPage> {

    private MoviesApiClient moviesApiClient;

    private List<Movie> movies;

    public VideosController(List<Movie> movies) {
        moviesApiClient = RetrofitServiceGenerator
                .generateService(MoviesApiClient.BASE_URL, MoviesApiClient.class);
        this.movies = movies;
    }

    public void fetchMoviesVideos() {
        if (movies == null) {
            throw new InvalidParameterException("Movie list should not be null.");
        }

        movies.forEach(movie -> {
            if (movie.getVideos() == null || movie.getVideos().isEmpty()) {
                Call<VideosResultPage> call = moviesApiClient.getMovieVideos(movie.getId(), MoviesApiClient.API_KEY);
                call.enqueue(this);
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<VideosResultPage> call, Response<VideosResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            VideosResultPage resultPage = response.body();
            Integer movieId = resultPage.getId();
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
