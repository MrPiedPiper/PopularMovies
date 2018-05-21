package com.fancystachestudios.popularmovies.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.MovieAdapter;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.mainRecyclerView) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    MovieObject[] movieArray = new MovieObject[0];

    NetworkUtils networkUtils = new NetworkUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MovieAdapter(movieArray);
        mRecyclerView.setAdapter(mAdapter);

        myTest();
    }

    private void myTest(){
        URL testUrl = null;
        try {
            testUrl = new URL("https://api.themoviedb.org/3/discover/movie?api_key=d4eebd22dad46597eeaa3f0851eef72f&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        MovieObject[] movies = networkUtils.getMoviesFromUrl(testUrl);
        refreshMovies(movies);
    }

    public void refreshMovies(MovieObject[] movies){
        for(MovieObject currMovie : movies){
            Log.d("movieTest", currMovie.getTitle() + currMovie.getVoteCount());
        }
        movieArray = movies;
        mAdapter = new MovieAdapter(movieArray);
        mAdapter.notifyDataSetChanged();
        //Toast.makeText(this, testString, Toast.LENGTH_SHORT);
    }
}
