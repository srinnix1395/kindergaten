package com.srinnix.kindergarten.bulletinboard.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.model.LoadingItem3State;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/4/2017.
 */

public class Loading3StateViewHolder extends LoadingViewHolder{
    @BindView(R.id.imageview_start_loading)
    ImageView imvStartLoading;

    public Loading3StateViewHolder(View itemView, PostAdapter.RetryListener retryListener) {
        super(itemView, retryListener);
    }

    public void bindData(LoadingItem3State loadingItem3State) {
        switch (loadingItem3State.getLoadingState()) {
            case LoadingItem3State.STATE_LOADING: {
                imvRetry.setVisibility(View.GONE);
                imvStartLoading.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            }
            case LoadingItem3State.STATE_ERROR: {
                imvRetry.setVisibility(View.VISIBLE);
                imvStartLoading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
            }
            case LoadingItem3State.STATE_IDLE:{
                imvRetry.setVisibility(View.GONE);
                imvStartLoading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            }
        }
    }

    @OnClick(R.id.imageview_start_loading)
    public void onClickRetry() {
        if (mRetryListener != null) {
            mRetryListener.onClickRetry();
        }
    }
}
