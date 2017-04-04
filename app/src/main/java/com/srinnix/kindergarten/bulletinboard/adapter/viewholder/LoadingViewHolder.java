package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.model.LoadingItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/11/2017.
 */

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.progressbar_itemloading)
    protected ProgressBar progressBar;

    @BindView(R.id.imageview_retry)
    protected ImageView imvRetry;

    protected PostAdapter.RetryListener mRetryListener;

    public LoadingViewHolder(View itemView, PostAdapter.RetryListener mRetryListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.mRetryListener = mRetryListener;
        progressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);
    }

    public void bindData(LoadingItem loadingItem) {
        switch (loadingItem.getLoadingState()) {
            case LoadingItem.STATE_IDLE_AND_LOADING: {
                imvRetry.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            }
            case LoadingItem.STATE_ERROR: {
                imvRetry.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    @OnClick(R.id.imageview_retry)
    void onClickRetry() {
        if (mRetryListener != null) {
            mRetryListener.onClickRetry();
        }
    }
}
