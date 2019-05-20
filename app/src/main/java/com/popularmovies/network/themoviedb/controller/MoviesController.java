package com.popularmovies.network.themoviedb.controller;

import android.view.View;
import android.widget.ImageView;

import com.popularmovies.BuildConfig;
import com.popularmovies.R;
import com.popularmovies.network.glide.GlideHelper;
import com.popularmovies.network.themoviedb.MovieListSortOrder;
import com.popularmovies.network.themoviedb.MoviesApiClient;
import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.MoviesResultPage;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;
import com.popularmovies.network.themoviedb.model.VideosResultPage;
import com.popularmovies.util.RetrofitServiceGenerator;

import java.security.InvalidParameterException;

import retrofit2.Call;

public class MoviesController {

    private final MoviesApiClient moviesApiClient;

    public MoviesController() {
        moviesApiClient = RetrofitServiceGenerator
                .generateService(MoviesApiClient.BASE_URL, MoviesApiClient.class);
    }

    public Call<MoviesResultPage> buildMovieListCall(MovieListSortOrder sortOrder, Integer page) {
        Call<MoviesResultPage> call;
        switch (sortOrder) {
            case POPULAR:
                call = moviesApiClient.getPopularMovies(page, BuildConfig.THE_MOVIE_DB_KEY);
                break;
            case TOP_RATED:
                call = moviesApiClient.getTopRatedMovies(page, BuildConfig.THE_MOVIE_DB_KEY);
                break;
            default:
                throw new InvalidParameterException("Invalid sort order.");
        }
        return call;
    }

    public Call<VideosResultPage> buildVideoListCall(Movie movie) {
        if (movie == null) {
            throw new InvalidParameterException("Movie should not be null.");
        }

        return moviesApiClient.getMovieVideos(movie.getId(), BuildConfig.THE_MOVIE_DB_KEY);
    }

    public Call<ReviewsResultPage> buildReviewListCall(Movie movie) {
        if (movie == null) {
            throw new InvalidParameterException("Movie should not be null.");
        }

        return moviesApiClient.getMovieReviews(movie.getId(), BuildConfig.THE_MOVIE_DB_KEY);
    }

    public Call<Movie> buildMovieDetailsCall(Integer movieId) {
        return moviesApiClient.getMovieDetails(movieId, BuildConfig.THE_MOVIE_DB_KEY);
    }

    public static void loadMoviePoster(View view, final ImageView imageView, String posterPath) {
        GlideHelper.loadImageIntoImageView(view,
                imageView,
                buildPosterURL(posterPath),
                R.drawable.load_poster_error_image);
    }

    private static String buildPosterURL(String posterPath) {
        return MoviesApiClient.BASE_URL_IMAGES.concat(posterPath);
    }
}
