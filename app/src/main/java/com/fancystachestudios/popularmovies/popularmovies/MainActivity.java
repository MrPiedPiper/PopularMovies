package com.fancystachestudios.popularmovies.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL testUrl = null;
        try {
            testUrl = new URL("https://api.themoviedb.org/3/discover/movie?api_key=d4eebd22dad46597eeaa3f0851eef72f&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1");
            NetworkUtils.getJsonFromUrl(testUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void refreshMovies(MovieObject[] movies){
        for(MovieObject currMovie : movies){
            Log.d("naputest", currMovie.getTitle() + currMovie.getVoteCount());
        }
        //Toast.makeText(this, testString, Toast.LENGTH_SHORT);
    }
}
