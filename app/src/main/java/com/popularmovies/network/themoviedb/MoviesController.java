package com.popularmovies.network.themoviedb;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.popularmovies.R;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.MoviesResultPage;
import com.popularmovies.network.glide.GlideHelper;
import com.popularmovies.util.RetrofitServiceGenerator;

import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.popularmovies.network.themoviedb.MoviesApiClient.API_KEY;

public class MoviesController extends AndroidViewModel implements Callback<MoviesResultPage> {

    private static final int FIRST_PAGE = 1;

    private MutableLiveData<List<Movie>> moviesList;

    private MoviesApiClient moviesApiClient;

    private Integer currentPage;

    private MovieListSortOrder currentSortOrder;

    private boolean loading = false;

    public MoviesController(@NonNull Application application) {
        super(application);
        moviesApiClient = RetrofitServiceGenerator
                .generateService(MoviesApiClient.BASE_URL, MoviesApiClient.class);
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
        Call<MoviesResultPage> call;
        switch (sortOrder) {
            case POPULAR:
                call = moviesApiClient.getPopularMovies(nextPage, API_KEY);
                break;
            case TOP_RATED:
                call = moviesApiClient.getTopRatedMovies(nextPage, API_KEY);
                break;
            default:
                throw new InvalidParameterException("Invalid sort order.");
        }
        loading = true;
        call.enqueue(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onResponse(Call<MoviesResultPage> call, Response<MoviesResultPage> response) {
        if (response.code() == HttpURLConnection.HTTP_OK) {
            MoviesResultPage moviesResultPage = response.body();
            if (!moviesResultPage.getPage().equals(FIRST_PAGE)) {
                currentPage = moviesResultPage.getPage();
                List<Movie> currentList = moviesList.getValue();
                currentList.addAll(moviesResultPage.getResults());
                moviesList.setValue(currentList);
            } else {
                currentPage = FIRST_PAGE;
                moviesList.setValue(moviesResultPage.getResults());
            }
            new VideosController(moviesList.getValue()).fetchMoviesVideos();
            new ReviewsController(moviesList.getValue()).fetchMoviesReviews();
        }
        loading = false;
    }

    @Override
    public void onFailure(Call<MoviesResultPage> call, Throwable t) {
        moviesList.setValue(Collections.emptyList());
        loading = false;
    }

    public MutableLiveData<List<Movie>> getMoviesList() {
        return moviesList;
    }

    public static void loadMoviePoster(View view, final ImageView imageView, String posterPath) {
        GlideHelper.loadImageIntoImageView(view,
                imageView,
                buildPosterURL(posterPath),
                R.drawable.load_poster_error_image);
    }

    public boolean isLoading() {
        return loading;
    }

    public MovieListSortOrder getCurrentSortOrder() {
        return this.currentSortOrder;
    }

    private static String buildPosterURL(String posterPath) {
        return MoviesApiClient.BASE_URL_IMAGES.concat(posterPath);
    }
}
