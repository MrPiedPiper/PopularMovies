package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.R;

import java.util.List;

/**
 * Created by napuk on 5/20/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private MovieObject[] mDataset;

    public void setMovies (MovieObject[] movies){
        mDataset = movies;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder (TextView v){
            super(v);
            mTextView = v;
        }
    }

    public MovieAdapter(MovieObject[] myDataSet){
        mDataset = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_text_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset[position].getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void refreshMovies() {
        this.notifyDataSetChanged();
    }
}
