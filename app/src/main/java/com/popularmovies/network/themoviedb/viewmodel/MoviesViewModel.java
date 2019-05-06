package com.popularmovies.network.themoviedb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.popularmovies.data.entity.FavoriteMovie;
import com.popularmovies.network.themoviedb.MovieListSortOrder;
import com.popularmovies.network.themoviedb.controller.MoviesController;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.MoviesResultPage;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;
import com.popularmovies.network.themoviedb.model.VideosResultPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void fetchNextMoviesPage() {
        Call<MoviesResultPage> call = moviesController.buildMovieListCall(currentSortOrder, currentPage + 1);
        loading = true;
        call.enqueue(new MoviesCallback(this));
    }

    public void fetchVideos(Movie movie) {
        Call<VideosResultPage> call = moviesController.buildVideoListCall(movie);
        call.enqueue(new VideosCallback(movie));
    }

    public void fetchReviews(Movie movie) {
        Call<ReviewsResultPage> call = moviesController.buildReviewListCall(movie);
        call.enqueue(new ReviewsCallback(movie));
    }

    public void fetchMovieListDetails(List<FavoriteMovie> favoriteMovies) {
        moviesList.setValue(new ArrayList<>());
        if (favoriteMovies != null && !favoriteMovies.isEmpty()) {
            List<Call<Movie>> callList = favoriteMovies
                    .stream()
                    .map(m -> moviesController.buildMovieDetailsCall(m.getMovieId()))
                    .collect(Collectors.toList());
            callList.forEach(c -> c.enqueue(new MovieCallback(this)));
        }
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

    //Adds a single movie to the list. This method is necessary since adding a single item to
    //the LiveData value (when it is a collection) doesn't notify the observers
    void addMovieToList(Movie movie) {
        //Synchronized so that no value is lost when the asynchronous calls add items
        synchronized (this) {
            moviesList.getValue().add(movie);
            //Sets the value as itself, just to notify the observers
            moviesList.setValue(moviesList.getValue());
        }
    }
}
