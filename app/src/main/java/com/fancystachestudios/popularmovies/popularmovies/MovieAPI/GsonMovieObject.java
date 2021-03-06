package com.fancystachestudios.popularmovies.popularmovies.MovieAPI;

import java.io.Serializable;

/**
 * Custom Class for storing movie data (GSON puts the info in before being transferred to the RoomMovieObject
 */

public class GsonMovieObject implements Serializable {

    //Create all necessary variables
    private int vote_count;
    private int id;
    private boolean video;
    private float vote_average;
    private String title;
    private float popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private int[] genre_ids;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;

    public GsonMovieObject(){
        super();
    }

    public GsonMovieObject(int vote_count,
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
        this.vote_count = vote_count;
        this.id = id;
        this.video = video;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
    }

    //Set all of the getters and setters

    public void setVoteCount(int newVoteCount){
        this.vote_count = newVoteCount;
    }

    public int getVoteCount(){
        return vote_count;
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
        this.vote_average = newAverage;
    }

    public float getVoteAverage(){
        return vote_average;
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

    public float getPopularity(){
        return popularity;
    }

    public void setPosterPath(String newPosterPath){
        this.poster_path = newPosterPath;
    }

    public String getPosterPath(){
        return poster_path;
    }

    public void setOriginalLanguage(String newOriginalLanguage){
        this.original_language = newOriginalLanguage;
    }

    public String getOriginalLanguage(){
        return original_language;
    }

    public void setOriginalTitle(String newOriginalTitle){
        this.original_title = newOriginalTitle;
    }

    public String getOriginalTitle(){
        return original_title;
    }

    public void setGenreIds(int[] newGenreIds){
        this.genre_ids = newGenreIds;
    }

    public int[] getGenreIds(){
        return genre_ids;
    }

    public void setBackdropPath(String newBackdropPath){
        this.backdrop_path = newBackdropPath;
    }

    public String getBackdropPath(){
        return backdrop_path;
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
        this.release_date = newReleaseDate;
    }

    public String getReleaseDate(){
        return release_date;
    }
}
