package com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Class contains simple DB configuration with some Entities and one DAO
 */

@Entity(tableName = "movies")
public class Movie {


    @ColumnInfo(name = "vote_count")
    private int voteCount;

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "video")
    private boolean video;

    @ColumnInfo(name = "vote_average")
    private float voteAverage;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "popularity")
    private float popularity;

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

    //Set all of the getters and setters
    public void setVoteCount(int newVoteCount) {
        this.voteCount = newVoteCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public int getId() {
        return id;
    }

    public void setVideo(boolean newVideo) {
        this.video = newVideo;
    }

    public boolean getVideo() {
        return video;
    }

    public void setVoteAverage(int newAverage) {
        this.voteAverage = newAverage;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setPopularity(int newPopularity) {
        this.popularity = newPopularity;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPosterPath(String newPosterPath) {
        this.posterPath = newPosterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setOriginalLanguage(String newOriginalLanguage) {
        this.originalLanguage = newOriginalLanguage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalTitle(String newOriginalTitle) {
        this.originalTitle = newOriginalTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setGenreIds(int[] newGenreIds) {
        this.genreIds = newGenreIds;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setBackdropPath(String newBackdropPath) {
        this.backdropPath = newBackdropPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setAdult(boolean newAdult) {
        this.adult = newAdult;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setOverview(String newOverview) {
        this.overview = newOverview;
    }

    public String getOverview() {
        return overview;
    }

    public void setReleaseDate(String newReleaseDate) {
        this.releaseDate = newReleaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}