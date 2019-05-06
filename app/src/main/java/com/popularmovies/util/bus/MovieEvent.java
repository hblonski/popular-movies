package com.popularmovies.util.bus;

public class MovieEvent {

    public MovieEvent(Integer movieId, Type type, boolean success) {
        this.movieId = movieId;
        this.type = type;
        this.success = success;
    }

    private Integer movieId;

    private Type type;

    private boolean success;

    public Integer getMovieId() {
        return movieId;
    }

    public Type getType() {
        return type;
    }

    public boolean isSuccess() {
        return success;
    }

    public enum Type {
        LOADED_VIDEOS,
        LOADED_REVIEWS,
        LOADED_MOVIE_DETAILS,
        LOADED_MOVIE_LIST
    }
}
