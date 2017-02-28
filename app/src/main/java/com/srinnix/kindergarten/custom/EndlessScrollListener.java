package com.srinnix.kindergarten.custom;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by anhtu on 2/11/2017.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    public static final int POSITION_UP = 0;
    public static final int POSITION_DOWN = 1;

    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading;
    private int previousTotalItemCount = 0;
    private int visibleThreshold;
    private int positionLoading;

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        positionLoading = POSITION_DOWN;
        visibleThreshold = 2;
    }

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager, int positionLoading, int visibleThreshold) {
        this.layoutManager = layoutManager;
        this.positionLoading = positionLoading;
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        positionLoading = POSITION_DOWN;
        visibleThreshold = 3;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = layoutManager.getItemCount();
        int firstOrLastVisibleItemCount = 0;

        if (positionLoading == POSITION_DOWN) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstOrLastVisibleItemCount = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof GridLayoutManager) {
                firstOrLastVisibleItemCount = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
        }

        //if it's still loading and current total item > previous total item -> disable loading
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        //if it is not loading and users reach the threshold of loading more -> load more data
        boolean isReachThreshold;
        if (positionLoading == POSITION_DOWN) {
            isReachThreshold = (firstOrLastVisibleItemCount + visibleThreshold) >= totalItemCount;
        } else {
            isReachThreshold = (firstOrLastVisibleItemCount - visibleThreshold) <= 0;
        }

        if (!isLoading && isReachThreshold) {
            onLoadMore();
            isLoading = true;
        }
    }

    public abstract void onLoadMore();
}
