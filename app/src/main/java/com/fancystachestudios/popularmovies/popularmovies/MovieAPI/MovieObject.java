package com.fancystachestudios.popularmovies.popularmovies.MovieAPI;

import java.io.Serializable;

/**
 * MovieObject class is a template for the MovieObject
 */

public class MovieObject implements Serializable {

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

    public MovieObject(){
        super();
    }

    public MovieObject(int vote_count,
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

    public void setVoteCount(int newVoteCount){
        this.vote_count = vote_count;
    }

    public int getVoteCount(){
        return vote_count;
    }

    public void setId(int newId){
        this.id = id;
    }

    public int getid(){
        return id;
    }

    public void setVideo(boolean newVideo){
        this.video = video;
    }

    public boolean getVideo(){
        return video;
    }

    public void setVoteAverage(int newAverage){
        this.vote_average = vote_average;
    }

    public float getVoteAverage(){
        return vote_average;
    }

    public void setTitle(String newTitle){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setPopularity(int newPopularity){
        this.popularity = popularity;
    }

    public float getPopularity(){
        return popularity;
    }

    public void setPosterPath(String newPosterPath){
        this.poster_path = poster_path;
    }

    public String getPosterPath(){
        return poster_path;
    }

    public void setOriginalLanguage(String newOriginalLanguage){
        this.original_language = original_language;
    }

    public String getOriginalLanguage(){
        return original_language;
    }

    public void setOriginalTitle(String newOriginalTitle){
        this.original_title = original_title;
    }

    public String getOriginalTitle(){
        return original_title;
    }

    public void setGenreIds(int[] newGenreIds){
        this.genre_ids = genre_ids;
    }

    public int[] getGenreids(){
        return genre_ids;
    }

    public void setBackdropPath(String newBackdropPath){
        this.backdrop_path = backdrop_path;
    }

    public String getBackdropPath(){
        return backdrop_path;
    }

    public void setAdult(boolean newAdult){
        this.adult = adult;
    }

    public boolean getAdult(){
        return adult;
    }

    public void setOverview(String newOverview){
        this.overview = overview;
    }

    public String getOverview(){
        return overview;
    }

    public void setReleaseDate(String newReleaseDate){
        this.release_date = release_date;
    }

    public String getReleaseDate(){
        return release_date;
    }
}
