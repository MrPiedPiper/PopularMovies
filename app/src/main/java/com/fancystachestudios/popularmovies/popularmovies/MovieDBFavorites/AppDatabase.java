package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

/**
 * Class constructs the MovieDatabase
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;


@Database(entities = {RoomMovieObject.class}, version = 1)
@TypeConverters({Converters.class})
public  abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
