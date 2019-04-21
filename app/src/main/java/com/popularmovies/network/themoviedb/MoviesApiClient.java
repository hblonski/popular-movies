package com.popularmovies.network.themoviedb;

import com.popularmovies.model.MoviesResultPage;
import com.popularmovies.model.VideosResultPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Used to send requests to The Movie DB API (https://api.themoviedb.org).
 * See https://square.github.io/retrofit/
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
}
