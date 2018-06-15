package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.widget.CircularProgressDrawable;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;

import java.io.Serializable;

/**
 * Custom Class for storing movie data
 */

@Entity(tableName = "movies")
public class TableMovieItem implements Serializable {

    //Create all necessary variables
    @ColumnInfo(name = "vote_count")
    private int voteCount;

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "video")
    private boolean video;

    @ColumnInfo(name = "vote_average")
    private int voteAverage;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "popularity")
    private int popularity;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @ColumnInfo(name = "original_language")
    private String originalLanguage;

    @ColumnInfo(name = "original_title")
    private String originalTitle;

    @ColumnInfo(name = "genre_ids")
    private int[] genreIds;

    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = "adult")
    private boolean adult;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "release_date")
    private String releaseDate;


    public TableMovieItem(){
        super();
    }

    public TableMovieItem(MovieObject movieObject){
        this.voteCount = movieObject.getVoteCount();
        this.id = movieObject.getId();
        this.video = movieObject.getVideo();
        this.voteAverage = (int) movieObject.getVoteAverage() * 100;
        this.title = movieObject.getTitle();
        this.popularity = (int) movieObject.getPopularity() * 100;
        this.posterPath = movieObject.getPosterPath();
        this.originalLanguage = movieObject.getOriginalLanguage();
        this.originalTitle = movieObject.getOriginalTitle();
        this.genreIds = movieObject.getGenreIds();
        this.backdropPath = movieObject.getBackdropPath();
        this.adult = movieObject.getAdult();
        this.overview = movieObject.getOverview();
        this.releaseDate = movieObject.getReleaseDate();
    }

    public TableMovieItem(int vote_count,
                       int id,
                       boolean video,
                       float vote_average,
                       String title,
                       float popularity,
                       String poster_path,
                       String original_language,
                       String original_title,
                       int[] genre_ids,
                       String backdrop_path,
                       boolean adult,
                       String overview,
                       String release_date){
        this.voteCount = vote_count;
        this.id = id;
        this.video = video;
        this.voteAverage = (int) vote_average * 100;
        this.title = title;
        this.popularity = (int) popularity * 100;
        this.posterPath = poster_path;
        this.originalLanguage = original_language;
        this.originalTitle = original_title;
        this.genreIds = genre_ids;
        this.backdropPath = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = release_date;
    }

    //Set all of the getters and setters

    public void setVoteCount(int newVoteCount){
        this.voteCount = newVoteCount;
    }

    public int getVoteCount(){
        return voteCount;
    }

    public void setId(int newId){
        this.id = newId;
    }

    public int getId(){
        return id;
    }

    public void setVideo(boolean newVideo){
        this.video = newVideo;
    }

    public boolean getVideo(){
        return video;
    }

    public void setVoteAverage(int newAverage){
        this.voteAverage = newAverage;
    }

    public int getVoteAverage(){
        return voteAverage / 100;
    }

    public void setTitle(String newTitle){
        this.title = newTitle;
    }

    public String getTitle(){
        return title;
    }

    public void setPopularity(int newPopularity){
        this.popularity = newPopularity;
    }

    public int getPopularity(){
        return popularity / 100;
    }

    public void setPosterPath(String newPosterPath){
        this.posterPath = newPosterPath;
    }

    public String getPosterPath(){
        return posterPath;
    }

    public void setOriginalLanguage(String newOriginalLanguage){
        this.originalLanguage = newOriginalLanguage;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public void setOriginalTitle(String newOriginalTitle){
        this.originalTitle = newOriginalTitle;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setGenreIds(int[] newGenreIds){
        this.genreIds = newGenreIds;
    }

    public int[] getGenreIds(){
        return genreIds;
    }

    public void setBackdropPath(String newBackdropPath){
        this.backdropPath = newBackdropPath;
    }

    public String getBackdropPath(){
        return backdropPath;
    }

    public void setAdult(boolean newAdult){
        this.adult = newAdult;
    }

    public boolean getAdult(){
        return adult;
    }

    public void setOverview(String newOverview){
        this.overview = newOverview;
    }

    public String getOverview(){
        return overview;
    }

    public void setReleaseDate(String newReleaseDate){
        this.releaseDate = newReleaseDate;
    }

    public String getReleaseDate(){
        return releaseDate;
    }
}
