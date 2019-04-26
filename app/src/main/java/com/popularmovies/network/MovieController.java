package com.popularmovies.network;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.popularmovies.R;
import com.popularmovies.model.Movie;
import com.popularmovies.util.RetrofitUtil;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieController extends AndroidViewModel implements Callback<MovieApi.Page> {

    //Replace the value with your MovieDB api key
    private static final String API_KEY = "";

    private static final int FIRST_PAGE = 1;

    private MutableLiveData<List<Movie>> moviesList;

    private MovieApi movieApi;

    private Integer currentPage;

    private MovieListSortOrder currentSortOrder;

    private boolean loading = false;

    public MovieController(@NonNull Application application) {
        super(application);
        Retrofit retrofitInstance = RetrofitUtil.getRetrofitInstance(MovieApi.BASE_URL);
        movieApi = retrofitInstance.create(MovieApi.class);
        moviesList = new MutableLiveData<>();
        currentPage = null;
        currentSortOrder = null;
    }

    public void fetchNextMoviesListPage(MovieListSortOrder sortOrder) {
        Integer nextPage;
        if (currentSortOrder != sortOrder) {
            nextPage = FIRST_PAGE;
            currentSortOrder = sortOrder;
        } else {
            nextPage = currentPage + 1;
        }
        Call<MovieApi.Page> call;
        switch (sortOrder) {
            case POPULAR:
                call = movieApi.getPopularMovies(nextPage, API_KEY);
                break;
            case TOP_RATED:
                call = movieApi.getTopRatedMovies(nextPage, API_KEY);
                break;
            default:
                throw new InvalidParameterException("Invalid sort order.");
        }
        loading = true;
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<MovieApi.Page> call, Response<MovieApi.Page> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            MovieApi.Page page = response.body();
            if (!page.getPage().equals(FIRST_PAGE)) {
                currentPage = page.getPage();
                List<Movie> currentList = moviesList.getValue();
                currentList.addAll(page.getResults());
                moviesList.setValue(currentList);
            } else {
                currentPage = FIRST_PAGE;
                moviesList.setValue(page.getResults());
            }
        }
        loading = false;
    }

    @Override
    public void onFailure(Call<MovieApi.Page> call, Throwable t) {
        moviesList.setValue(Collections.<Movie>emptyList());
        loading = false;
    }

    public MutableLiveData<List<Movie>> getMoviesList() {
        return moviesList;
    }

    public static void loadMoviePoster(View view, final ImageView imageView, String posterPath) {
        Glide.with(view)
                .load(buildPosterURL(posterPath))
                .error(R.drawable.load_poster_error_image)
                .into(imageView);
    }

    public boolean isLoading() {
        return loading;
    }

    public MovieListSortOrder getCurrentSortOrder() {
        return this.currentSortOrder;
    }

    private static String buildPosterURL(String posterPath) {
        return MovieApi.BASE_URL_IMAGES.concat(posterPath);
    }
}
