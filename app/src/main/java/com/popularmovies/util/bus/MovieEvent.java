package com.popularmovies.util.bus;

/**
 * Represents an event related to an operation involving a movie.
 * Since http calls are made asynchronously, the EventBus instance is used to share information
 * between callbacks and classes that use them, through MovieEvents.
 * See {@link EventBus}.
 * */
public class MovieEvent {

    public MovieEvent(Integer movieId, Type type, boolean success) {
        this.movieId = movieId;
        this.type = type;
        this.success = success;
    }

    /**
     * The id of the movie related to the event.
     * */
    private Integer movieId;

    /**
     * The event {@link Type}.
     * */
    private Type type;

    /**
     * Indicates if the operation related to the event was successful.
     * */
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
