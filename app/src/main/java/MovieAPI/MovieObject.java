package MovieAPI;

import android.graphics.Movie;

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

    public void setVoteCount(int newVoteCount){
        this.voteCount = newVoteCount;
    }

    public int getVoteCount(){
        return voteCount;
    }

    public void setId(int newId){
        this.id = newId;
    }

    public int getid(){
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

    public float getVoteAverage(){
        return voteAverage;
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

    public int[] getGenreids(){
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
