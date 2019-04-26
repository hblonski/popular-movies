package com.popularmovies.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorite_movie")
public class FavoriteMovie {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "fmv_id")
    private String movieId;

    @ColumnInfo(name = "fmv_title")
    @NonNull
    private String movieTitle;

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    @NonNull
    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(@NonNull String movieTitle) {
        this.movieTitle = movieTitle;
    }
}
