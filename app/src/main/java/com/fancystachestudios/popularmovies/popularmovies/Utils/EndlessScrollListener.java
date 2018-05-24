package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends android.support.v7.widget.RecyclerView.OnScrollListener{

    //Declare variables
    private GridLayoutManager mLayoutManager;
    private int currItemCount;
    private int previousItemCount;
    private int howManyScreensBuffered = 3;
    private int distanceToBottomForRefresh;
    private boolean loading = true;

    //Constructor sets the mLayoutManager and currItemCount variables
    protected EndlessScrollListener(GridLayoutManager mLayoutManager){
        this.mLayoutManager = mLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int horizontalScrollAmount, int verticalScrollAmount) {
        //Get the amount of views on screen
        int onScreenItemCount = mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition();
        //Set the distanceToBottomForRefresh to the amount on screen * how many screens should be buffered
        distanceToBottomForRefresh = onScreenItemCount * howManyScreensBuffered;
        //Set the currentItemCount to the current item count in the GridLayoutManager
        this.currItemCount = mLayoutManager.getItemCount();
        //Set the distanceToBottom to the currItemCount - the position of the last visible item on-screen
        int distanceToBottom = currItemCount - mLayoutManager.findLastVisibleItemPosition();
        //If we're not currently loading anything, and the buffer distance allows it,
        if(!loading && distanceToBottom <= distanceToBottomForRefresh){
            //Set loading to true
            loading = true;
            //Set the previousItemCount to the currItemCount
            previousItemCount = currItemCount;
            //Call addItems (Which should load more items)
            addItems();
        }
        //If there's more items than before, we must have just finished loading
        if(currItemCount > previousItemCount){
            //So set loading to false
            loading = false;
        }
    }

    //Function resets the scroll variables
    public void resetScroll(){
        currItemCount = 0;
        distanceToBottomForRefresh = 0;
        loading = true;
        previousItemCount = 0;
    }

    //Function is filled when a "new EndlessScrollListener" is declared
    protected abstract void addItems();
}