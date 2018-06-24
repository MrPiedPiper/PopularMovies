package com.fancystachestudios.popularmovies.popularmovies.MovieAPI;

import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.TableMovieItem;

/**
 * MovieAPIManager is a utility class for interacting with the themoviedb.org API
 */

public class MovieAPIManager {

    //API key 
    static String KEY = "Your API key goes here";


    //URL to movie list

    static String PART1 = "http://api.themoviedb.org/3/movie/";
    //part 2 is the sort type
    static String PART3 = "?api_key=";
    //part 4 is the api key
    static String PART5 = "&language=en-US&page=";
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
    private static String TRAILER_BASE1 = "https://api.themoviedb.org/3/movie/";
    //2 is the id
    private static String TRAILER_BASE3 = "/videos?api_key=";
    //4 is the API key
    private static String TRAILER_BASE5 = "&language=en-US";

    //Function returns path to movie JSON by sort path and page number
    public String getPathToMoviePage(String sortTypeString, int pageNumber){
        String returnString = PART1 + sortTypeString + PART3 + KEY + PART5 + pageNumber;
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
        return TRAILER_BASE1 + movieId + TRAILER_BASE3 + KEY + TRAILER_BASE5;
    }

    //Get path to Youtube video
    public String getYoutubeFromKey(String youtubeKey){
        return YOUTUBE_BASE + youtubeKey;
    }

    public String getVideoThumbnailPath(String videoKey){
        return THUMBNAIL_BASE1 + videoKey + THUMBNAIL_BASE3;
    }



}