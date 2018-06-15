package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class is the DAO for the Room
 */

@Dao
@TypeConverters({Converters.class})
public interface MovieDao {

    @Query("SELECT * FROM movies")
    List<TableMovieItem> getAll();

    @Query("SELECT * FROM movies WHERE id IN (:movieIds)")
    List<TableMovieItem> loadAllByIds(int[] movieIds);

    @Query("SELECT * FROM movies WHERE id LIKE :movieId LIMIT 1")
    TableMovieItem findById(int movieId);

    @Query("SELECT * FROM movies WHERE title LIKE :movieTitle LIMIT 1")
    TableMovieItem findByTitle(String movieTitle);

    @Insert
    void insertAll(TableMovieItem... movies);

    @Delete
    void delete(TableMovieItem movie);

}
