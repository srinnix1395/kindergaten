package com.srinnix.kindergarten.schoolboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.schoolboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.schoolboard.adapter.viewholder.PostViewHolder;
import com.srinnix.kindergarten.schoolboard.adapter.viewholder.PostedViewHolder;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

/**
 * Created by DELL on 2/11/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_POSTED = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private ArrayList<Object> arrPost;
    private int accountType;
    private final RetryListener mRetryListener;

    public PostAdapter(Context context, ArrayList<Object> arrPost, RetryListener mRetryListener) {
        this.arrPost = arrPost;
        accountType = SharedPreUtils.getInstance(context).getAccountType();
        this.mRetryListener = mRetryListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_POST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }
            case VIEW_TYPE_POSTED: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posted, parent, false);
                return new PostedViewHolder(view);
            }
            case VIEW_TYPE_LOADING: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view, mRetryListener);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrPost.size() - 1) {
            ((LoadingViewHolder) holder).bindData(((LoadingItem) arrPost.get(position)));
        } else {
            ((PostedViewHolder) holder).bindData(((Post) arrPost.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return arrPost.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == arrPost.size() - 1) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_POSTED;
    }

    public interface RetryListener {
        void onClickRetry();
    }
}
