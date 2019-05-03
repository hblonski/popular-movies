package com.popularmovies.network.themoviedb.viewmodel;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.MoviesResultPage;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MoviesCallback implements Callback<MoviesResultPage> {

    private static final int FIRST_PAGE = 1;

    private final MoviesViewModel moviesViewModel;

    public MoviesCallback(MoviesViewModel moviesViewModel) {
        this.moviesViewModel = moviesViewModel;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<MoviesResultPage> call, Response<MoviesResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            MoviesResultPage moviesResultPage = response.body();
            if (!moviesResultPage.getPage().equals(FIRST_PAGE)) {
                moviesViewModel.setCurrentPage(moviesResultPage.getPage());
                List<Movie> currentList = moviesViewModel.getMoviesList().getValue();
                currentList.addAll(moviesResultPage.getResults());
                moviesViewModel.setMoviesList(currentList);
            } else {
                moviesViewModel.setCurrentPage(FIRST_PAGE);
                moviesViewModel.setMoviesList(moviesResultPage.getResults());
            }
            moviesViewModel.fetchVideos();
            moviesViewModel.fetchReviews();
        }
        moviesViewModel.setLoading(false);
    }

    @Override
    public void onFailure(Call<MoviesResultPage> call, Throwable t) {
        moviesViewModel.setMoviesList(Collections.emptyList());
        moviesViewModel.setLoading(false);
    }
}
