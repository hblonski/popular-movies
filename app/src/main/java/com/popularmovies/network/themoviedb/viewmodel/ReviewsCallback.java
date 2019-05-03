package com.popularmovies.network.themoviedb.viewmodel;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ReviewsCallback implements Callback<ReviewsResultPage> {

    private final MoviesViewModel moviesViewModel;

    public ReviewsCallback(MoviesViewModel moviesViewModel) {
        this.moviesViewModel = moviesViewModel;
    }

    @Override
    public void onResponse(Call<ReviewsResultPage> call, Response<ReviewsResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            ReviewsResultPage resultPage = response.body();
            Integer movieId = resultPage.getMovieId();
            Movie movie = moviesViewModel.getMoviesList().getValue()
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
