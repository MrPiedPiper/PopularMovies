package com.fancystachestudios.popularmovies.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.TrailerObject;
import com.fancystachestudios.popularmovies.popularmovies.Utils.TrailerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreTrailersActivity extends AppCompatActivity
implements TrailerAdapter.TrailerClickListener{

    @BindView(R.id.custom_recyclerview)RecyclerView mRecyclerView;

    ArrayList<TrailerObject> allTrailers;

    Toast generalUseToast;

    MovieAPIManager movieAPIManager = new MovieAPIManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customized_recyclerview);
        ButterKnife.bind(this);


        //Get the trailers (Sent over from the DetailActivity
        allTrailers = (ArrayList<TrailerObject>) getIntent().getSerializableExtra(getString(R.string.detail_more_trailers_intent_extra_key));


        //Create variables for the RecyclerView properties
        TrailerAdapter trailerAdapter = new TrailerAdapter(this, allTrailers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        //Set the RecyclerView properties
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(trailerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //If it's the Home button
            case android.R.id.home:
                //Simulate the Back button
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTrailerClick(int clickedItemIndex) {
        //Get the link to the YouTube video
        String youtubeAddress = movieAPIManager.getYoutubeFromKey(allTrailers.get(clickedItemIndex).getKey());
        //Create a new ACTION_VIEW Intent to the YouTube video
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeAddress));
        if(youtubeIntent.resolveActivity(getPackageManager()) != null){
            //Start the Activity
            startActivity(youtubeIntent);
        }else{
            if(generalUseToast != null) generalUseToast.cancel();
            generalUseToast.makeText(this, getResources().getString(R.string.detail_no_app), Toast.LENGTH_LONG).show();
        }
    }

}
