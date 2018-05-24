package com.fancystachestudios.popularmovies.popularmovies.Utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.AbsListView;

/**
 * Code from the "codepath" github repository. I don't have time to add comments at the time of writing, but you can find plenty of comments over at the links below. (Written by original author)
 * https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 * https://gist.github.com/nesquena/d09dc68ff07e845cc622
 */

public abstract class EndlessScrollListener extends android.support.v7.widget.RecyclerView.OnScrollListener{
    private int visibleThreshold = 2;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
    }

    public EndlessScrollListener(GridLayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

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
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if(mLayoutManager instanceof StaggeredGridLayoutManager){
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        }else if(mLayoutManager instanceof  GridLayoutManager){
            lastVisibleItemPosition = ((GridLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        }else if(mLayoutManager instanceof LinearLayoutManager){
            lastVisibleItemPosition = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
        }

        if(totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if(loading && (totalItemCount > previousTotalItemCount)){
            loading = false;
            previousTotalItemCount = totalItemCount;
        }
        if(!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount){
            currentPage++;
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }
    }

    public void resetState(){
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    public abstract boolean onLoadMore(int page, int totalitemsCount);

}
