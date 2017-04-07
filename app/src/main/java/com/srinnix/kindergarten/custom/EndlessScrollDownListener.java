package com.srinnix.kindergarten.custom;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by anhtu on 2/11/2017.
 */

public abstract class EndlessScrollDownListener extends RecyclerView.OnScrollListener {
    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading;
    private int previousTotalItemCount = 0;
    private int visibleThreshold;

    public EndlessScrollDownListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = 1;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemCount = 0;

        if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemCount = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemCount = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                isLoading = true;
            }
        }

        //if it's still loading and current total item > previous total item -> disable loading
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        //if it is not loading and users reach the threshold of loading more -> load more data
        boolean isReachThreshold = (lastVisibleItemCount + visibleThreshold) >= totalItemCount;

        if (!isLoading && isReachThreshold) {
            isLoading = true;
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
