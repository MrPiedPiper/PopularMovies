package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * MainActivity RecyclerView Adapter
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //Declare variable to hold Dataset
    private ArrayList<MovieObject> mDataset;
    //Get access to MovieAPIManager
    private MovieAPIManager movieAPIManager = new MovieAPIManager();
    //Get the MovieClickListener
    final private MovieClickListener mMovieClickListener;

    //Create the ViewHolder for the RecyclerView (With an OnClickListener)
    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        //Declare variables to hold the Views
        TextView mListNumberTextView;
        ConstraintLayout mLayout;
        ImageView mImageView;
        ViewHolder (View v){
            super(v);
            //Set the Views to the variables
            mLayout = v.findViewById(R.id.item_root_layout);
            mListNumberTextView = v.findViewById(R.id.item_list_number_text);
            mImageView = v.findViewById(R.id.item_background_image);
            //Set the OnClickListener
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Get the index of the item clicked
            int clickedPosition = getAdapterPosition();
            //Call the mMovieClickListener's onMovieClick passing the index
            mMovieClickListener.onMovieClick(clickedPosition);
        }
    }

    public interface MovieClickListener{
        void onMovieClick(int clickedItemIndex);
    }

    public MovieAdapter(ArrayList<MovieObject> myDataSet, MovieClickListener listener){
        //Set the Dataset to the provided ArrayList
        mDataset = myDataSet;
        //Set the MovieclickListener to the provided Listener
        mMovieClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the item_movie layout
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        //Create the vh ViewHolder
        ViewHolder vh = new ViewHolder(v);

        //Return vh
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Get the poster path
        String posterPath = movieAPIManager.getPosterPath(mDataset.get(position));
        //Use Picasso to load the image into the ImageView
        Picasso.get().load(posterPath).into(holder.mImageView);
        //Set the mListNumberTextView to position +1
        holder.mListNumberTextView.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        //Return the Dataset's size
        return mDataset.size();
    }

    //I can't call this for some reason. :/
    public void updateList(ArrayList<MovieObject> newMovies){
        mDataset = newMovies;
        notifyDataSetChanged();
    }


}
