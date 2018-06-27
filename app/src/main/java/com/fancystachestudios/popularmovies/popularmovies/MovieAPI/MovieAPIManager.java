package com.fancystachestudios.popularmovies.popularmovies.MovieAPI;

import android.util.Log;

import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.TableMovieItem;

/**
 * MovieAPIManager is a utility class for interacting with the themoviedb.org API
 */

public class MovieAPIManager {

    //API key
    static String KEY = "Your API key goes here";

    //Movie URL base
    static String MOVIE_BASE = "http://api.themoviedb.org/3/movie/";
    //English and page base
    static String ENG_AND_PAGE = "&language=en-US&page=";


    //URL to movie list
    //part 1 is MOVIE_BASE
    //part 2 is the sort type
    static String PART3 = "?api_key=";
    //part 4 is the api key
    //part 6 is the page number


    //Base URLs for loading_animation images
    static String IMAGE_BASE_185 = "http://image.tmdb.org/t/p/w185/";
    static String IMAGE_BASE_780 = "http://image.tmdb.org/t/p/w780/";

    //Strings for sorting methods
    public static String POPULARITY = "popular";
    public static String RATING = "top_rated";

    //String base for the Youtube URL
    private static String YOUTUBE_BASE = "https://www.youtube.com/watch?v=";

    //String base for the thumbnail path URL
    private static String THUMBNAIL_BASE1 = "https://img.youtube.com/vi/";
    //2 is the id
    private static String THUMBNAIL_BASE3 = "/hqdefault.jpg";

    //String base for the trailer list path URL
    //part 1 is MOVIE_BASE
    //2 is the id
    private static String TRAILER_BASE3 = "/videos?api_key=";
    //4 is the API key
    private static String TRAILER_BASE5 = "&language=en-US";

    //String base for the Review list path URL
    //part 1 is MOVIE_BASE
    //part 2 is the movie ID
    private static String REVIEW_BASE3 = "/reviews?api_key=";
    //part 3 is the API Key
    //part 4 is the ENG_AND_PAGE
    //part 5 is the page #

    //Function returns path to movie JSON by sort path and page number
    public String getPathToMoviePage(String sortTypeString, int pageNumber){
        String returnString = MOVIE_BASE + sortTypeString + PART3 + KEY + ENG_AND_PAGE + pageNumber;
        return returnString;
    }

    //Method returns path the the poster image
    public String getPosterPath(TableMovieItem movie){
        return IMAGE_BASE_185 + movie.getPosterPath();
    }

    //Method returns path to the backdrop image
    public String getBackdropPath(TableMovieItem movie){
        return IMAGE_BASE_780 + movie.getBackdropPath();
    }

    //Method returns path to the trailer JSON
    public String getTrailerListPath(int movieId){
        return MOVIE_BASE + movieId + TRAILER_BASE3 + KEY + TRAILER_BASE5;
    }

    //Get path to Youtube video
    public String getYoutubeFromKey(String youtubeKey){
        return YOUTUBE_BASE + youtubeKey;
    }

    public String getVideoThumbnailPath(String videoKey){
        Log.d("test", THUMBNAIL_BASE1 + videoKey + THUMBNAIL_BASE3);
        return THUMBNAIL_BASE1 + videoKey + THUMBNAIL_BASE3;
    }

    public String getReviewPath(int movieID, int pageNum){
        Log.d("test", MOVIE_BASE + movieID + REVIEW_BASE3 + KEY + ENG_AND_PAGE + pageNum);
        return MOVIE_BASE + movieID + REVIEW_BASE3 + KEY + ENG_AND_PAGE + pageNum;
    }



}