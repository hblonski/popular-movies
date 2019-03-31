package com.popularmovies.network;

import android.arch.lifecycle.MutableLiveData;
import android.widget.ImageView;

import com.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieController implements Callback<MovieApi.Page> {

    private static final String API_KEY = "";

    private MutableLiveData<List<Movie>> moviesList;

    private MovieApi movieApi;

    public MovieController() {
        Retrofit retrofitInstance = RetrofitUtil.getRetrofitInstance(MovieApi.BASE_URL);
        movieApi = retrofitInstance.create(MovieApi.class);
        moviesList = new MutableLiveData<>();
    }

    public void fetchMovieList(MovieListSortOrder sortOrder, Integer page) {
        Call<MovieApi.Page> call;
        switch (sortOrder) {
            case POPULAR:
                call = movieApi.getPopularMovies(page, API_KEY);
                break;
            case TOP_RATED:
                call = movieApi.getTopRatedMovies(page, API_KEY);
                break;
            default:
                throw new InvalidParameterException("Invalid sort order.");
        }
        call.enqueue(this);
    }

    public void loadMoviePoster(ImageView imageView, String posterPath) {
        Picasso.get().load(buildPorterURL(posterPath)).into(imageView);
    }

    private String buildPorterURL(String posterPath) {
        return MovieApi.BASE_URL_IMAGES.concat(posterPath);
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