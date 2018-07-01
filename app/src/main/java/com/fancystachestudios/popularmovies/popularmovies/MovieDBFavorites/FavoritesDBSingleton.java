package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.fancystachestudios.popularmovies.popularmovies.R;

/**
 * Basic singleton to ensure all instances of the favorites DB are the same, and thus all activate the observers when changed.
 */

public class FavoritesDBSingleton {

    private static AppDatabase myDatabase;

    private FavoritesDBSingleton(Context context){
    }

    public static AppDatabase getInstance(Context currContext){
        if(myDatabase == null){
            myDatabase = Room.databaseBuilder(currContext.getApplicationContext(), AppDatabase.class, currContext.getString(R.string.favorite_movies_table_name)).build();
        }
        return myDatabase;
    }


}
