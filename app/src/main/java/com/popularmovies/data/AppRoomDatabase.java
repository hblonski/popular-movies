package com.popularmovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.popularmovies.data.dao.FavoriteMovieDAO;
import com.popularmovies.data.entity.FavoriteMovie;

@Database(entities = FavoriteMovie.class, version = 1)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract FavoriteMovieDAO favoriteMovieDAO();

    private static AppRoomDatabase appRoomDatabaseInstance;

    public static AppRoomDatabase getInstance(final Context context) {
        if (appRoomDatabaseInstance == null) {
            synchronized (AppRoomDatabase.class) {
                if (appRoomDatabaseInstance == null) {
                    appRoomDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppRoomDatabase.class,
                            "popular_movies_database")
                            .build();
                }
            }
        }
        return appRoomDatabaseInstance;
    }
}
