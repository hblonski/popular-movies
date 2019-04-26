package com.popularmovies.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

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

    public LiveData<List<FavoriteMovie>> findAll() {
        return favoriteMovies;
    }

    public void insert(FavoriteMovie favoriteMovie) {
        favoriteMovieRepository.insert(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie) {
        favoriteMovieRepository.delete(favoriteMovie);
    }
}
