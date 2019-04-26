package com.popularmovies.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.popularmovies.data.entity.FavoriteMovie;

import java.util.List;

@Dao
public interface FavoriteMovieDAO {

    @Insert
    void insert(FavoriteMovie favoriteMovie);

    @Delete
    void delete(FavoriteMovie favoriteMovie);

    @Query("SELECT * FROM favorite_movie ORDER BY fmv_title")
    LiveData<List<FavoriteMovie>> findAll();

    @Query("SELECT * FROM favorite_movie WHERE fmv_id = :movieId")
    LiveData<FavoriteMovie> findByMovieId(String movieId);
}
