package com.popularmovies.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popularmovies.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Used to send requests to The Movie DB API (https://api.themoviedb.org).
 * See https://square.github.io/retrofit/
 * */
public interface MovieApi {

    String BASE_URL = "https://api.themoviedb.org/3/";

    String BASE_URL_IMAGES = "https://image.tmdb.org/t/p/w780";

    @GET("movie/popular")
    Call<Page> getPopularMovies(@Query("page") Integer page, @Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<Page> getTopRatedMovies(@Query("page") Integer page, @Query("api_key") String apiKey);

    class Page {

        //Page size according to themoviedb.org
        //See https://www.themoviedb.org/talk/522eeae419c2955e90252e23?language=en-US
        public static final int SIZE = 20;

        @JsonProperty("page")
        private Integer page;

        @JsonProperty("total_results")
        private Integer totalResults;

        @JsonProperty("total_pages")
        private Integer totalPages;

        @JsonProperty("results")
        private List<Movie> results;

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public List<Movie> getResults() {
            return results;
        }

        public void setResults(List<Movie> results) {
            this.results = results;
        }
    }
}
