package com.fancystachestudios.popularmovies.popularmovies;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.ReviewObject;
import com.fancystachestudios.popularmovies.popularmovies.MovieDBFavorites.TableMovieItem;
import com.fancystachestudios.popularmovies.popularmovies.Utils.EndlessScrollListener;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;
import com.fancystachestudios.popularmovies.popularmovies.Utils.ReviewAdapter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllReviewsActivity extends AppCompatActivity
        implements ReviewAdapter.ReviewClickListener{

    @BindView(R.id.custom_recyclerview)RecyclerView mRecyclerView;

    private ArrayList<ReviewObject> allReviews = new ArrayList<>();

    private TableMovieItem currMovie;
    private EndlessScrollListener mScrollListener;
    private int currPage = 0;
    ReviewAdapter.ReviewClickListener mClickListener;

    RecyclerView.Adapter mAdapter;

    private MovieAPIManager movieAPIManager = new MovieAPIManager();
    private NetworkUtils networkUtils = new NetworkUtils();


    private static final int ID_LOADER_REVIEW_LOAD = 22;
    LoaderManager.LoaderCallbacks<ArrayList<ReviewObject>> reviewLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customized_recyclerview);
        ButterKnife.bind(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        currMovie = (TableMovieItem) getIntent().getParcelableExtra(getString(R.string.detail_all_reviews_intent_extra_key));

        mClickListener = this;

        mAdapter = new ReviewAdapter(this, allReviews, mClickListener);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            protected void addItems() {
                loadNextPage();
            }
        };

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mScrollListener);

        loadNextPage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNextPage(){
        Log.d("log", "page " + currPage);
        currPage++;
        Log.d("log", "page " + currPage);

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
                        Log.d("test", "onStartLoading");
                        super.onStartLoading();
                        forceLoad();
                    }

                    @Override
                    public ArrayList<ReviewObject> loadInBackground() {
                        ArrayList<ReviewObject> retrievedReviews = new ArrayList<>();
                        try{
                            URL reviewUrl = new URL(movieAPIManager.getReviewPath(currMovie.getId(), currPage));
                            Log.d("reviewPath", reviewUrl.toString());
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
                    Log.d("test", "Finished loading");
                    Log.d("test", "List is " + reviewCount + " long");

                    allReviews.addAll(reviewObjects);
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

    @Override
    public void onReviewClick(int clickedItemIndex) {

    }
}
