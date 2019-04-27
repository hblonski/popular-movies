package com.popularmovies.data.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@SuppressWarnings({"unused", "NullableProblems"})
@Entity(tableName = "favorite_movie")
public class FavoriteMovie {

    public FavoriteMovie() {
        //Empty constructor
    }

    @Ignore
    public FavoriteMovie(@NonNull Integer movieId, @NonNull String movieTitle) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fmv_id")
    private Integer movieId;

    @ColumnInfo(name = "fmv_title")
    @NonNull
    private String movieTitle;

    @NonNull
    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull Integer movieId) {
        this.movieId = movieId;
    }

    @NonNull
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(@NonNull String movieTitle) {
        this.movieTitle = movieTitle;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof FavoriteMovie){
            return ((FavoriteMovie) obj).getMovieId().equals(movieId);
        }
        return false;
    }
}
