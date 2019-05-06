package com.popularmovies.network.themoviedb.controller;

import android.view.View;
import android.widget.ImageView;

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
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;

import static com.popularmovies.network.themoviedb.MoviesApiClient.API_KEY;

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
                call = moviesApiClient.getPopularMovies(page, API_KEY);
                break;
            case TOP_RATED:
                call = moviesApiClient.getTopRatedMovies(page, API_KEY);
                break;
            default:
                throw new InvalidParameterException("Invalid sort order.");
        }
        return call;
    }

    public List<Call<VideosResultPage>> buildVideoListCalls(List<Movie> movies) {
        if (movies == null) {
            throw new InvalidParameterException("Movie list should not be null.");
        }

        return movies.stream()
                .map(m -> moviesApiClient.getMovieVideos(m.getId(), API_KEY))
                .collect(Collectors.toList());
    }

    public Call<VideosResultPage> buildVideoListCall(Movie movie) {
        return moviesApiClient.getMovieVideos(movie.getId(), API_KEY);
    }

    public List<Call<ReviewsResultPage>> buildReviewListCalls(List<Movie> movies) {
        if (movies == null) {
            throw new InvalidParameterException("Movie list should not be null.");
        }

        return movies.stream()
                .map(m -> moviesApiClient.getMovieReviews(m.getId(), API_KEY))
                .collect(Collectors.toList());
    }

    public Call<Movie> buildMovieDetailsCall(Integer movieId) {
        return moviesApiClient.getMovieDetails(movieId, API_KEY);
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
