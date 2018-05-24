package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class EndlessScrollListener extends android.support.v7.widget.RecyclerView.OnScrollListener{

    GridLayoutManager mLayoutManager;
    int totalItemCount;
    int distanceToBottomForRefresh;
    public boolean loading = true;
    int previousItemCount;

    public EndlessScrollListener(GridLayoutManager mLayoutManager){
        this.mLayoutManager = mLayoutManager;
        this.totalItemCount = mLayoutManager.getItemCount();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int horizontalScrollAmount, int verticalScrollAmount) {
        super.onScrolled(recyclerView, horizontalScrollAmount, verticalScrollAmount);
        int onScreenItemCount = mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition();
        distanceToBottomForRefresh = onScreenItemCount * 3;
        this.totalItemCount = mLayoutManager.getItemCount();
        int distanceToBottom = totalItemCount - mLayoutManager.findLastVisibleItemPosition();
        if(!loading && distanceToBottom <= distanceToBottomForRefresh){
            loading = true;
            previousItemCount = totalItemCount;
            onScroll();
        }
        if(totalItemCount > previousItemCount){
            loading = false;
        }
    }

    public void resetScroll(){
        totalItemCount = 0;
        distanceToBottomForRefresh = 0;
        loading = true;
        previousItemCount = 0;
    }

    protected abstract void onScroll();
}