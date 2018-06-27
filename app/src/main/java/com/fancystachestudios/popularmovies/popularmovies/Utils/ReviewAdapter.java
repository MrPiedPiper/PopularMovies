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
 * Created by napuk on 6/26/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<ReviewObject> mDataSet = new ArrayList<>();

    private MovieAPIManager movieAPIManager = new MovieAPIManager();

    final private ReviewClickListener mReviewClickListener;

    Context currContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mNumber;
        TextView mAuthor;
        TextView mContent;

        public ViewHolder(View v) {
            super(v);
            mNumber = v.findViewById(R.id.review_number);
            mAuthor = v.findViewById(R.id.review_author);
            mContent = v.findViewById(R.id.review_content);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mReviewClickListener.onReviewClick(getAdapterPosition());
        }
    }

    public interface ReviewClickListener{
        void onReviewClick(int clickedItemIndex);
    }

    public ReviewAdapter(Context context, ArrayList<ReviewObject> myDataset, ReviewClickListener listener){
        currContext = context;
        mDataSet = myDataset;
        mReviewClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewObject currReview = mDataSet.get(position);
        holder.mNumber.setText(String.valueOf(position+1));
        holder.mAuthor.setText(currReview.getAuthor());
        holder.mContent.setText(currReview.getContent());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
