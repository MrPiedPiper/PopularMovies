package com.fancystachestudios.popularmovies.popularmovies.Utils;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
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
    public ArrayList<MovieObject> getMoviesFromUrl(URL url) throws IOException {

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
        //Get movies from the JSONObject
        ArrayList<MovieObject> movies = getMovieArrayFromJsonResults(baseJsonResults);

        //Return the movies
        return movies;
    }

    /**
     * Function converts the JSON retrieved from themoviedb to an ArrayList of movies
     * @param jsonResults JSON retrieved from themoviedb
     * @return ArrayList of MovieObjects
     */
    private ArrayList<MovieObject> getMovieArrayFromJsonResults(JSONObject jsonResults) {
        try {
            //Gain access to Gson
            Gson gson = new Gson();
            //Get the JSONArray of movies (Could cause JSONException)
            JSONArray jsonArrayMovies = jsonResults.getJSONArray("results");
            //Make the "movies" ArrayList
            ArrayList<MovieObject> movies = new ArrayList<>();
            //For each movie in the JSON,
            for(int i = 0; i < jsonArrayMovies.length(); i++){
                //put the JSON into a MovieObject (using Gson) (Could cause JSONException)
                MovieObject currMovie = gson.fromJson(jsonArrayMovies.get(i).toString(), MovieObject.class);
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
}
