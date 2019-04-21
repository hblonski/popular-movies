package com.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MoviesResultPage {

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
