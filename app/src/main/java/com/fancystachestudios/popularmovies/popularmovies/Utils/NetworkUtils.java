package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.os.Debug;
import android.util.Log;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.TrailersObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.TableMovieItem;
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
     * Function gets Movie JSON from provided URL
     * @param url JSON location
     * @return ArrayList of MovieObjects
     * @throws IOException failed to retrieve from URL
     */
    public ArrayList<TableMovieItem> getMoviesFromUrl(URL url) throws IOException {

        JSONObject movieJson = getJsonObjectFromUrl(url);

        //Get movies from the JSONObject
        ArrayList<TableMovieItem> movies = getMovieArrayFromJsonResults(movieJson);

        //Return the movies
        return movies;
    }

    public ArrayList<TrailersObject> getTrailersFromUrl(URL url) throws IOException {
        JSONObject trailerJson = getJsonObjectFromUrl(url);
        Log.d("JSONTest", trailerJson.toString());
        ArrayList<TrailersObject> trailers = getTrailerArrayFromJsonResults(trailerJson);

        return trailers;
    }

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
    private ArrayList<TableMovieItem> getMovieArrayFromJsonResults(JSONObject jsonResults) {
        try {
            //Gain access to Gson
            Gson gson = new Gson();
            //Get the JSONArray of movies (Could cause JSONException)
            JSONArray jsonArrayMovies = jsonResults.getJSONArray("results");
            //Make the "movies" ArrayList
            ArrayList<TableMovieItem> movies = new ArrayList<>();
            //For each movie in the JSON,
            for(int i = 0; i < jsonArrayMovies.length(); i++){
                //put the JSON into a MovieObject (using Gson) (Could cause JSONException)
                MovieObject gsonMovieObject = gson.fromJson(jsonArrayMovies.get(i).toString(), MovieObject.class);
                //Convert the gsonMovieObject to a TableMovieItem
                TableMovieItem currMovie = new TableMovieItem(gsonMovieObject);
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
    private ArrayList<TrailersObject> getTrailerArrayFromJsonResults(JSONObject jsonResults) {
        try {
            //Gain access to Gson
            Gson gson = new Gson();
            //Get the JSONArray of trailers (Could cause JSONException)
            JSONArray jsonArrayTrailers = jsonResults.getJSONArray("results");
            //Make the "trailers" ArrayList
            ArrayList<TrailersObject> trailers = new ArrayList<>();
            //For each movie in the JSON,
            for(int i = 0; i < jsonArrayTrailers.length(); i++){
                //put the JSON into a TrailersObject (using Gson) (Could cause JSONException)
                TrailersObject gsonTrailerObject = gson.fromJson(jsonArrayTrailers.get(i).toString(), TrailersObject.class);
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
}
