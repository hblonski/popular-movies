package com.popularmovies.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
