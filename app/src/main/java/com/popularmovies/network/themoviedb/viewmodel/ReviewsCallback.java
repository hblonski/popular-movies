package com.popularmovies.network.themoviedb.viewmodel;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;
import com.popularmovies.util.bus.EventBus;
import com.popularmovies.util.bus.MovieEvent;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsCallback implements Callback<ReviewsResultPage> {

    private final Movie movie;

    private final EventBus eventBusInstance = EventBus.getInstance();

    public ReviewsCallback(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onResponse(Call<ReviewsResultPage> call, Response<ReviewsResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            ReviewsResultPage resultPage = response.body();
            movie.setReviews(resultPage.getReviews());
            eventBusInstance.publish(new MovieEvent(movie.getId(), MovieEvent.Type.LOADED_REVIEWS, true));
        } else {
            eventBusInstance.publish(new MovieEvent(movie.getId(), MovieEvent.Type.LOADED_REVIEWS, false));
        }
    }

    @Override
    public void onFailure(Call<ReviewsResultPage> call, Throwable t) {
        eventBusInstance.publish(new MovieEvent(movie.getId(), MovieEvent.Type.LOADED_REVIEWS, false));
    }
}
