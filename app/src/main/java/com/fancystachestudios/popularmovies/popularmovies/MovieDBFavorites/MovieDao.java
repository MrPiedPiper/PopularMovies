package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

/**
 * Class is the DAO for the Room
 */

@Dao
@TypeConverters({Converters.class})
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<RoomMovieObject>> getAll();

    @Query("SELECT * FROM movies WHERE id IN (:movieIds)")
    List<RoomMovieObject> loadAllByIds(int[] movieIds);

    @Query("SELECT * FROM movies WHERE id LIKE :movieId LIMIT 1")
    RoomMovieObject findById(int movieId);

    @Query("SELECT * FROM movies WHERE title LIKE :movieTitle LIMIT 1")
    RoomMovieObject findByTitle(String movieTitle);

    @Insert
    void insertAll(RoomMovieObject... movies);

    @Delete
    void delete(RoomMovieObject movie);

}
