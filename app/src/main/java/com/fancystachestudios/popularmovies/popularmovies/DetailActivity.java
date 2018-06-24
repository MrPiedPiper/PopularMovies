package com.fancystachestudios.popularmovies.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Loader;
import android.media.Image;
import android.support.v4.app.LoaderManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.TrailersObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.AppDatabase;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.MovieDao;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.TableMovieItem;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Table;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main class for the DetailActivity
 */

public class DetailActivity extends AppCompatActivity{

    //Get all of the necessary Views
    @BindView(R.id.detail_backdrop)ImageView backdropImageView;
    @BindView(R.id.detail_votes)TextView voteCountTextView;
    @BindView(R.id.detail_stars)RatingBar voteAverageRatingBar;
    @BindView(R.id.detail_title)TextView titleTextView;
    @BindView(R.id.detail_poster_imageview)ImageView posterImageView;
    @BindView(R.id.detail_overview)TextView overviewTextView;
    @BindView(R.id.detail_release_date)TextView releaseDateTextView;

    @BindView(R.id.detail_trailer_image)ImageView trailerImageView;

    Context currContex;

    //Create the Loader
    private LoaderManager.LoaderCallbacks<TableMovieItem> tableMovieItemLoader;
    //Constant to save the table being queried by the Loader
    private static final String LOADER_IS_FAVORITE = "query";
    //Table loader ID
    private static final int ID_LOADER_FAVORITES = 23;

    //Constant to save the table being queried by the Loader
    private static final String LOADER_TOGGLE_FAVORITE = "favorite toggle";
    //Table loader ID
    private static final int ID_LOADER_TOGGLE_FAVORITE = 24;


    //Create the Trailer Loader
    private LoaderManager.LoaderCallbacks<ArrayList<TrailersObject>> trailerLoader;

    //Constant to save the table being queried by the Loader
    private static final String LOADER_TRAILER_LOAD = "trailer load";
    //Table loader ID
    private static final int ID_LOADER_TRAILER_LOAD = 25;
    //Create the Trailer Arraylist
    ArrayList<TrailersObject> currTrailers;

    //Get access to the movieAPIManager
    private MovieAPIManager movieAPIManager = new MovieAPIManager();

    //Get access to the NetworkUtils
    private NetworkUtils networkUtils = new NetworkUtils();

    private TableMovieItem currMovie;

    private Menu menu;

    private boolean isFavorite = false;

    //Load the Database
    AppDatabase favoriteDb;
    MovieDao movieDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //Set the context variable
        currContex = this;

        //Retrieve the movie selected to open the DetailActivity
        Intent intent = getIntent();
        currMovie = (TableMovieItem) intent.getParcelableExtra(getString(R.string.detail_intent_tag));

        //Set the views to the data from the movie
        setViewsWithMovie(currMovie);

        //Set the trailer thumbnail
        //Picasso.get().load(movieAPIManager.getVideoThumbnailPath(currMovie.))
        Log.d("movieId", String.valueOf(currMovie.getId()));

        //Open up the database
        favoriteDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.favorite_movies_table_name)).build();
        //Get the Dao
        movieDao = favoriteDb.movieDao();

        //Set the tableMovieItemLoader Loader
        tableMovieItemLoader = new LoaderManager.LoaderCallbacks<TableMovieItem>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public android.support.v4.content.Loader<TableMovieItem> onCreateLoader(int id, final Bundle args) {
                Log.d("mytest", "created loader");
                if(id == ID_LOADER_FAVORITES) {
                    return new android.support.v4.content.AsyncTaskLoader<TableMovieItem>(getBaseContext()) {

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();
                            if (args == null) {
                                this.stopLoading();
                            }
                            forceLoad();
                        }

                        @Override
                        public TableMovieItem loadInBackground() {
                            //Load the Database
                            AppDatabase favoriteDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.favorite_movies_table_name)).build();
                            //Get the DAO
                            MovieDao movieDao = favoriteDb.movieDao();
                            Log.d("mytest", String.valueOf(movieDao.findById(currMovie.getId())));

                            return movieDao.findById(currMovie.getId());
                        }
                    };
                }else if(id == ID_LOADER_TOGGLE_FAVORITE){
                    return new android.support.v4.content.AsyncTaskLoader<TableMovieItem>(getBaseContext()){

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();
                            if (args == null) {
                                this.stopLoading();
                            }
                            forceLoad();
                        }

                        @Override
                        public TableMovieItem loadInBackground() {

                            isFavorite = !isFavorite;

                            if(isFavorite){
                                movieDao.insertAll(currMovie);

                                TableMovieItem testMovie = movieDao.findById(currMovie.getId());

                                Log.d("movieTest", String.valueOf(testMovie.getId()));
                            }else{
                                movieDao.delete(movieDao.findById(currMovie.getId()));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isFavorite){
                                        menu.getItem(0).setIcon(R.drawable.ic_favorite_full);
                                    }else{
                                        menu.getItem(0).setIcon(R.drawable.ic_favorite_empty);
                                    }
                                }
                            });
                            Log.d("favorite", String.valueOf(isFavorite));
                            return null;
                        }


                    };
                }else{
                    return null;
                }
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<TableMovieItem> loader, TableMovieItem tableMovieItem) {
                if(loader.getId() == ID_LOADER_FAVORITES){
                    if(tableMovieItem != null){
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_full);
                        isFavorite = true;
                    }else{
                        isFavorite = false;
                    }
                }
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<TableMovieItem> loader) {

            }
        };


        //Set the tableMovieItemLoader Loader
        trailerLoader = new LoaderManager.LoaderCallbacks<ArrayList<TrailersObject>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public android.support.v4.content.Loader<ArrayList<TrailersObject>> onCreateLoader(int id, final Bundle args) {
                return new android.support.v4.content.AsyncTaskLoader<ArrayList<TrailersObject>>(getBaseContext()){

                        @Override
                        protected void onStartLoading() {
                            Log.d("Loadertest", "onStartLoading");
                            super.onStartLoading();
                            forceLoad();
                        }

                        @Override
                        public ArrayList<TrailersObject> loadInBackground() {
                            Log.d("Loadertest", "loadInBackground");
                            ArrayList<TrailersObject> retrievedTrailers = null;
                            try {
                                URL trailerUrl = new URL(movieAPIManager.getTrailerListPath(currMovie.getId()));
                                retrievedTrailers = networkUtils.getTrailersFromUrl(trailerUrl);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return retrievedTrailers;

                        }


                    };
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<ArrayList<TrailersObject>> loader, ArrayList<TrailersObject> trailersObjects) {
                if(trailersObjects != null){
                    currTrailers = trailersObjects;
                    Picasso.get().load(movieAPIManager.getVideoThumbnailPath(currTrailers.get(0).getKey())).into(trailerImageView);
                }
                Log.d("LoaderTest", "trailerobjects is " + (trailersObjects == null) + " null");
                Log.d("LoaderTest", movieAPIManager.getVideoThumbnailPath(currTrailers.get(0).getKey()));
                Log.d("Loadertest", "onLoadFinished");
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<ArrayList<TrailersObject>> loader) {

            }
        };

        //Get the LoaderManager
        LoaderManager loaderManager = getSupportLoaderManager();
        //Get the trailer Loader by ID
        android.support.v4.content.Loader<ArrayList<TrailersObject>> roomLoader = loaderManager.getLoader(ID_LOADER_TRAILER_LOAD);
        //If there's  no Loader,
        if(roomLoader == null){
            //Create it
            loaderManager.initLoader(ID_LOADER_TRAILER_LOAD, null, trailerLoader);
        }else{
            //Otherwise, restart it
            loaderManager.destroyLoader(ID_LOADER_TRAILER_LOAD);
            loaderManager.initLoader(ID_LOADER_TRAILER_LOAD, null, trailerLoader);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        this.menu = menu;

        //Inflate the detail_menu
        getMenuInflater().inflate(R.menu.detail_menu, menu);


        //Create a Bundle for the Loader
        Bundle bundle = new Bundle();
        //Insert the current movie
        bundle.putParcelable(LOADER_IS_FAVORITE, currMovie);

        //Get the LoaderManager
        LoaderManager loaderManager = getSupportLoaderManager();
        //Get the Loader by ID
        android.support.v4.content.Loader<TableMovieItem> roomLoader = loaderManager.getLoader(ID_LOADER_FAVORITES);
        //If there's  no Loader,
        if(roomLoader == null){
            //Create it
            loaderManager.initLoader(ID_LOADER_FAVORITES, bundle, tableMovieItemLoader);
        }else{
            //Otherwise, restart it
            loaderManager.destroyLoader(ID_LOADER_FAVORITES);
            loaderManager.initLoader(ID_LOADER_FAVORITES, bundle, tableMovieItemLoader);
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.detail_favorite_button:
                //Add to favorites

                //Create a Bundle for the Loader
                Bundle bundle = new Bundle();
                //Insert the current movie
                bundle.putParcelable(LOADER_TOGGLE_FAVORITE, currMovie);

                //Get the LoaderManager
                LoaderManager loaderManager = getSupportLoaderManager();
                //Get the Loader by ID
                android.support.v4.content.Loader<TableMovieItem> roomLoader = loaderManager.getLoader(ID_LOADER_TOGGLE_FAVORITE);
                //If there's  no Loader,
                if (roomLoader == null) {
                    //Create it
                    loaderManager.initLoader(ID_LOADER_TOGGLE_FAVORITE, bundle, tableMovieItemLoader);
                } else {
                    //Otherwise, restart it
                    loaderManager.restartLoader(ID_LOADER_TOGGLE_FAVORITE, bundle, tableMovieItemLoader);
                }
                break;
        }
        super.onOptionsItemSelected(item);

        return true;
    }

    //Function sets the attributes of the Views based on the MovieObject
    private void setViewsWithMovie(TableMovieItem movie){
        //Load all of the data from the movie object and set View data accordingly
        Picasso.get().load(movieAPIManager.getBackdropPath(movie)).into(backdropImageView);
        voteCountTextView.setText(String.valueOf(movie.getVoteCount()));
        voteAverageRatingBar.setRating((float)movie.getVoteAverage()/2);
        titleTextView.setText(movie.getTitle());
        Picasso.get().load(movieAPIManager.getPosterPath(movie)).into(posterImageView);
        overviewTextView.setText(movie.getOverview());
        releaseDateTextView.setText(movie.getReleaseDate());
    }
}
