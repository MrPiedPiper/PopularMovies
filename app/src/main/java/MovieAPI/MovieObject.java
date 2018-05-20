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
        voteCount = newVoteCount;
    }

    public int getVoteCount(){
        return voteCount;
    }

    public void setId(int newId){
        id = newId;
    }

    public int getid(){
        return id;
    }

    public void setVideo(boolean newVideo){
        video = newVideo;
    }

    public boolean getVideo(){
        return video;
    }

    public void setVoteAverage(int newAverage){
        voteAverage = newAverage;
    }

    public float getVoteAverage(){
        return voteAverage;
    }

    public void setTitle(String newTitle){
        title = newTitle;
    }

    public String getTitle(){
        return title;
    }

    public void setPopularity(int newPopularity){
        popularity = newPopularity;
    }

    public float getPopularity(){
        return popularity;
    }

    public void setPosterPath(String newPosterPath){
        posterPath = newPosterPath;
    }

    public String getPosterPath(){
        return posterPath;
    }

    public void setOriginalLanguage(String newOriginalLanguage){
        originalLanguage = newOriginalLanguage;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public void setOriginalTitle(String newOriginalTitle){
        originalTitle = newOriginalTitle;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setGenreIds(int[] newGenreIds){
        genreIds = newGenreIds;
    }

    public int[] getGenreids(){
        return genreIds;
    }

    public void setBackdropPath(String newBackdropPath){
        backdropPath = newBackdropPath;
    }

    public String getBackdropPath(){
        return backdropPath;
    }

    public void setAdult(boolean newAdult){
        adult = newAdult;
    }

    public boolean getAdult(){
        return adult;
    }

    public void setOverview(String newOverview){
        overview = newOverview;
    }

    public String getOverview(){
        return overview;
    }

    public void setReleaseDate(String newReleaseDate){
        releaseDate = newReleaseDate;
    }

    public String getReleaseDate(){
        return releaseDate;
    }
}
