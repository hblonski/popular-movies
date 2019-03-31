package com.popularmovies.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.popularmovies.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    String BASE_URL = "https://api.themoviedb.org/3";

    String BASE_URL_IMAGES = "https://image.tmdb.org/t/p/w342";

    @GET("/movie/popular")
    Call<Page> getPopularMovies(@Query("page") Integer page, @Query("api_key") String apiKey);

    @GET("/movie/top_rated")
    Call<Page> getTopRatedMovies(@Query("page") Integer page, @Query("api_key") String apiKey);

    class Page {

        @JsonProperty("page")
        private Integer page;

        @JsonProperty("total_results")
        private Integer totalResults;

        @JsonProperty("total_pages")
        private Integer totalpages;

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

        public Integer getTotalpages() {
            return totalpages;
        }

        public void setTotalpages(Integer totalpages) {
            this.totalpages = totalpages;
        }

        public List<Movie> getResults() {
            return results;
        }

        public void setResults(List<Movie> results) {
            this.results = results;
        }
    }
}
