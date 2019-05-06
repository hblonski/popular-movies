package com.popularmovies.network.themoviedb;

import com.popularmovies.network.themoviedb.model.Movie;
import com.popularmovies.network.themoviedb.model.MoviesResultPage;
import com.popularmovies.network.themoviedb.model.ReviewsResultPage;
import com.popularmovies.network.themoviedb.model.VideosResultPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Used to send requests to The Movie DB API.
 * @see <a href="https://square.github.io/retrofit/">https://square.github.io/retrofit/</a>
 * @see <a href="https://api.themoviedb.org">https://api.themoviedb.org</a>
 * */
public interface MoviesApiClient {

    //Replace the value with your MovieDB api key
    String API_KEY = "";

    String BASE_URL = "https://api.themoviedb.org/3/";

    String BASE_URL_IMAGES = "https://image.tmdb.org/t/p/w780";

    @GET("movie/popular")
    Call<MoviesResultPage> getPopularMovies(@Query("page") Integer page, @Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResultPage> getTopRatedMovies(@Query("page") Integer page, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideosResultPage> getMovieVideos(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResultPage> getMovieReviews(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") Integer movieId, @Query("api_key") String apiKey);
}
