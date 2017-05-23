package com.srinnix.kindergarten.custom;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by anhtu on 2/11/2017.
 */

public abstract class EndlessScrollUpListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private int previousTotalItemCount = 0;
    private int visibleThreshold;

    public EndlessScrollUpListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        visibleThreshold = 1;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            return;
        }
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemCount;

        firstVisibleItemCount = layoutManager.findFirstVisibleItemPosition();

        //if it's still loading and current total item > previous total item -> disable loading
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        //if it is not loading and users reach the threshold of loading more -> load more data
        boolean isReachThreshold = (firstVisibleItemCount - visibleThreshold) <= 0;

        if (!isLoading && isReachThreshold) {
            onLoadMore();
            isLoading = true;
        }
    }

    public abstract void onLoadMore();
}
