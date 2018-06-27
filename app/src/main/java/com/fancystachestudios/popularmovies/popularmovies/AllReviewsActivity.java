package com.fancystachestudios.popularmovies.popularmovies;

import android.annotation.SuppressLint;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.ReviewObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.RoomMovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.EndlessScrollListener;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;
import com.fancystachestudios.popularmovies.popularmovies.Utils.ReviewAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity shows that displays the reviews
 */

public class AllReviewsActivity extends AppCompatActivity{

    //Get the RecyclerView
    @BindView(R.id.custom_recyclerview)RecyclerView mRecyclerView;

    //Array with all the reviews
    private ArrayList<ReviewObject> allReviews = new ArrayList<>();

    //Current movie
    private RoomMovieObject currMovie;

    //EndlessScrollListener
    private EndlessScrollListener mScrollListener;
    //Current page (of reviews)
    private int currPageNum = 0;

    //Adapter for the RecyclerView
    RecyclerView.Adapter mAdapter;

    //Variables to access the MovieAPIManager and NetworkUtils
    private MovieAPIManager movieAPIManager = new MovieAPIManager();
    private NetworkUtils networkUtils = new NetworkUtils();

    //Review Loader ID
    private static final int ID_LOADER_REVIEW_LOAD = 22;
    //Review LoaderCallbacks
    LoaderManager.LoaderCallbacks<ArrayList<ReviewObject>> reviewLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customized_recyclerview);
        ButterKnife.bind(this);

        //Set the current movie
        currMovie = (RoomMovieObject) getIntent().getParcelableExtra(getString(R.string.detail_all_reviews_intent_extra_key));

        //Set up the RecyclerView Variables
        mAdapter = new ReviewAdapter(this, allReviews);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            protected void addItems() {
                loadNextPage();
            }
        };

        //Set the RecyclerView properties
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mScrollListener);

        //Load the first page
        loadNextPage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //If it's the home button
            case android.R.id.home:
                //Simulate the Back button
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNextPage(){
        //Increment the page number
        currPageNum++;
        //Activate Loader
        getReviews();
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
                        //Create a variable to (hopefully) store the retrieved reviews in
                        ArrayList<ReviewObject> retrievedReviews = new ArrayList<>();
                        try{
                            //Get the URL to the Reviews
                            URL reviewUrl = new URL(movieAPIManager.getReviewPath(currMovie.getId(), currPageNum));
                            //Get the reviews from the URL
                            retrievedReviews = networkUtils.getReviewsFromUrl(reviewUrl);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Return the reviews
                        return retrievedReviews;
                    }
                };
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<ArrayList<ReviewObject>> loader, ArrayList<ReviewObject> reviewObjects) {
                //If there's actually reviews,
                if(reviewObjects != null && reviewObjects.size() > 0){
                    //Add the reviews to the allReviews variable
                    allReviews.addAll(reviewObjects);
                    //Notify the Adapter that the dataset has changed
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<ArrayList<ReviewObject>> loader) {

            }
        };

        //Get the LoaderManager
        LoaderManager loaderManager = getSupportLoaderManager();
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

}
