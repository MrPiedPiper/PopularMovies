package com.fancystachestudios.popularmovies.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.CustomMovieList;
import com.fancystachestudios.popularmovies.popularmovies.Utils.MovieAdapter;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{
    @BindView(R.id.mainRecyclerView) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    NetworkUtils networkUtils = new NetworkUtils();
    CustomMovieList customMovieList = new CustomMovieList();


    ArrayList<MovieObject> movieArray;

    MovieAdapter.MovieClickListener movieClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        movieArray = new ArrayList<MovieObject>();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        movieClickListener = this;
        mAdapter = new MovieAdapter(movieArray, movieClickListener);
        mRecyclerView.setAdapter(mAdapter);

        new myTest().execute();
    }

    private class myTest extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            URL testUrl = null;
            try {
                testUrl = new URL("https://api.themoviedb.org/3/discover/movie?api_key=d4eebd22dad46597eeaa3f0851eef72f&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ArrayList<MovieObject> movies = new ArrayList<MovieObject>();
            try {
                movies = networkUtils.getMoviesFromUrl(testUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            refreshMovies(movies);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void refreshMovies(ArrayList<MovieObject> movies){
        Log.d("mytest", "Length is "+movies.size());
        if(movies.size() == 0) {
            Log.d("errortime", "It's empty :<");
        }
        /**
        for(MovieObject currMovie : movies){
            //Log.d("movieTest", currMovie.getTitle() + "Votes: " + currMovie.getVoteCount());
        }
         **/
        movieArray = movies;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView.Adapter newAdapter = new MovieAdapter(movieArray, movieClickListener);
                mRecyclerView.swapAdapter(newAdapter, true);
                mAdapter.notifyDataSetChanged();
            }
        });
        //Toast.makeText(this, testString, Toast.LENGTH_SHORT);
    }

    @Override
    public void onMovieClick(int clickedItemIndex) {
        //TODO Change this out for DB
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(getString(R.string.detail_intent_tag), movieArray.get(clickedItemIndex));
        startActivity(intent);
    }
}
