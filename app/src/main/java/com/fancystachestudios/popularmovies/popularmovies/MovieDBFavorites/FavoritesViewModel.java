package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * ViewModel implemented with help from the Udacity course "Android Developer Nanodegree" lesson 8: "Android Architecture Components" sections 18-22
 */

public class FavoritesViewModel extends AndroidViewModel {


    private LiveData<List<RoomMovieObject>> favorites;


    public FavoritesViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = FavoritesDBSingleton.getInstance(this.getApplication());
        favorites = database.movieDao().getAll();
    }


    public LiveData<List<RoomMovieObject>> getFavorites() {
        return favorites;
    }
}
