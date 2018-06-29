package com.fancystachestudios.popularmovies.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.ReviewObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.TrailerObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.AppDatabase;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.MovieDao;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.RoomMovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
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

    @BindView(R.id.trailer_thumbnail)ImageView trailerThumbnailImageView;
    @BindView(R.id.trailer_title_textview)TextView trailerTitleTextView;
    @BindView(R.id.detail_more_trailers)Button trailerMoreTrailersButton;
    @BindView(R.id.detail_video_layout)ConstraintLayout videoLayoutConstraint;

    @BindView(R.id.detail_reviews_button)Button allReviewsButton;

    Context currContext;

    Toast generalUseToast;


    //Create the LoaderManager variable
    LoaderManager loaderManager;


    //Create the Loader
    private LoaderManager.LoaderCallbacks<RoomMovieObject> tableMovieItemLoader;
    //Constant to save the table being queried by the Loader
    private static final String LOADER_IS_FAVORITE = "query";
    //Table loader ID
    private static final int ID_LOADER_FAVORITES = 23;

    //Constant to save the table being queried by the Loader
    private static final String LOADER_TOGGLE_FAVORITE = "favorite toggle";
    //Table loader ID
    private static final int ID_LOADER_TOGGLE_FAVORITE = 24;

    //Table loader ID
    private static final int ID_LOADER_TRAILER_LOAD = 25;
    //Create the Trailer Arraylist
    ArrayList<TrailerObject> allTrailers;
    //Create the current Trailer
    TrailerObject currTrailer;


    private static final int ID_LOADER_REVIEW_LOAD = 26;
    LoaderManager.LoaderCallbacks<ArrayList<ReviewObject>> reviewLoader;


    //Get access to the movieAPIManager
    private MovieAPIManager movieAPIManager = new MovieAPIManager();

    //Get access to the NetworkUtils
    private NetworkUtils networkUtils = new NetworkUtils();

    private RoomMovieObject currMovie;

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
        currContext = this;

        //Retrieve the movie selected to open the DetailActivity
        Intent intent = getIntent();
        currMovie = (RoomMovieObject) intent.getParcelableExtra(getString(R.string.detail_intent_tag));

        trailerMoreTrailersButton.setVisibility(View.GONE);
        videoLayoutConstraint.setVisibility(View.GONE);
        allReviewsButton.setVisibility(View.GONE);

        //Set the views to the data from the movie
        setViewsWithMovie(currMovie);

        //Open up the database
        favoriteDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.favorite_movies_table_name)).build();
        //Get the Dao
        movieDao = favoriteDb.movieDao();

        //Set the LoaderManager variable
        loaderManager = getSupportLoaderManager();

        //Set the tableMovieItemLoader Loader
        tableMovieItemLoader = new LoaderManager.LoaderCallbacks<RoomMovieObject>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public android.support.v4.content.Loader<RoomMovieObject> onCreateLoader(int id, final Bundle args) {
                Log.d("mytest", "created loader");
                if(id == ID_LOADER_FAVORITES) {
                    return new android.support.v4.content.AsyncTaskLoader<RoomMovieObject>(getBaseContext()) {

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();
                            if (args == null) {
                                this.stopLoading();
                            }
                            forceLoad();
                        }

                        @Override
                        public RoomMovieObject loadInBackground() {
                            //Load the Database
                            AppDatabase favoriteDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, getString(R.string.favorite_movies_table_name)).build();
                            //Get the DAO
                            MovieDao movieDao = favoriteDb.movieDao();
                            Log.d("mytest", String.valueOf(movieDao.findById(currMovie.getId())));

                            return movieDao.findById(currMovie.getId());
                        }
                    };
                }else if(id == ID_LOADER_TOGGLE_FAVORITE){
                    return new android.support.v4.content.AsyncTaskLoader<RoomMovieObject>(getBaseContext()){

                        @Override
                        protected void onStartLoading() {
                            super.onStartLoading();
                            if (args == null) {
                                this.stopLoading();
                            }
                            forceLoad();
                        }

                        @Override
                        public RoomMovieObject loadInBackground() {

                            isFavorite = !isFavorite;

                            if(isFavorite){
                                movieDao.insertAll(currMovie);

                                RoomMovieObject testMovie = movieDao.findById(currMovie.getId());

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
            public void onLoadFinished(android.support.v4.content.Loader<RoomMovieObject> loader, RoomMovieObject roomMovieObject) {
                if(loader.getId() == ID_LOADER_FAVORITES){
                    if(roomMovieObject != null){
                        menu.getItem(0).setIcon(R.drawable.ic_favorite_full);
                        isFavorite = true;
                    }else{
                        isFavorite = false;
                    }
                }
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<RoomMovieObject> loader) {

            }
        };

        trailerThumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playTrailer();
            }
        });

        setTrailerViews();

        getReviews();

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

        //Get the Loader by ID
        android.support.v4.content.Loader<RoomMovieObject> roomLoader = loaderManager.getLoader(ID_LOADER_FAVORITES);
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

                //Get the Loader by ID
                android.support.v4.content.Loader<RoomMovieObject> roomLoader = loaderManager.getLoader(ID_LOADER_TOGGLE_FAVORITE);
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

    //Function sets the attributes of the Views based on the GsonMovieObject
    private void setViewsWithMovie(RoomMovieObject movie){
        //Load all of the data from the movie object and set View data accordingly
        Picasso.get().load(movieAPIManager.getBackdropPath(movie)).into(backdropImageView);
        voteCountTextView.setText(String.valueOf(movie.getVoteCount()));
        voteAverageRatingBar.setRating((float)movie.getVoteAverage()/2);
        titleTextView.setText(movie.getTitle());
        Picasso.get().load(movieAPIManager.getPosterPath(movie)).into(posterImageView);
        overviewTextView.setText(movie.getOverview());
        releaseDateTextView.setText(movie.getReleaseDate());
    }

    //Function sets the attributes of the trailer Views based on trailer data
    private void setTrailerViews(){

        //Create and set the tableMovieItemLoader Loader
        LoaderManager.LoaderCallbacks<ArrayList<TrailerObject>> trailerLoader = new LoaderManager.LoaderCallbacks<ArrayList<TrailerObject>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public android.support.v4.content.Loader<ArrayList<TrailerObject>> onCreateLoader(int id, final Bundle args) {
                return new android.support.v4.content.AsyncTaskLoader<ArrayList<TrailerObject>>(getBaseContext()) {

                    @Override
                    protected void onStartLoading() {
                        Log.d("Loadertest", "onStartLoading");
                        super.onStartLoading();
                        forceLoad();
                    }

                    @Override
                    public ArrayList<TrailerObject> loadInBackground()  {
                        Log.d("Loadertest", "loadInBackground");
                        ArrayList<TrailerObject> retrievedTrailers = null;
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
            public void onLoadFinished(android.support.v4.content.Loader<ArrayList<TrailerObject>> loader, ArrayList<TrailerObject> trailerObjects) {
                if(trailerObjects != null && trailerObjects.size() > 0){
                    videoLayoutConstraint.setVisibility(View.VISIBLE);
                    //Set the curTrailers to the trailerObjects
                    allTrailers = trailerObjects;
                    //Loop through all available trailers
                    for (TrailerObject currTrailer : trailerObjects) {
                        //If one is of the type "Trailer",
                        if (currTrailer.getType().equals("Trailer")) {
                            //Set the Trailer to that and escape the loop
                            setViewsWithTrailer(currTrailer);
                            break;
                        }
                    }
                    //If one isn't found, set the trailer to the first available trailer
                    if (trailerTitleTextView.getText() == "") {
                        setViewsWithTrailer(trailerObjects.get(0));
                    }
                }
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<ArrayList<TrailerObject>> loader) {

            }
        };

        //Get the trailer Loader by ID
        android.support.v4.content.Loader<ArrayList<TrailerObject>> roomLoader = loaderManager.getLoader(ID_LOADER_TRAILER_LOAD);
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

    private void setViewsWithTrailer(TrailerObject trailer){
        currTrailer = trailer;

        Picasso.get().load(movieAPIManager.getVideoThumbnailPath(trailer.getKey())).into(trailerThumbnailImageView);

        trailerTitleTextView.setText(trailer.getName());

        if(allTrailers.size() > 1){
            trailerMoreTrailersButton.setVisibility(View.VISIBLE);
            Resources res = getResources();
            trailerMoreTrailersButton.setText(res.getQuantityString(R.plurals.detail_more_trailers, allTrailers.size()-1, allTrailers.size()-1));
        }
    }

    public void playTrailer(){
        String youtubeAddress = movieAPIManager.getYoutubeFromKey(currTrailer.getKey());
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeAddress));
        if(youtubeIntent.resolveActivity(getPackageManager()) != null){
            startActivity(youtubeIntent);
        }else{
            if(generalUseToast != null) generalUseToast.cancel();
            generalUseToast.makeText(this, getResources().getString(R.string.detail_no_app), Toast.LENGTH_LONG).show();
        }
    }

    public void moreTrailersOnClick(View view){
        Intent moreTrailersIntent = new Intent(this, MoreTrailersActivity.class);
        moreTrailersIntent.putExtra(getString(R.string.detail_more_trailers_intent_extra_key), allTrailers);
        startActivity(moreTrailersIntent);
    }

    //Function sets the attributes of the trailer Views based on trailer data
    private void getReviews(){

        //Create and set the tableMovieItemLoader Loader
        reviewLoader = new LoaderManager.LoaderCallbacks<ArrayList<ReviewObject>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public android.support.v4.content.Loader<ArrayList<ReviewObject>> onCreateLoader(int id, final Bundle bundle) {
                return new android.support.v4.content.AsyncTaskLoader<ArrayList<ReviewObject>>(getBaseContext()) {

                    @Override
                    protected void onStartLoading() {
                        super.onStartLoading();
                        forceLoad();
                    }

                    @Override
                    public ArrayList<ReviewObject> loadInBackground() {
                        ArrayList<ReviewObject> retrievedReviews = new ArrayList<>();
                        try{
                            URL reviewUrl = new URL(movieAPIManager.getReviewPath(currMovie.getId(), 1));
                            retrievedReviews = networkUtils.getReviewsFromUrl(reviewUrl);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return retrievedReviews;
                    }
                };
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<ArrayList<ReviewObject>> loader, ArrayList<ReviewObject> reviewObjects) {
                if(reviewObjects != null && reviewObjects.size() > 0){
                    int reviewCount = reviewObjects.size();
                    //I need to implement the endlessscrolllistener :<
                    Log.d("test", "List is " + reviewCount + " long");

                    allReviewsButton.setVisibility(View.VISIBLE);
                    allReviewsButton.setText(getResources().getQuantityString(R.plurals.detail_all_reviews_button, reviewCount, reviewCount));
                }
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<ArrayList<ReviewObject>> loader) {

            }
        };

        //Get the trailer Loader by ID
        android.support.v4.content.Loader<ArrayList<ReviewObject>> roomLoader = loaderManager.getLoader(ID_LOADER_REVIEW_LOAD);
        //If there's  no Loader,
        if(roomLoader == null){
            //Create it
            loaderManager.initLoader(ID_LOADER_REVIEW_LOAD, null, reviewLoader);
        }else{
            //Otherwise, restart it
            loaderManager.destroyLoader(ID_LOADER_REVIEW_LOAD);
            loaderManager.initLoader(ID_LOADER_REVIEW_LOAD, null, reviewLoader);
        }
    }

    public void allReviewsOnClick(View view){
        Intent allReviewsIntent = new Intent(this, AllReviewsActivity.class);
        allReviewsIntent.putExtra(getString(R.string.detail_all_reviews_intent_extra_key), currMovie);
        startActivity(allReviewsIntent);
    }
}
