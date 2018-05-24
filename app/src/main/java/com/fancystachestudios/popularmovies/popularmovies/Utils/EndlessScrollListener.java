package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Code from the "codepath" github repository.
 * Instructions I followed: https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 * File I copied: https://gist.github.com/nesquena/d09dc68ff07e845cc622
 */

public abstract class EndlessScrollListener extends android.support.v7.widget.RecyclerView.OnScrollListener{
    //Minimum amount of items below current scroll before loading more
    private int visibleThreshold = 20;
    //Current offset
    private int currentPage = 0;
    //Total count of items from the last load
    private int previousTotalItemCount = 0;
    //True if we're waiting for the next dataset to load
    private boolean loading = true;
    //Starting page index
    private int startingPageIndex = 0;

    //The current LayoutManager
    private RecyclerView.LayoutManager mLayoutManager;

    //Constructor for LinearLayoutManager
    public EndlessScrollListener(LinearLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
    }

    //Constructor for GridLayoutManager
    public EndlessScrollListener(GridLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    //Gets the last visible item
    public int getLastVisibleItem(int[] lastVisibleItemPosition){
        int maxSize = 0;
        for(int i=0; i<lastVisibleItemPosition.length; i++){
            if(i==0){
                maxSize = lastVisibleItemPosition[i];
            }else if(lastVisibleItemPosition[i] > maxSize){
                maxSize = lastVisibleItemPosition[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int horizontalScrollAmount, int verticalScrollAmount) {
        //Last visible item position defaults to 0
        int lastVisibleItemPosition = 0;
        //Gets the total item count
        int totalItemCount = mLayoutManager.getItemCount();

        if(mLayoutManager instanceof StaggeredGridLayoutManager){
            //If the LayoutManager is a StaggeredGridLayoutManager,
            //Get the last visible item positions,
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            //And set the lastVisibleitemPosition to the last visible item
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        }else if(mLayoutManager instanceof  GridLayoutManager){
            //If the LayoutManager is a GridLayoutManager,
            //Set the lastVisibleItemPosition to the last visible item
            lastVisibleItemPosition = ((GridLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        }else if(mLayoutManager instanceof LinearLayoutManager){
            //If the LayoutManager is a LinearLayoutManager,
            //Set the lastVisibleItemPosition to the last visible item
            lastVisibleItemPosition = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        }

        //If the total item count is less than before, something must be wrong.
        if(totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        //If loading and the totalItemCount is larger than the previousTotalItemCount, the loading must be done.
        if(loading && (totalItemCount > previousTotalItemCount)){
            //Set the loading to false
            loading = false;
            //Set the previousItemCount to the totalItemCount
            previousTotalItemCount = totalItemCount;
        }
        //If not loading and we've scrolled down far enough (Based on the visibleThreshhold variable)
        if(!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount){
            //Increment the currentPage variable
            currentPage++;
            //Call onLoadMore with the currentPage and the totalItemCount
            onLoadMore(currentPage, totalItemCount);
            //And set loading to true
            loading = true;
        }
    }

    //Function resets the state of the scroll
    public void resetState(){
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    //Function is added when the class is re-created in another class -If that makes sense-
    public abstract boolean onLoadMore(int page, int totalitemsCount);

}
