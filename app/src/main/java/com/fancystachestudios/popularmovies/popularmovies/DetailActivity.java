package com.fancystachestudios.popularmovies.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main class for the DetailActivity
 */

public class DetailActivity extends AppCompatActivity {

    //Get all of the necessary Views
    @BindView(R.id.detail_backdrop)ImageView backdropImageView;
    @BindView(R.id.detail_votes)TextView voteCountTextView;
    @BindView(R.id.detail_stars)RatingBar voteAverageRatingBar;
    @BindView(R.id.detail_title)TextView titleTextView;
    @BindView(R.id.detail_poster_imageview)ImageView posterImageView;
    @BindView(R.id.detail_overview)TextView overviewTextView;
    @BindView(R.id.detail_release_date)TextView releaseDateTextView;

    //Get access to the movieAPIManager
    private MovieAPIManager movieAPIManager = new MovieAPIManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //Retrieve the movie selected to open the DetailActivity
        Intent intent = getIntent();
        MovieObject movie = (MovieObject) intent.getSerializableExtra(getString(R.string.detail_intent_tag));

        //Set the views to the data from the movie
        setViewsWithMovie(movie);
    }

    //Function sets the attributes of the Views based on the MovieObject
    private void setViewsWithMovie(MovieObject movie){
        //Load all of the data from the movie object and set View data accordingly
        Picasso.get().load(movieAPIManager.getBackdropPath(movie)).into(backdropImageView);
        Log.d("myBackdropTest", movieAPIManager.getBackdropPath(movie));
        voteCountTextView.setText(String.valueOf(movie.getVoteCount()));
        voteAverageRatingBar.setRating(movie.getVoteAverage()/2);
        titleTextView.setText(movie.getTitle());
        Picasso.get().load(movieAPIManager.getPosterPath(movie)).into(posterImageView);
        overviewTextView.setText(movie.getOverview());
        releaseDateTextView.setText(movie.getReleaseDate());
    }
}
