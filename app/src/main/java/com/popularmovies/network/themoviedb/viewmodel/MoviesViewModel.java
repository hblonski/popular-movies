package com.popularmovies.network.themoviedb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.popularmovies.network.themoviedb.MovieListSortOrder;
import com.popularmovies.network.themoviedb.controller.MoviesController;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.MoviesResultPage;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;
import com.popularmovies.network.themoviedb.model.VideosResultPage;

import java.util.List;

import retrofit2.Call;

public class MoviesViewModel extends AndroidViewModel {

    private static final int PAGE_ZERO = 0;

    private final MutableLiveData<List<Movie>> moviesList;

    private final MoviesController moviesController;

    private Integer currentPage;

    private MovieListSortOrder currentSortOrder;

    private boolean loading = false;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        moviesList = new MutableLiveData<>();
        currentPage = PAGE_ZERO;
        currentSortOrder = null;
        moviesController = new MoviesController();
    }

    public void fetchNextMoviesListPage() {
        Call<MoviesResultPage> call = moviesController.buildMovieListCall(currentSortOrder, currentPage + 1);
        loading = true;
        call.enqueue(new MoviesCallback(this));
    }

    public void fetchMovieVideos() {
        List<Call<VideosResultPage>> callList = moviesController.buildVideoListCalls(moviesList.getValue());
        callList.forEach(c -> c.enqueue(new VideosCallback(this)));
    }

    public void fetchReviews() {
        List<Call<ReviewsResultPage>> callList = moviesController.buildReviewListCalls(moviesList.getValue());
        callList.forEach(c -> c.enqueue(new ReviewsCallback(this)));
    }

    public MutableLiveData<List<Movie>> getMoviesList() {
        return moviesList;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setCurrentSortOrder(MovieListSortOrder newSortOrder) {
        if (currentSortOrder != newSortOrder) {
            currentPage = PAGE_ZERO;
        }
        currentSortOrder = newSortOrder;
    }

    void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    void setLoading(boolean loading) {
        this.loading = loading;
    }

    void setMoviesList(List<Movie> moviesList) {
        this.moviesList.setValue(moviesList);
    }
}
