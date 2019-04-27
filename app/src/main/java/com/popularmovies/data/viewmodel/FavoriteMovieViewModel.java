package com.popularmovies.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.popularmovies.data.entity.FavoriteMovie;
import com.popularmovies.data.repository.FavoriteMovieRepository;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {

    private FavoriteMovieRepository favoriteMovieRepository;

    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public FavoriteMovieViewModel(@NonNull Application application) {
        super(application);
        favoriteMovieRepository = new FavoriteMovieRepository(application);
    }

    public LiveData<FavoriteMovie> findByMovieId(Integer movieId) {
        return favoriteMovieRepository.findByMovieId(movieId);
    }

    public LiveData<List<FavoriteMovie>> findAll() {
        return favoriteMovieRepository.findAll();
    }

    public void insert(FavoriteMovie favoriteMovie) {
        favoriteMovieRepository.insert(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie) {
        favoriteMovieRepository.delete(favoriteMovie);
    }
}
