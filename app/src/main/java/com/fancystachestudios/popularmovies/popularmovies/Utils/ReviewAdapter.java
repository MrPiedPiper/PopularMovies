package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.MovieAPIManager;
import com.fancystachestudios.popularmovies.popularmovies.MovieAPI.ReviewObject;
import com.fancystachestudios.popularmovies.popularmovies.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Custom adapter for the Review RecyclerView
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    //ArrayList stores the data for the RecyclerView to display
    private ArrayList<ReviewObject> mDataSet = new ArrayList<>();

    //Context variable stores the context of the activity using the RecyclerView
    Context currContext;

    public class ViewHolder extends RecyclerView.ViewHolder{

        //Review index
        TextView mNumber;
        //Review author
        TextView mAuthor;
        //Review content
        TextView mContent;

        public ViewHolder(View v) {
            super(v);
            //Get the views
            mNumber = v.findViewById(R.id.review_number);
            mAuthor = v.findViewById(R.id.review_author);
            mContent = v.findViewById(R.id.review_content);
        }
    }

    public ReviewAdapter(Context context, ArrayList<ReviewObject> myDataset){
        //When created, set the context and dataset
        currContext = context;
        mDataSet = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the View
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_layout, parent, false);
        //Create the ViewHolder
        ViewHolder vh = new ViewHolder(v);
        //Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Set the current review based on the ViewHolder's position
        ReviewObject currReview = mDataSet.get(position);

        //Set the Views using the currReview
        holder.mNumber.setText(String.valueOf(position+1));
        holder.mAuthor.setText(currReview.getAuthor());
        holder.mContent.setText(currReview.getContent());
    }

    @Override
    public int getItemCount() {
        //Return the dataset's size
        return mDataSet.size();
    }

}
