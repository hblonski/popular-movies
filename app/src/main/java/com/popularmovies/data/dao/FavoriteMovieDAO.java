package com.popularmovies.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
}
