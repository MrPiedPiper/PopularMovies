package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.TrailerObject;
import com.fancystachestudios.popularmovies.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by napuk on 6/24/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    //Declare variable to hold Dataset
    private ArrayList<TrailerObject> mDataset;
    //Get access to MovieAPIManager
    private MovieAPIManager movieAPIManager = new MovieAPIManager();
    //Get the MovieClickListener
    final private TrailerClickListener mTrailerClickListener;
    //Set the Context variable
    Context currContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Declare the variables to hold the views
        ImageView mTrailerThumbnail;
        TextView mTrailerTitle;

        ViewHolder (View v){
            super(v);
            //Set the views to the variables
            mTrailerThumbnail = v.findViewById(R.id.trailer_thumbnail);
            mTrailerTitle = v.findViewById(R.id.trailer_title_textview);

            //Set the onClickListener
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mTrailerClickListener.onTrailerClick(getAdapterPosition());
        }
    }

    public interface TrailerClickListener{
        void onTrailerClick(int clickedItemIndex);
    }

    public TrailerAdapter(Context context, ArrayList<TrailerObject> myDataset, TrailerClickListener listener){
        //Set the context variable
        currContext = context;
        mDataset = myDataset;
        mTrailerClickListener = listener;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        //Get the current trailer
        TrailerObject currTrailer = mDataset.get(position);
        //Set the views
        holder.mTrailerTitle.setText(currTrailer.getName());
        Picasso.get().load(movieAPIManager.getVideoThumbnailPath(currTrailer.getKey())).into(holder.mTrailerThumbnail);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
