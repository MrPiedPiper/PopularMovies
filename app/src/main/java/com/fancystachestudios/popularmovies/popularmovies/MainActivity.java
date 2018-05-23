package com.fancystachestudios.popularmovies.popularmovies;

import android.content.Intent;
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

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.CustomMovieList;
import com.fancystachestudios.popularmovies.popularmovies.Utils.MovieAdapter;
import com.fancystachestudios.popularmovies.popularmovies.Utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{
    @BindView(R.id.mainRecyclerView) RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    NetworkUtils networkUtils = new NetworkUtils();
    CustomMovieList customMovieList = new CustomMovieList();
    MovieAPIManager movieAPIManager = new MovieAPIManager();

    String currListSort = movieAPIManager.POPULARITY;

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

        loadPopularMovies();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.main_menu_spinner);
        final Spinner spinner = (Spinner)item.getActionView();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.main_menu_sorting_spinner_array,
                R.layout.custom_spinner_text);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_text);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemText = spinner.getSelectedItem().toString();
                String[] possibleItems = getResources().getStringArray(R.array.main_menu_sorting_spinner_array);
                if (itemText.equals(possibleItems[0])) {
                    Log.d("mytest", "selected " + itemText);
                    loadPopularMovies();
                } else if (itemText.equals(possibleItems[1])) {
                    Log.d("mytest", "selected " + itemText);
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
        int itemId = item.getItemId();
        if(itemId == R.id.main_menu_refresh){
            new loadMovies().execute();
        }
        return true;
    }

    public void refreshMovies(ArrayList<MovieObject> movies){
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

    private void loadPopularMovies(){
        currListSort = MovieAPIManager.POPULARITY;
        new loadMovies().execute();
    }

    private void loadTopRatedMovies(){
        currListSort = MovieAPIManager.RATING;
        new loadMovies().execute();
    }

    public class loadMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            URL movieSearchUrl = null;
            try {
                movieSearchUrl = new URL(MovieAPIManager.PART1 + currListSort + MovieAPIManager.PART3 + MovieAPIManager.KEY);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ArrayList<MovieObject> movies = new ArrayList<MovieObject>();
            try {
                movies = networkUtils.getMoviesFromUrl(movieSearchUrl);
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

}
