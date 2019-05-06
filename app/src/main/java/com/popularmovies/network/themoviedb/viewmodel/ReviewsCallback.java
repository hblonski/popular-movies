package com.popularmovies.network.themoviedb.viewmodel;

import android.content.Context;
import android.widget.Toast;

import com.popularmovies.R;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ReviewsCallback implements Callback<ReviewsResultPage> {

    private final List<Movie> movies;

    private final Context context;

    public ReviewsCallback(List<Movie> movies, Context context) {
        this.context = context;
        this.movies = movies;
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
        } else {
            handleFailure();
        }
    }

    @Override
    public void onFailure(Call<ReviewsResultPage> call, Throwable t) {
        handleFailure();
    }

    private void handleFailure() {
        Toast.makeText(context,
                R.string.connection_error_the_movie_db,
                Toast.LENGTH_LONG).show();
    }
}
