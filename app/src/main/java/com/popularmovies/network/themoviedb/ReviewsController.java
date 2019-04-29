package com.popularmovies.network.themoviedb;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;
import com.popularmovies.util.RetrofitServiceGenerator;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsController implements Callback<ReviewsResultPage> {

    private MoviesApiClient moviesApiClient;

    private List<Movie> movies;

    public ReviewsController(List<Movie> movies) {
        moviesApiClient = RetrofitServiceGenerator
                .generateService(MoviesApiClient.BASE_URL, MoviesApiClient.class);
        this.movies = movies;
    }

    public void fetchMoviesReviews() {
        if (movies == null) {
            throw new InvalidParameterException("Movie list should not be null.");
        }

        movies.forEach(movie -> {
            if (movie.getVideos() == null || movie.getVideos().isEmpty()) {
                Call<ReviewsResultPage> call = moviesApiClient.getMovieReviews(movie.getId(), MoviesApiClient.API_KEY);
                call.enqueue(this);
            }
        });
    }

    @Override
    public void onResponse(Call<ReviewsResultPage> call, Response<ReviewsResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            ReviewsResultPage resultPage = response.body();
            Integer movieId = resultPage.getMovieId();
            Movie movie = movies
                    .stream()
                    .filter(m -> m.getId().equals(movieId))
                    .findFirst()
                    .orElse(null);
            movie.setReviews(resultPage.getReviews());
        }
    }

    @Override
    public void onFailure(Call<ReviewsResultPage> call, Throwable t) {
        //Empty
    }
}
