package com.fancystachestudios.popularmovies.popularmovies;

import android.annotation.SuppressLint;
import android.support.v4.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.MovieAdapter;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main class for the MainActivity
 * Loader implemented with help from Udacity "Android Developer Nanodegree Program" Lesson 7: "Lifecycle" Part 16: "Leveraging Loaders" (Credit to the "The Android Open Source Project")
 */

public class MainActivity extends AppCompatActivity
        implements
            MovieAdapter.MovieClickListener,
            LoaderManager.LoaderCallbacks<ArrayList<MovieObject>>{

    //Prepare RecyclerView variables
    @BindView(R.id.mainRecyclerView) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    MovieAdapter.MovieClickListener movieClickListener;
    private RecyclerView.LayoutManager mLayoutManager;

    //Constant to save and restore URL being queried by the Loader
    private static final String SEARCH_QUERY_URL = "query";
    //themoviedb Loader ID
    private static final int THEMOVIEDB_LOADER_ID = 22;
    //Current query URL
    URL currentQuery = null;

    //Get access to utility class
    NetworkUtils networkUtils = new NetworkUtils();
    //get access to the MovieAPIManager class
    MovieAPIManager movieAPIManager = new MovieAPIManager();

    //Set default sort
    String currListSort = MovieAPIManager.POPULARITY;

    //Create array for storing the movies
    ArrayList<MovieObject> movieArray = new ArrayList<>();

    //Create general use Toast
    Toast generalToast;

    //Create general use Context
    Context mainActivityContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Set up RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movieClickListener = this;
        mAdapter = new MovieAdapter(movieArray, movieClickListener);
        mRecyclerView.setAdapter(mAdapter);

        //Load the movies
        loadPopularMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Get the sorting Spinner
        MenuItem item = menu.findItem(R.id.main_menu_spinner);
        final Spinner spinner = (Spinner)item.getActionView();

        //Create an ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.main_menu_sorting_spinner_array,
                R.layout.custom_spinner_text);
        //Set the Spinner's dropdown text to R.layout.custom_spinner_text
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_text);
        //Set the Spinner's adapter
        spinner.setAdapter(spinnerAdapter);

        //Add click functionality to the Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Get the text from the item selected
                String itemText = spinner.getSelectedItem().toString();
                //Get all of the text in the Spinner
                String[] possibleItems = getResources().getStringArray(R.array.main_menu_sorting_spinner_array);
                //Sort through each for the sort type and call the load function
                if (itemText.equals(possibleItems[0])) {
                    loadPopularMovies();
                } else if (itemText.equals(possibleItems[1])) {
                    loadTopRatedMovies();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Get the toolbar item pressed
        int itemId = item.getItemId();
        //If it's the refresh button, call the reload function
        if(itemId == R.id.main_menu_refresh){
            loadMovies();
        }
        return true;
    }

    //Function refreshes the list
    public void refreshMovies(ArrayList<MovieObject> movies){
        //If there's no movies in the ArrayList show error message to the user
        if(movies == null || movies.size() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(generalToast != null) {
                        generalToast.cancel();
                    }
                    generalToast = Toast.makeText(
                            mainActivityContext,
                            "There's no movies. \n" +
                                    "Maybe check your connection?",
                            Toast.LENGTH_LONG);
                    generalToast.show();
                    }
            });
            return;
        }
        //Set the movieArray to the retrieved movies
        movieArray = movies;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Create a new Adapter
                RecyclerView.Adapter newAdapter = new MovieAdapter(movieArray, movieClickListener);
                //Set the RecyclerView to use the Adapter
                mRecyclerView.swapAdapter(newAdapter, true);
            }
        });
    }

    @Override
    public void onMovieClick(int clickedItemIndex) {
        //Create a new Intent to move to the DetaiActivity
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        //putExtra the movie selected
        detailActivityIntent.putExtra(getString(R.string.detail_intent_tag), movieArray.get(clickedItemIndex));
        //Start the Activity
        startActivity(detailActivityIntent);
    }

    //Function sets the sort to popular movies, and refreshes the movies
    private void loadPopularMovies(){
        //Set the sort to Popularity
        currListSort = MovieAPIManager.POPULARITY;
        //Refresh the movies
        loadMovies();
    }

    //Function sets the sort to top rated movies, and refreshes the movies
    private void loadTopRatedMovies(){
        //Set the sort to Rating
        currListSort = MovieAPIManager.RATING;
        //Refresh the movies
        loadMovies();
    }

    private void loadMovies(){

        //Get the URL
        ArrayList<String> movieSearchList = new ArrayList<>();
        for(int i=0; i<10; i++){
            movieSearchList.add(movieAPIManager.getPathToMoviePage(currListSort, i+1));
        }
        //Create a Bundle for the Loader
        Bundle queryBundle = new Bundle();
        //Insert a String
        queryBundle.putStringArrayList(SEARCH_QUERY_URL, movieSearchList);

        //Get the LoaderManager
        LoaderManager loaderManager = getSupportLoaderManager();
        //Get the Loader by ID
        android.support.v4.content.Loader<ArrayList<MovieObject>> movieLoader = loaderManager.getLoader(THEMOVIEDB_LOADER_ID);
        if(movieLoader == null){
            //If it's not there, make it.
            loaderManager.initLoader(THEMOVIEDB_LOADER_ID, queryBundle, this).forceLoad();
        }else{
            //If it's there, restart it.
            loaderManager.restartLoader(THEMOVIEDB_LOADER_ID, queryBundle, this).forceLoad();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public android.support.v4.content.Loader<ArrayList<MovieObject>> onCreateLoader(int i, final Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<ArrayList<MovieObject>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }
                //TODO add loading gif
            }

            @Override
            public ArrayList<MovieObject> loadInBackground() {
                //Get the URL to the movies
                ArrayList<String> movieSearchArrayList = args.getStringArrayList(SEARCH_QUERY_URL);
                //Set the movie ArrayList
                ArrayList<MovieObject> movies = new ArrayList<>();
                ArrayList<MovieObject> currPage = new ArrayList<>();
                for(int i=0; i<movieSearchArrayList.size(); i++){
                    try {
                        //Convert the String into a URL
                        URL movieSearchUrl = new URL(movieSearchArrayList.get(i));
                        //Get the movies and set the movies variable
                        currPage = networkUtils.getMoviesFromUrl(movieSearchUrl);
                        for(int j=0; j<currPage.size(); j++){
                            movies.add(currPage.get(j));
                            Log.d("mytest", "This is " + currPage.get(j));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                    Log.d("mytest", "There are " + movies.size() + " movies");
                }
                //Return movies
                return movies;
            }

        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<MovieObject>> loader, ArrayList<MovieObject> mMovieList) {
        //Refresh the movies
        refreshMovies(mMovieList);
        //TODO loading GIF
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<MovieObject>> loader) {

    }


}
