package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fancystachestudios.popularmovies.popularmovies.MainActivity;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by napuk on 5/20/2018.
 */

public class NetworkUtils extends AppCompatActivity{

    CustomMovieList customMovieList = new CustomMovieList();

    public URI buildUriFromUrl(URL url) {
        URI returnUri = null;
        try {
            returnUri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return returnUri;
    }

    public JSONObject stringToJsonObject(String string) {
        JSONObject returnJson = null;
        try {
            returnJson = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJson;
    }

    public ArrayList<MovieObject> getMoviesFromUrl(URL url) throws IOException {

        //Followed these instructions to learn how to use OkHttp http://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url.toString())
                .build();

        Response response = client.newCall(request).execute();

        String resultString = response.body().string();
        JSONObject baseJsonResults = stringToJsonObject(resultString);
        ArrayList<MovieObject> movies = getMovieArrayFromJsonResults(baseJsonResults);
        return movies;
    }

    private ArrayList<MovieObject> getMovieArrayFromJsonResults(JSONObject jsonResults) {
        try {
            Gson gson = new Gson();
            JSONArray jsonArrayMovies = jsonResults.getJSONArray("results");
            ArrayList<MovieObject> movies = new ArrayList<>();
            for(int i = 0; i < jsonArrayMovies.length(); i++){
                //movies.add(i, gson.fromJson(jsonArrayMovies.get(i).toString(), MovieObject.class));
                MovieObject currMovie = gson.fromJson(jsonArrayMovies.get(i).toString(), MovieObject.class);
                movies.add(i, currMovie);
                Log.d("testing", "Number " + i);
                Log.d("testing", "Array is " + movies.size());
                Log.d("testing", "Movie is " + movies.get(i).getTitle());
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
