package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by napuk on 5/20/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<MovieObject> mDataset;
    private MovieAPIManager movieAPIManager = new MovieAPIManager();

    final private MovieClickListener mMovieClickListener;

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;
        public ConstraintLayout mLayout;
        public ImageView mImageView;
        public ViewHolder (View v){
            super(v);
            mLayout = v.findViewById(R.id.item_root_layout);
            mTextView = v.findViewById(R.id.item_list_number_text);
            mImageView = v.findViewById(R.id.item_background_image);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mMovieClickListener.onMovieClick(clickedPosition);
        }
    }

    public interface MovieClickListener{
        void onMovieClick(int clickedItemIndex);
    }

    public MovieAdapter(ArrayList<MovieObject> myDataSet, MovieClickListener listener){
        mDataset = myDataSet;
        mMovieClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String posterPath = movieAPIManager.getPosterPath(mDataset.get(position));
        Picasso.get().load(posterPath).into(holder.mImageView);
        holder.mTextView.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateList(ArrayList<MovieObject> newMovies){
        mDataset = newMovies;
        notifyDataSetChanged();
    }


}
