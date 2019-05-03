package com.popularmovies.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.popularmovies.data.AppRoomDatabase;
import com.popularmovies.data.dao.FavoriteMovieDAO;
import com.popularmovies.data.entity.FavoriteMovie;

import java.util.List;

public class FavoriteMovieRepository {

    private final FavoriteMovieDAO favoriteMovieDAO;

    @SuppressWarnings("FieldCanBeLocal")
    private LiveData<List<FavoriteMovie>> favoriteMovies;

    public FavoriteMovieRepository(Application application) {
        AppRoomDatabase appRoomDatabase = AppRoomDatabase.getInstance(application);
        favoriteMovieDAO = appRoomDatabase.favoriteMovieDAO();
    }

    public void insert(FavoriteMovie favoriteMovie) {
        new InsertAsyncTask(favoriteMovieDAO).execute(favoriteMovie);
    }

    public void delete(FavoriteMovie favoriteMovie) {
        new DeleteAsyncTask(favoriteMovieDAO).execute(favoriteMovie);
    }

    public LiveData<List<FavoriteMovie>> findAll() {
        favoriteMovies = favoriteMovieDAO.findAll();
        return favoriteMovies;
    }

    public LiveData<FavoriteMovie> findByMovieId(Integer movieId) {
        return favoriteMovieDAO.findByMovieId(movieId);
    }

    private static class InsertAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {

        private final FavoriteMovieDAO favoriteMovieDAO;

        InsertAsyncTask(FavoriteMovieDAO favoriteMovieDAO) {
            this.favoriteMovieDAO = favoriteMovieDAO;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            favoriteMovieDAO.insert(favoriteMovies[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {

        private final FavoriteMovieDAO favoriteMovieDAO;

        DeleteAsyncTask(FavoriteMovieDAO favoriteMovieDAO) {
            this.favoriteMovieDAO = favoriteMovieDAO;
        }

        @Override
        protected Void doInBackground(FavoriteMovie... favoriteMovies) {
            favoriteMovieDAO.delete(favoriteMovies[0]);
            return null;
        }
    }

}
