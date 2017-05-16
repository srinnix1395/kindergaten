package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.PostedViewHolder;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/11/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_POST = 1;
    private static final int VIEW_TYPE_LOADING = 2;

    private ArrayList<Object> arrPost;
    private RetryListener mRetryListener;
    private PostListener mPostListener;

    public PostAdapter(ArrayList<Object> arrPost, RetryListener mRetryListener, PostListener mPostListener) {
        this.arrPost = arrPost;
        this.mRetryListener = mRetryListener;
        this.mPostListener = mPostListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_POST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posted, parent, false);
                return new PostedViewHolder(view, mPostListener, viewType);
            }
            case VIEW_TYPE_LOADING: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view, mRetryListener);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        int size = payloads.size();
        if (size == 0) {
            onBindViewHolder(holder, position);
            return;
        }

        if (payloads.get(size - 1) instanceof Boolean) {
            ((PostedViewHolder) holder).bindImageLike((Boolean) payloads.get(size-1));
            return;
        }

        if (payloads.get(size - 1) instanceof ArrayList) {
            ArrayList arrayListPayloads = (ArrayList) payloads.get(size-1);
            ((PostedViewHolder) holder).bindImageLike((Boolean) arrayListPayloads.get(0), (Integer) arrayListPayloads.get(1));
            return;
        }

        if (payloads.get(size - 1) instanceof Integer) {
            ((PostedViewHolder) holder).bindComment((Integer) payloads.get(size-1));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
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
        if (arrPost.get(position) instanceof Post) {
            return VIEW_TYPE_POST;
        }

        return VIEW_TYPE_LOADING;
    }

    public interface RetryListener {
        void onClickRetry();
    }

    public interface PostListener {
        void onClickLike(int position);

        void onClickNumberLike(int position);

        void onClickImage(int position);

        void onClickComment(int position, boolean isShowKeyboard);

        void onClickShare(int position);
    }
}
