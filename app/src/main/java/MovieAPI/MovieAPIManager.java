package MovieAPI;

import com.google.gson.Gson;

/**
 * Created by napuk on 5/19/2018.
 */

public class MovieAPIManager {
    private static String KEY = "d4eebd22dad46597eeaa3f0851eef72f";
    private static String READ_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkNGVlYmQyMmRhZDQ2NTk3ZWVhYTNmMDg1MWVlZjcyZiIsInN1YiI6IjViMDBjM2ZhYzNhMzY4NmM5OTAwNTFhNiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.4BwjTMn5lgnSYSeLke7JLCwc7kxKc1j4tHO0NaFoK7A";

    private static String PART1 = "https://api.themoviedb.org/3/discover/movie?api_key=";
    private static String PART2 = "&language=en-US&sort_by=";
    private static String PART3 = "&include_adult=false&include_video=false&page=1";

    private static String POPULARITY = "popularity.asc";
    private static String RATING = "vote_average.desc";


    public void getPopularMovieArray(){


        Gson gson = new Gson();
        //TODO implement function
    }

    public void getRatingMovieArray(){
        //TODO implement function
    }
}
