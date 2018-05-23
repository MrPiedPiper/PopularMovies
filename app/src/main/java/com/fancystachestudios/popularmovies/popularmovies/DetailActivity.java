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
 * Created by napuk on 5/22/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_backdrop)ImageView backdropImageView;

    @BindView(R.id.detail_votes)TextView voteCountTextView;
    @BindView(R.id.detail_stars)RatingBar voteAverageRatingBar;
    @BindView(R.id.detail_title)TextView titleTextView;
    @BindView(R.id.detail_poster_imageview)ImageView posterImageView;
    @BindView(R.id.detail_overview)TextView overviewTextView;
    @BindView(R.id.detail_release_date)TextView releaseDateTextView;


    MovieAPIManager movieAPIManager = new MovieAPIManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        MovieObject movie = (MovieObject) intent.getSerializableExtra(getString(R.string.detail_intent_tag));

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
