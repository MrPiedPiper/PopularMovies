package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.util.Log;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.GsonMovieObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.ReviewObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.TrailerObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.RoomMovieObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Utility class for accessing the internet
 */

public class NetworkUtils {

    /**
     * Function converts JSON String to a JSONObject
     * @param string JSON String
     * @return JSONObject
     */
    public JSONObject stringToJsonObject(String string) {
        JSONObject returnJson = null;
        try {
            returnJson = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJson;
    }

    /**
     * Function gets ArrayList of Movies from provided URL
     * @param url JSON location
     * @return ArrayList of MovieObjects
     * @throws IOException failed to retrieve from URL
     */
    public ArrayList<RoomMovieObject> getMoviesFromUrl(URL url) throws IOException {

        JSONObject movieJson = getJsonObjectFromUrl(url);

        //Get movies from the JSONObject
        ArrayList<RoomMovieObject> movies = getMovieArrayFromJsonResults(movieJson);

        //Return the movies
        return movies;
    }

    /**
     * Function gets ArrayList of Trailers from provided URL
     * @param url JSON location
     * @return ArrayList of TrailerObjects
     * @throws IOException failed to retrieve from URL
     */
    public ArrayList<TrailerObject> getTrailersFromUrl(URL url) throws IOException {
        JSONObject trailerJson = getJsonObjectFromUrl(url);
        Log.d("JSONTest", trailerJson.toString());
        ArrayList<TrailerObject> trailers = getTrailerArrayFromJsonResults(trailerJson);

        return trailers;
    }

    /**
     * Function gets ArrayList of Reviews from provided URL
     * @param url JSON location
     * @return ArrayList of ReviewObjects
     * @throws IOException failed to retrieve from URL
     */
    public ArrayList<ReviewObject> getReviewsFromUrl(URL url) throws IOException {
        JSONObject reviewJson = getJsonObjectFromUrl(url);
        ArrayList<ReviewObject> reviews = getReviewArrayFromJsonResults(reviewJson);
        return reviews;
    }

    /**
     * Function gets JSON from provided URL
     * @param url JSON location
     * @return ArrayList of TrailerObjects
     * @throws IOException failed to retrieve from URL
     */
    private JSONObject getJsonObjectFromUrl(URL url) throws IOException {

        //Followed these instructions to learn how to use OkHttp http://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html

        //Get the OkHttpCliend
        OkHttpClient client = new OkHttpClient();

        //Make a new request
        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        //Get the response (Might throw IOExeption)
        Response response = client.newCall(request).execute();

        //Make a String from the response
        String resultString = response.body().string();
        //Convert the String into a JSONObject
        JSONObject baseJsonResults = stringToJsonObject(resultString);

        return baseJsonResults;
    }

    /**
     * Function converts the JSON retrieved from themoviedb to an ArrayList of movies
     * @param jsonResults JSON retrieved from themoviedb
     * @return ArrayList of MovieObjects
     */
    private ArrayList<RoomMovieObject> getMovieArrayFromJsonResults(JSONObject jsonResults) {
        try {
            //Gain access to Gson
            Gson gson = new Gson();
            //Get the JSONArray of movies (Could cause JSONException)
            JSONArray jsonArrayMovies = jsonResults.getJSONArray("results");
            //Make the "movies" ArrayList
            ArrayList<RoomMovieObject> movies = new ArrayList<>();
            //For each movie in the JSON,
            for(int i = 0; i < jsonArrayMovies.length(); i++){
                //put the JSON into a GsonMovieObject (using Gson) (Could cause JSONException)
                GsonMovieObject gsonGsonMovieObject = gson.fromJson(jsonArrayMovies.get(i).toString(), GsonMovieObject.class);
                //Convert the gsonGsonMovieObject to a RoomMovieObject
                RoomMovieObject currMovie = new RoomMovieObject(gsonGsonMovieObject);
                //Add the movie to the ArrayList
                movies.add(i, currMovie);
            }
            //Return the movies ArrayList
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //If it fails, return null
        return null;
    }

    /**
     * Function converts the JSON retrieved from themoviedb to an ArrayList of trailers
     * @param jsonResults JSON retrieved from themoviedb
     * @return ArrayList of TrailerObjects
     */
    private ArrayList<TrailerObject> getTrailerArrayFromJsonResults(JSONObject jsonResults) {
        try {
            //Gain access to Gson
            Gson gson = new Gson();
            //Get the JSONArray of trailers (Could cause JSONException)
            JSONArray jsonArrayTrailers = jsonResults.getJSONArray("results");
            //Make the "trailers" ArrayList
            ArrayList<TrailerObject> trailers = new ArrayList<>();
            //For each movie in the JSON,
            for(int i = 0; i < jsonArrayTrailers.length(); i++){
                //put the JSON into a TrailerObject (using Gson) (Could cause JSONException)
                TrailerObject gsonTrailerObject = gson.fromJson(jsonArrayTrailers.get(i).toString(), TrailerObject.class);
                //Add the movie to the ArrayList
                trailers.add(i, gsonTrailerObject);
            }
            //Return the movies ArrayList
            return trailers;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //If it fails, return null
        return null;
    }

    /**
     * Function converts the JSON retrieved from themoviedb to an ArrayList of reviews
     * @param jsonResults JSON retrieved from themoviedb
     * @return ArrayList of MovieObjects
     */
    private ArrayList<ReviewObject> getReviewArrayFromJsonResults(JSONObject jsonResults){
        try {
            //Gain access to Gson
            Gson gson = new Gson();
            //Get the JSONArray of trailers (Could cause JSONException)
            JSONArray jsonArrayReviews = jsonResults.getJSONArray("results");
            //Make the "trailers" ArrayList
            ArrayList<ReviewObject> reviews = new ArrayList<>();
            //For each movie in the JSON,
            for(int i = 0; i < jsonArrayReviews.length(); i++){
                //put the JSON into a TrailerObject (using Gson) (Could cause JSONException)
                ReviewObject gsonReviewObject = gson.fromJson(jsonArrayReviews.get(i).toString(), ReviewObject.class);
                //Add the movie to the ArrayList
                reviews.add(i, gsonReviewObject);
            }
            //Return the movies ArrayList
            return reviews;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //If it fails, return null
        return null;
    }
}
