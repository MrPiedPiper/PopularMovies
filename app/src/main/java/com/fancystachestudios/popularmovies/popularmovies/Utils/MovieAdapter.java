package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieObject;
import com.fancystachestudios.popularmovies.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napuk on 5/20/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<MovieObject> mDataset;

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView mTextView;
        public ViewHolder (View v){
            super(v);
            mTextView = v.findViewById(R.id.test_text);
        }
    }

    public MovieAdapter(ArrayList<MovieObject> myDataSet){
        mDataset = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_text_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("mytest", "viewholder #:" + String.valueOf(position));
        Log.d("mytest", String.valueOf(mDataset.size()));
        Log.d("mytest", mDataset.get(position).getTitle());
        Log.d("mytest", mDataset.get(position).getBackdropPath());
        Log.d("mytest", mDataset.get(position).getPosterPath());
        holder.mTextView.setText(mDataset.get(position).getTitle());
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
