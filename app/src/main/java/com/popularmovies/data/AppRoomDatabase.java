package com.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
