package com.fancystachestudios.popularmovies.popularmovies;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.AppDatabase;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.FavoritesDBSingleton;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.FavoritesViewModel;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.RoomMovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.EndlessScrollListener;
import com.fancystachestudios.popularmovies.popularmovies.Utils.MovieAdapter;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main class for the MainActivity
 *
 * Loader implemented with help from Udacity "Android Developer Nanodegree Program" Lesson 7: "Lifecycle" Part 16: "Leveraging Loaders" (Credit to the "The Android Open Source Project")
 *
 * Infinite scrolling Code from the "codepath" github repository.
 * Instructions I followed: https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */

public class MainActivity extends AppCompatActivity
        implements
            MovieAdapter.MovieClickListener,
            LoaderManager.LoaderCallbacks<ArrayList<RoomMovieObject>>{

    //Get View(s)
    @BindView(R.id.mainRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.main_loading_layout) ConstraintLayout mRefreshLoadingLayout;
    @BindView(R.id.main_loading_imageview) ImageView mRefreshLoadingImageView;
    @BindView(R.id.main_no_movies_text_layout) ConstraintLayout mNoMoviesTextLayout;


    Bundle currInstanceState;

    static final String STATE_SORT_TYPE = "sortType";
    static final String STATE_SCROLL_POSITION = "scrollPos";
    static final String STATE_SORT_INDEX = "sortIndex";

    //Declare the anim variable
    Animation anim;

    //Prepare RecyclerView variables
    private MovieAdapter mAdapter;
    MovieAdapter.MovieClickListener movieClickListener;
    GridLayoutManager mLayoutManager;
    EndlessScrollListener mScrollListener;

    //Constant to save and restore URL being queried by the Loader
    private static final String SEARCH_QUERY_URL = "query";
    //themoviedb Loader ID
    private static final int THEMOVIEDB_LOADER_ID = 22;

    //Get access to utility class
    NetworkUtils networkUtils = new NetworkUtils();
    //get access to the MovieAPIManager class
    MovieAPIManager movieAPIManager = new MovieAPIManager();

    //Set default sort
    String currListSort = MovieAPIManager.POPULARITY;

    //Menu sort selection Spinner
    Spinner sortSpinner;

    //Create array for storing the movies
    ArrayList<RoomMovieObject> movieArray = new ArrayList<>();
    //Create array for storing the favorite movies
    ArrayList<RoomMovieObject> favoriteArray = new ArrayList<>();

    //Current page
    private int currPage = 0;

    //Load the Database
    AppDatabase favoriteDb;

    //boolean to check if it's the first start for the spinner misfire on starting
    boolean spinnerMisfiring = true;
    //boolean to check if it's the first start for the LiveDate misfire on starting
    boolean liveDateMisfiring = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Set the anim variable to my custom loading animation
        anim = AnimationUtils.loadAnimation(this, R.anim.loading_animation);

        //Set up RecyclerView
        mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        movieClickListener = this;
        mAdapter = new MovieAdapter(this, movieArray, movieClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            protected void addItems() {
                //When more items are needed, (if the user's not looking at the favorites,) load next page
                if(!(currListSort.equals(getString(R.string.favorite_key)))){
                    loadNextPage();
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        //Hide the "There's no results" text
        mNoMoviesTextLayout.setVisibility(View.INVISIBLE);

        //Set the room variable to the database
        favoriteDb = FavoritesDBSingleton.getInstance(this);


        restoreState(savedInstanceState);

        //Load the movies
        refreshMovies();

        loadLiveFavorites();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Get the sorting Spinner
        MenuItem item = menu.findItem(R.id.main_menu_spinner);
        sortSpinner = (Spinner)item.getActionView();

        //Create an ArrayAdapter for the spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.main_menu_sorting_spinner_array,
                R.layout.custom_spinner_text);
        //Set the Spinner's dropdown text to R.layout.custom_spinner_text
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_text);
        //Set the Spinner's adapter
        sortSpinner.setAdapter(spinnerAdapter);

        //Add click functionality to the Spinner
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Check if the function is running just because of the activity being created
                if(spinnerMisfiring){
                    spinnerMisfiring = false;
                    return;
                }
                //Reset the Adapter dataset(For a nicer loading look)
                mAdapter.updateList(new ArrayList<RoomMovieObject>());
                //Get the text from the item selected
                String itemText = sortSpinner.getSelectedItem().toString();
                //Get all of the text in the Spinner
                String[] possibleItems = getResources().getStringArray(R.array.main_menu_sorting_spinner_array);
                //Sort through each for the sort type and call the load function
                if (itemText.equals(possibleItems[0])) {
                    loadPopularMovies();
                } else if (itemText.equals(possibleItems[1])) {
                    loadTopRatedMovies();
                } else if (itemText.equals(possibleItems[2])){
                    loadFavoriteMovies();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(currInstanceState != null){
            sortSpinner.setSelection(currInstanceState.getInt(STATE_SORT_INDEX));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Get the toolbar item pressed
        int itemId = item.getItemId();
        //If it's the refresh button, call the reload function
        if(itemId == R.id.main_menu_refresh){
            refreshMovies();
        }
        return true;
    }

    //Function refreshes the list
    public void refreshMovies(){
        //Start the loading animation
        startRefreshLoadAnimation();
        //Reset the movies, and load some new ones
        currPage = 0;
        //mAdapter.resetList();
        movieArray.clear();
        mScrollListener.resetScroll();
        if(currListSort.equals(getString(R.string.favorite_key))){
            loadFavoriteMovies();
        }else{
            loadNextPage();
        }
    }

    public void startRefreshLoadAnimation(){
        //Make the loading layout visible
        mRefreshLoadingLayout.setVisibility(View.VISIBLE);
        //Start the animation
        mRefreshLoadingImageView.startAnimation(anim);
    }

    public void stopRefreshLoadAnimation(){
        //Stop the animation
        mRefreshLoadingImageView.clearAnimation();
        //Make the loading layout invisible
        mRefreshLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onMovieClick(int clickedItemIndex) {
        //Create a new Intent to move to the DetaiActivity
        Intent detailActivityIntent = new Intent(this, DetailActivity.class);
        //putExtra the movie selected
        if(currListSort.equals(getString(R.string.favorite_key))){
            detailActivityIntent.putExtra(getString(R.string.detail_intent_tag), favoriteArray.get(clickedItemIndex));
        }else{
            detailActivityIntent.putExtra(getString(R.string.detail_intent_tag), movieArray.get(clickedItemIndex));
        }

        //Start the Activity
        startActivity(detailActivityIntent);
    }

    //Function sets the sort to popular movies, and refreshes the movies
    private void loadPopularMovies(){
        //Set the sort to Popularity
        currListSort = MovieAPIManager.POPULARITY;
        //Refresh the movies
        refreshMovies();
    }

    //Function sets the sort to top rated movies, and refreshes the movies
    private void loadTopRatedMovies(){
        //Set the sort to Rating
        currListSort = MovieAPIManager.RATING;
        //Refresh the movies
        refreshMovies();
    }

    //Function sets the sort to favorite movies, and refreshes the movies
    private void loadFavoriteMovies(){
        //Set the sort to Rating
        currListSort = getString(R.string.favorite_key);
        //Refresh the movies
        //refreshMovies();
        mAdapter.updateList(favoriteArray);
        stopRefreshLoadAnimation();
        mNoMoviesTextLayout.setVisibility(View.INVISIBLE);
    }

    private void loadNextPage(){
        //Increment the page variable
        currPage++;
        //Get the URL
        ArrayList<String> movieSearchList = new ArrayList<>();
        //Get the next page
        movieSearchList.add(movieAPIManager.getPathToMoviePage(currListSort, currPage));
        //Create a Bundle for the Loader
        Bundle queryBundle = new Bundle();
        //Insert a String
        queryBundle.putStringArrayList(SEARCH_QUERY_URL, movieSearchList);

        //Get the LoaderManager
        LoaderManager loaderManager = getSupportLoaderManager();
        //Get the Loader by ID
        android.support.v4.content.Loader<ArrayList<RoomMovieObject>> movieLoader = loaderManager.getLoader(THEMOVIEDB_LOADER_ID);
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
    public android.support.v4.content.Loader<ArrayList<RoomMovieObject>> onCreateLoader(int i, final Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<ArrayList<RoomMovieObject>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    this.stopLoading();
                }
                mNoMoviesTextLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public ArrayList<RoomMovieObject> loadInBackground() {
                //If the currListSort is Favorite Movies,
                if(currListSort.equals(getString(R.string.favorite_key))){

                    //LiveData
                    LiveData<List<RoomMovieObject>> liveMovies = favoriteDb.movieDao().getAll();

                    ArrayList<RoomMovieObject> movies = new ArrayList<>();
                    if(liveMovies.getValue() != null){
                        //Create the movies ArrayList and add all the live values
                        movies.addAll(liveMovies.getValue());
                    }

                    //Return the movies
                    return movies;
                }
                //Get the URL to the movies
                ArrayList<String> movieSearchArrayList = args.getStringArrayList(SEARCH_QUERY_URL);
                //Set the movie ArrayList
                ArrayList<RoomMovieObject> movies = new ArrayList<>();
                ArrayList<RoomMovieObject> currPage;
                for(int i=0; i<movieSearchArrayList.size(); i++){
                    try {
                        //Convert the String into a URL
                        URL movieSearchUrl = new URL(movieSearchArrayList.get(i));
                        //Get the movies
                        currPage = networkUtils.getMoviesFromUrl(movieSearchUrl);
                        if(currPage == null){
                            return null;
                        }
                        //Set the movie variable
                        movies.addAll(currPage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                //Return movies
                return movies;
            }

        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<RoomMovieObject>> loader, ArrayList<RoomMovieObject> mMovieList) {
        //If there's no movies in the ArrayList show error message to the user
        if(mMovieList == null || mMovieList.size() == 0 && !(currListSort.equals(getString(R.string.favorite_key)))) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    stopRefreshLoadAnimation();
                    mNoMoviesTextLayout.setVisibility(View.VISIBLE);


                }
            });
            return;
        }
        //Add the new movies to the list
        movieArray.addAll(mMovieList);
        //Update the Adapter's list
        mAdapter.updateList(movieArray);
        //Stop the loading animation
        stopRefreshLoadAnimation();


        RoomMovieObject[] testDbArray = new RoomMovieObject[movieArray.size()];
        for(int i=0; i<movieArray.size(); i++){
            testDbArray[i] = movieArray.get(i);
        }

        //new databaseTest().execute(testDbArray);

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<RoomMovieObject>> loader) {

    }

    private void loadLiveFavorites(){
        //Gets the ViewModel
        FavoritesViewModel favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        //Gets the list of favorites, and adds an Observer
        favoritesViewModel.getFavorites().observe(this, new Observer<List<RoomMovieObject>>() {
            @Override
            public void onChanged(@Nullable List<RoomMovieObject> roomMovieObjects) {
                //When there's a change
                //Clear the favoritesArray
                favoriteArray.clear();
                //Add the new data
                favoriteArray.addAll(roomMovieObjects);
                //And if this isn't the first time this is activated, (Because it activates when it's first created)
                if(!liveDateMisfiring){
                    //Update the adapter with the new data
                    mAdapter.updateList(favoriteArray);
                }else{
                    //Else, update the variable telling me this has run
                    liveDateMisfiring = false;
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(STATE_SORT_TYPE, currListSort);
        outState.putInt(STATE_SCROLL_POSITION, mLayoutManager.findFirstCompletelyVisibleItemPosition());
        outState.putInt(STATE_SORT_INDEX, sortSpinner.getSelectedItemPosition());


        super.onSaveInstanceState(outState);
    }

    private void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            currListSort = savedInstanceState.getString(STATE_SORT_TYPE);
            mRecyclerView.scrollToPosition(savedInstanceState.getInt(STATE_SCROLL_POSITION));
        }
    }
}
