package com.srinnix.kindergarten.schoolboard.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by anhtu on 2/11/2017.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerView.LayoutManager layoutManager;
    private boolean isLoading;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private int visibleThreshold = 5;

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EndlessScrollListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
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

        //if it's still loading and current total item > previous total item -> disable loading
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        //if it is not loading and users reach the threshold of loading more -> load more data
        if (!isLoading && (lastVisibleItemCount + visibleThreshold) >= totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
            isLoading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemCount);
}
