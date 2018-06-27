package com.fancystachestudios.popularmovies.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

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

    MovieAPIManager movieAPIManager = new MovieAPIManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customized_recyclerview);
        ButterKnife.bind(this);

        allTrailers = (ArrayList<TrailerObject>) getIntent().getSerializableExtra(getString(R.string.detail_more_trailers_intent_extra_key));

        TrailerAdapter trailerAdapter = new TrailerAdapter(this, allTrailers, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(trailerAdapter);
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

    @Override
    public void onTrailerClick(int clickedItemIndex) {
        String youtubeAddress = movieAPIManager.getYoutubeFromKey(allTrailers.get(clickedItemIndex).getKey());
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeAddress));
        startActivity(youtubeIntent);
    }

}
