package com.fancystachestudios.popularmovies.popularmovies.MovieAPI;

/**
 * MovieObject class is a template for the MovieObject
 */

public class MovieObject {

    private int voteCount;
    private int id;
    private boolean video;
    private float voteAverage;
    private String title;
    private float popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private int[] genreIds;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private String releaseDate;

    public MovieObject(){
        super();
    }

    public MovieObject(int voteCount,
                       int id,
                       boolean video,
                       float voteAverage,
                       String title,
                       float popularity,
                       String posterPath,
                       String originalLanguage,
                       String originalTitle,
                       int[] genreIds,
                       String backdropPath,
                       boolean adult,
                       String overview,
                       String releaseDate){
        this.voteCount = voteCount;
        this.id = id;
        this.video = video;
        this.voteAverage = voteAverage;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIds = genreIds;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public void setVoteCount(int newVoteCount){
        this.voteCount = voteCount;
    }

    public int getVoteCount(){
        return voteCount;
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
        this.voteAverage = voteAverage;
    }

    public float getVoteAverage(){
        return voteAverage;
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
        this.posterPath = posterPath;
    }

    public String getPosterPath(){
        return posterPath;
    }

    public void setOriginalLanguage(String newOriginalLanguage){
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public void setOriginalTitle(String newOriginalTitle){
        this.originalTitle = originalTitle;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setGenreIds(int[] newGenreIds){
        this.genreIds = genreIds;
    }

    public int[] getGenreids(){
        return genreIds;
    }

    public void setBackdropPath(String newBackdropPath){
        this.backdropPath = backdropPath;
    }

    public String getBackdropPath(){
        return backdropPath;
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
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate(){
        return releaseDate;
    }
}
