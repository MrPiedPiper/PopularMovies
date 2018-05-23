package com.fancystachestudios.popularmovies.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main class for the MainActivity
 */

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{

    //Prepare RecyclerView variables
    @BindView(R.id.mainRecyclerView) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    MovieAdapter.MovieClickListener movieClickListener;
    private RecyclerView.LayoutManager mLayoutManager;

    //Get access to utility class
    NetworkUtils networkUtils = new NetworkUtils();

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
        mRecyclerView.setHasFixedSize(true);
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
            new loadMovies().execute();
        }
        return true;
    }

    //Function refreshes the list
    public void refreshMovies(ArrayList<MovieObject> movies){
        //If there's no movies in the ArrayList show error message to the user
        if(movies.size() == 0) {
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
        new loadMovies().execute();
    }

    //Function sets the sort to top rated movies, and refreshes the movies
    private void loadTopRatedMovies(){
        //Set the sort to Rating
        currListSort = MovieAPIManager.RATING;
        //Refresh the movies
        new loadMovies().execute();
    }

    //AsyncTask refreshes the movies
    public class loadMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Get the URL to the movies
            URL movieSearchUrl = null;
            try {
                movieSearchUrl = new URL(MovieAPIManager.PART1 + currListSort + MovieAPIManager.PART3 + MovieAPIManager.KEY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //Set the movie ArrayList
            ArrayList<MovieObject> movies = new ArrayList<>();
            try {
                movies = networkUtils.getMoviesFromUrl(movieSearchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Refresh the movies
            refreshMovies(movies);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
