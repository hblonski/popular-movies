package com.popularmovies.network;

import android.arch.lifecycle.MutableLiveData;

import com.popularmovies.model.Movie;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieListController implements Callback<MovieApi.Page> {

    private MutableLiveData<List<Movie>> moviesList;

    private MovieApi movieApi;

    public MovieListController() {
        Retrofit retrofitInstance = RetrofitUtil.getRetrofitInstance(MovieApi.BASE_URL);
        movieApi = retrofitInstance.create(MovieApi.class);
        moviesList = new MutableLiveData<>();
    }

    public void fetchMovieList(MovieListSortOrder sortOrder, Integer page, String apiKey) {
        Call<MovieApi.Page> call;
        switch (sortOrder) {
            case POPULAR:
                call = movieApi.getPopularMovies(page, apiKey);
                break;
            case TOP_RATED:
                call = movieApi.getTopRatedMovies(page, apiKey);
                break;
            default:
                throw new InvalidParameterException("Invalid sort order.");
        }
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<MovieApi.Page> call, Response<MovieApi.Page> response) {
        MovieApi.Page page = response.body();
        moviesList.setValue(page.getResults());
    }

    @Override
    public void onFailure(Call<MovieApi.Page> call, Throwable t) {
        moviesList.setValue(Collections.<Movie>emptyList());
    }

    public MutableLiveData<List<Movie>> getMoviesList() {
        return moviesList;
    }
}
